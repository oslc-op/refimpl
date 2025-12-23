package co.oslc.refimpl.cm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.stream.Collectors;

import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.servlet.SessionHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.glassfish.jersey.servlet.ServletContainer;

import co.oslc.refimpl.cm.gen.servlet.Application;
import co.oslc.refimpl.cm.gen.servlet.CredentialsFilter;
import co.oslc.refimpl.cm.gen.servlet.ServletListener;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        int port = 8080;
        String portEnv = System.getenv("PORT");
        if (portEnv != null) {
            port = Integer.parseInt(portEnv);
        }

        Server server = new Server(port);

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
        if (staticResources != null) {
            String externalForm = staticResources.toExternalForm();
            logger.info("Serving static content from: " + externalForm);

            context.setBaseResource(ResourceFactory.of(context).newResource(externalForm));

            // Use ResourceServlet for static assets
            ServletHolder staticServlet = new ServletHolder("default", ResourceServlet.class);
            staticServlet.setInitParameter("dirAllowed", "true");
            staticServlet.setInitParameter("acceptRanges", "true");
            context.addServlet(staticServlet, "/static/*");
            // Also map swagger-ui
            context.addServlet(staticServlet, "/swagger-ui/*");
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

    public static class IndexServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            // Serve static/index.html
            // If the path is exactly "/" or "/index.html"
            String path = req.getServletPath();
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
