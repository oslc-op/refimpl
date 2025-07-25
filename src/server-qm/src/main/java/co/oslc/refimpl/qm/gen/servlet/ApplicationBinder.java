// Start of user code Copyright
/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Simple
 *
 * This file is generated by Lyo Designer (https://www.eclipse.org/lyo/)
 */
// End of user code

package co.oslc.refimpl.qm.gen.servlet;

// spotless:off
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.servlet.ServletContext;

import co.oslc.refimpl.qm.gen.RestDelegate;
import co.oslc.refimpl.qm.gen.ResourcesFactory;
import static co.oslc.refimpl.qm.gen.servlet.ServletListener.getConfigurationProperty;
import static co.oslc.refimpl.qm.gen.servlet.ServletListener.getServletUrlPattern;
import java.util.Set;

import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
// Start of user code imports
// End of user code
// spotless:on

// Start of user code pre_class_code
// End of user code

public class ApplicationBinder extends AbstractBinder {

    private static final Logger log = LoggerFactory.getLogger(ApplicationBinder.class);

    // Start of user code class_attributes
    // End of user code

    // Start of user code class_methods
    // End of user code

    public ApplicationBinder()
    {
        log.info("HK2 contract binding init");
    }

    @Override
    protected void configure() {
        log.info("HK2 contract binding start");
    
        // Start of user code ConfigureInitialise
        // End of user code
        bindFactory(LyoConfigurationFactory.class)
                .to(LyoGeneratedAppConfig.class);
        bindAsContract(RestDelegate.class).in(Singleton.class);
        bindFactory(ResourcesFactoryFactory.class).to(ResourcesFactory.class).in(Singleton.class);
    
    
    
        // Start of user code ConfigureFinalize
        // End of user code
    }
    static class ResourcesFactoryFactory implements Factory<ResourcesFactory> {
        @Override
        public ResourcesFactory provide() {
            return new ResourcesFactory(OSLC4JUtils.getServletURI());
        }
    
        @Override
        public void dispose(ResourcesFactory instance) {
        }
    }

    public static class LyoConfigurationFactory implements Factory<LyoGeneratedAppConfig> {
        private static final Logger logger = LoggerFactory.getLogger(LyoConfigurationFactory.class);

        private final LyoGeneratedAppConfig instance;

        @Inject
        public LyoConfigurationFactory(ServletContext context) {
            // TODO: refactor to avoid duplication with contextInitialized()
            String basePathKey = "baseurl";
            String fallbackBase = "http://localhost:8080";
            String servletName = "JAX-RS Servlet";

            String basePathProperty = getConfigurationProperty(basePathKey, fallbackBase, context, ServletListener.class);
            UriBuilder builder = UriBuilder.fromUri(basePathProperty);
            String baseUrl = builder.path(context.getContextPath()).build().toString();
            String servletUrlPattern = "services/";
            try {
                servletUrlPattern = getServletUrlPattern(context, servletName);
            } catch (Exception e1) {
                logger.error("servletListener encountered problems identifying the servlet URL pattern.", e1);
            }

            String corsFriendsString = getConfigurationProperty("cors.friends", "", context, ServletListener.class);
            Set<String> corsFriends;
            if (corsFriendsString == null || corsFriendsString.isEmpty()) {
                corsFriends = Set.of();
            } else {
                corsFriends = java.util.Arrays.stream(corsFriendsString.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(java.util.stream.Collectors.toSet());
            }
            this.instance = new LyoGeneratedAppConfig(baseUrl, servletUrlPattern, corsFriends);
        }

        @Override
        public LyoGeneratedAppConfig provide() {
            return instance;
        }

        @Override
        public void dispose(LyoGeneratedAppConfig instance) {
            // Noop
        }
    }

    
    
}
