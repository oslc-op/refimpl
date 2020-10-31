// Start of user code Copyright
/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *
 *  Contributors:
 *
 *     Sam Padgett         - initial API and implementation
 *     Michael Fiedler     - adapted for OSLC4J
 *     Jad El-khoury       - initial implementation of code generator (https://bugs.eclipse.org/bugs/show_bug.cgi?id=422448)
 *     Andrii Berezovskyi  - change URL configuration logic (Bug 509767)
 *
 * This file is generated by org.eclipse.lyo.oslc4j.codegenerator
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.am.gen.servlet;

import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import co.oslc.refimpl.am.gen.OSLCAMServer2020RefImplManager;

// Start of user code imports
// End of user code


/**
 * During the initialization of this ServletListener, the base URI for the OSLC resources produced by this server is configured through the OSLC4J method setPublicURI().
 * <p>
 * A number of approaches can be used to set this base URI:
 * <ol>
 * <li>Set a system environment property of name "LYO_BASE".</li>
 * <li>In the web.xml file, set a {@code context-param} property, with name "co.oslc.refimpl.am.gen.servlet.baseurl}</li>
 * <li>Override the value of the DEFAULT_BASE constant.</li>
 * </ol>
 */
public class ServletListener implements ServletContextListener  {
    private static final Logger logger = LoggerFactory.getLogger(ServletListener.class);

    // Start of user code class_attributes
    // End of user code

    public ServletListener() {
        super();
    }

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent)
    {
        //These are default values. You can modify any of them early in this method.
        String basePathEnvKey = "LYO_BASE";
        String basePathContextPropertyKey = String.format("%s.%s", ServletListener.class.getPackage().getName(), "baseurl");
        String fallbackBase = "http://localhost:8080";
        String servletName = "JAX-RS Servlet";

        // Start of user code contextInitialized_init
        // End of user code

        String baseUrl = generateBasePath(servletContextEvent, basePathEnvKey, basePathContextPropertyKey, fallbackBase);
        String servletUrlPattern = "services/";
        try {
            servletUrlPattern = getServletUrlPattern(servletContextEvent, servletName);
        } catch (Exception e1) {
            logger.error("servletListner encountered problems identifying the servlet URL pattern.", e1);
        }
        try {
            logger.info("Setting public URI: " + baseUrl);
            OSLC4JUtils.setPublicURI(baseUrl);
            logger.info("Setting servlet path: " + servletUrlPattern);
            OSLC4JUtils.setServletPath(servletUrlPattern);
        } catch (MalformedURLException e) {
            logger.error("servletListner encountered MalformedURLException.", e);
        } catch (IllegalArgumentException e) {
            logger.error("servletListner encountered IllegalArgumentException.", e);
        }

        logger.info("servletListner contextInitialized.");

        // Establish connection to data backbone etc ...
        OSLCAMServer2020RefImplManager.contextInitializeServletListener(servletContextEvent);

        // Start of user code contextInitialized_final
        // End of user code
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        // Start of user code contextDestroyed_init
        // End of user code

        // Shutdown connections to data backbone etc...
        OSLCAMServer2020RefImplManager.contextDestroyServletListener(servletContextEvent);

        // Start of user code contextDestroyed_final
        // End of user code
    }

    // Start of user code class_methods
    // End of user code

    private static String generateBasePath(final ServletContextEvent servletContextEvent, String basePathEnvKey, String basePathContextPropertyKey, String fallbackBase) {
        String base = getBasePathFromEnvironment(basePathEnvKey).orElseGet(() -> getBasePathFromContext(servletContextEvent, basePathContextPropertyKey).orElseGet(() -> fallbackBase));
        UriBuilder builder = UriBuilder.fromUri(base);
        return builder.path(servletContextEvent.getServletContext().getContextPath()).build().toString();
    }

    private static Optional<String> getBasePathFromEnvironment(String basePathEnvKey) {
        final Map<String, String> env = System.getenv();
        if(env.containsKey(basePathEnvKey)) {
            logger.info("Found {} env variable", basePathEnvKey);
            return Optional.of(env.get(basePathEnvKey));
        }
        return Optional.empty();
    }

    private static Optional<String> getBasePathFromContext(final ServletContextEvent servletContextEvent, String basePathContextPropertyKey) {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        String base = servletContext.getInitParameter(basePathContextPropertyKey);
        if (base == null || base.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(base);
    }

    private static String getServletUrlPattern(final ServletContextEvent servletContextEvent, String servletName) throws Exception {
        final ServletContext servletContext = servletContextEvent.getServletContext();

        ServletRegistration servletRegistration = servletContext.getServletRegistration(servletName);
        if (servletRegistration == null) {
            throw new NoSuchElementException("no servlet with name \"" + servletName + "\" is found.");
        }
        java.util.Collection<java.lang.String> mappings = servletRegistration.getMappings();
        if (mappings.size() != 1) {
            throw new NoSuchElementException("unable to identify servlet mappings for servlet with name \"" + servletName + "\".");
        }
        String mapping = (String) mappings.toArray()[0];

        //url patterns in  most cases end with '\*'. But a url-pattern with just '\' may be found for exact matches.
        if (mapping.endsWith("*"))
            mapping = mapping.substring(0, mapping.length()-1);
        return mapping;
    }
}

