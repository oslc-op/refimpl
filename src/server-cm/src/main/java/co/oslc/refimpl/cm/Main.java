package co.oslc.refimpl.cm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.stream.Collectors;

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.ee10.jsp.JettyJspServlet;
import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.servlet.ServletMapping;
import org.eclipse.jetty.ee10.servlet.SessionHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.VirtualThreads;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.VirtualThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.glassfish.jersey.servlet.ServletContainer;

import co.oslc.refimpl.cm.gen.servlet.Application;
import co.oslc.refimpl.cm.gen.servlet.CredentialsFilter;
import co.oslc.refimpl.cm.gen.servlet.EmbeddedJspStarter;
import co.oslc.refimpl.cm.gen.servlet.ServletListener;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final int DEFAULT_MIN_THREADS = 8;
    private static final int DEFAULT_MAX_THREADS = 64;
    private static final int DEFAULT_RESERVED_THREADS = -1;
    private static final int DEFAULT_VIRTUAL_MAX_CONCURRENT_TASKS = 500;

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portEnv = System.getenv("PORT");
        if (portEnv != null) {
            port = Integer.parseInt(portEnv);
        }

        QueuedThreadPool threadPool = createThreadPool();
        Server server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);
        server.setStopAtShutdown(true);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        // Enable Session Handler
        context.setSessionHandler(new SessionHandler());

        server.setHandler(context);

        // Set Context Parameters
        String baseUrl = System.getenv("OSLC_BASE_URL");
        if (baseUrl == null) {
            baseUrl = "http://localhost:" + port + "/";
        }
        context.setInitParameter("co.oslc.refimpl.cm.gen.servlet.baseurl", baseUrl);
        context.setInitParameter("co.oslc.refimpl.cm.gen.servlet.cors.friends", "*");

        // Add Listener
        context.addEventListener(new ServletListener());

        // Add CredentialsFilter
        context.addFilter(CredentialsFilter.class, "/services/*", EnumSet.of(DispatcherType.REQUEST));

        // JSP servlet
        Path tempDir = Files.createTempDirectory("server-cm-jsp");
        Path scratchDir = Files.createDirectories(tempDir.resolve("scratch"));

        context.setAttribute(
                ServletContext.TEMPDIR,
                tempDir.toFile());

        // Jasper expects a non-system context classloader.
        context.setClassLoader(
                new URLClassLoader(
                        new URL[0],
                        Thread.currentThread().getContextClassLoader()));

        // Initialize Apache Jasper when the context starts.
        context.addBean(new EmbeddedJspStarter(context));

        // The JSP servlet must be named "jsp".
        ServletHolder jsp = new ServletHolder(
                "jsp",
                JettyJspServlet.class);

        jsp.setInitOrder(0);
        jsp.setInitParameter("scratchdir", scratchDir.toString());
        jsp.setInitParameter("fork", "false");
        jsp.setInitParameter("xpoweredBy", "false");
        jsp.setInitParameter("keepgenerated", "true");

        context.addServlet(jsp, "*.jsp");

        ServletMapping swaggerIndexJsp = new ServletMapping();
        swaggerIndexJsp.setServletName("jsp");
        swaggerIndexJsp.setPathSpec("/swagger-ui/index.jsp");

        context.getServletHandler().addServletMapping(swaggerIndexJsp);

        context.setAttribute(
                InstanceManager.class.getName(),
                new SimpleInstanceManager());

        // Add Jersey Servlet
        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer());
        jerseyServlet.setName("JAX-RS Servlet"); // Explicitly set name for ServletListener
        jerseyServlet.setInitParameter("jakarta.ws.rs.Application", Application.class.getName());
        jerseyServlet.setInitOrder(1);
        context.addServlet(jerseyServlet, "/services/*");

        // Add Index Servlet for root path
        context.addServlet(new ServletHolder(new IndexServlet()), "/");

        // Add Static Content Servlet for other assets
        URL staticResources = Main.class.getClassLoader().getResource("static");
        URI applicationJar = Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI();

        ResourceFactory resources = ResourceFactory.of(context);

        Resource jarRoot = resources.newJarFileResource(applicationJar);
        Resource staticRoot = resources.newClassLoaderResource("static/");

        Resource webRoot = staticRoot == null
                ? jarRoot
                : ResourceFactory.combine(staticRoot, jarRoot);

        context.setBaseResource(webRoot);
        if (staticResources != null) {
            String externalForm = staticResources.toExternalForm();
            logger.info("Serving static content from: " + externalForm);

            // context.setBaseResource(ResourceFactory.of(context).newResource(externalForm));

            // Use separate ResourceServlet instances for each path prefix. A servlet holder
            // can only be registered once reliably in Jetty's servlet mapping table.
            ServletHolder staticServlet = new ServletHolder("static", ResourceServlet.class);
            staticServlet.setInitParameter("dirAllowed", "true");
            staticServlet.setInitParameter("acceptRanges", "true");
            context.addServlet(staticServlet, "/static/*");

            ServletHolder swaggerServlet = new ServletHolder("swagger", ResourceServlet.class);
            swaggerServlet.setInitParameter("baseResource", "swagger-ui");
            swaggerServlet.setInitParameter("dirAllowed", "true");
            swaggerServlet.setInitParameter("acceptRanges", "true");
            context.addServlet(swaggerServlet, "/swagger-ui/*");
        } else {
            logger.warn("Static resources not found!");
        }

        try {
            server.start();
            logger.info("Server started on port " + port);
            server.join();
        } catch (Exception e) {
            logger.error("Error starting server", e);
            System.exit(1);
        }
    }

    private static QueuedThreadPool createThreadPool() {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName(System.getenv().getOrDefault("JETTY_THREAD_POOL_NAME", "qtp"));
        threadPool.setMinThreads(getIntEnv("JETTY_MIN_THREADS", DEFAULT_MIN_THREADS));
        threadPool.setMaxThreads(getIntEnv("JETTY_MAX_THREADS", DEFAULT_MAX_THREADS));
        threadPool.setReservedThreads(getIntEnv("JETTY_RESERVED_THREADS", DEFAULT_RESERVED_THREADS));

        if (getBooleanEnv("JETTY_VIRTUAL_THREADS", true)) {
            if (VirtualThreads.areSupported()) {
                VirtualThreadPool virtualThreadPool = new VirtualThreadPool();
                virtualThreadPool.setName(System.getenv().getOrDefault(
                        "JETTY_VIRTUAL_THREAD_NAME", "qtp-virtual-"));
                virtualThreadPool.setMaxConcurrentTasks(getIntEnv(
                        "JETTY_VIRTUAL_MAX_CONCURRENT_TASKS", DEFAULT_VIRTUAL_MAX_CONCURRENT_TASKS));
                virtualThreadPool.setTracking(getBooleanEnv("JETTY_VIRTUAL_TRACKING", false));
                threadPool.setVirtualThreadsExecutor(virtualThreadPool);
                logger.info("Jetty virtual threads enabled for application tasks");
            } else {
                logger.warn("Virtual threads requested but unavailable on this Java runtime; using platform threads");
            }
        }

        return threadPool;
    }

    private static int getIntEnv(String name, int defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Ignoring invalid integer value for {}: {}", name, value);
            return defaultValue;
        }
    }

    private static boolean getBooleanEnv(String name, boolean defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : Boolean.parseBoolean(value);
    }

    public static class IndexServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            // Serve static/index.html
            // If the path is exactly "/" or "/index.html"
            String path = req.getRequestURI().substring(req.getContextPath().length());
            if (path.equals("/") || path.equals("/index.html") || path.equals("/index.jsp")) {
                try (InputStream in = Main.class.getClassLoader().getResourceAsStream("static/index.html")) {
                    if (in != null) {
                        String content = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
                                .lines().collect(Collectors.joining("\n"));

                        resp.setContentType("text/html");
                        resp.setCharacterEncoding("UTF-8");
                        resp.getWriter().write(content);
                    } else {
                        resp.sendError(404, "Index not found");
                    }
                }
            } else {
                resp.sendError(404);
            }
        }
    }
}
