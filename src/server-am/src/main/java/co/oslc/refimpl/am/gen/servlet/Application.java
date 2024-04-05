// Start of user code Copyright
/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *
 *     Michael Fiedler     - initial API and implementation for Bugzilla adapter
 *     Jad El-khoury       - initial implementation of code generator (https://bugs.eclipse.org/bugs/show_bug.cgi?id=422448)
 *     Matthieu Helleboid  - Support for multiple Service Providers.
 *     Anass Radouani      - Support for multiple Service Providers.
 *
 * This file is generated by org.eclipse.lyo.oslc4j.codegenerator
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.am.gen.servlet;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.inject.Inject;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.AllowedValues;
import org.eclipse.lyo.oslc4j.core.model.Compact;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.Dialog;
import org.eclipse.lyo.oslc4j.core.model.Error;
import org.eclipse.lyo.oslc4j.core.model.ExtendedError;
import org.eclipse.lyo.oslc4j.core.model.OAuthConfiguration;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.PrefixDefinition;
import org.eclipse.lyo.oslc4j.core.model.Preview;
import org.eclipse.lyo.oslc4j.core.model.Property;
import org.eclipse.lyo.oslc4j.core.model.Publisher;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.eclipse.lyo.oslc4j.core.model.ResourceShapeFactory;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import co.oslc.refimpl.am.gen.services.RootServicesService;
import co.oslc.refimpl.am.gen.services.ServiceProviderCatalogService;
import co.oslc.refimpl.am.gen.services.ServiceProviderService;
import co.oslc.refimpl.am.gen.services.ResourceShapeService;

import org.eclipse.lyo.oslc.domains.am.LinkType;
import org.eclipse.lyo.oslc.domains.Person;
import org.eclipse.lyo.oslc.domains.am.Resource;
import org.eclipse.lyo.oslc.domains.am.Oslc_amDomainConstants;
import org.eclipse.lyo.oslc.domains.DctermsDomainConstants;
import org.eclipse.lyo.oslc.domains.FoafDomainConstants;
import org.eclipse.lyo.oslc.domains.jazz_am.Jazz_amDomainConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcDomainConstants;
import org.eclipse.lyo.oslc.domains.RdfsDomainConstants;
import co.oslc.refimpl.am.gen.services.ResourcesService;
import co.oslc.refimpl.am.gen.services.LinksService;
import co.oslc.refimpl.am.gen.services.WsResource;
import co.oslc.refimpl.am.gen.services.WsLinkType;

// Start of user code imports
// End of user code

// Start of user code pre_class_code
// End of user code

/**
 * Generated by Lyo Designer 6.0.0.qualifier
 */

@OpenAPIDefinition(info = @Info(title = "AM", version = "1.0.0"), servers = @Server(url = "/services/"))
public class Application extends jakarta.ws.rs.core.Application {

    private static final Set<Class<?>>         RESOURCE_CLASSES                          = new HashSet<Class<?>>();
    private static final Map<String, Class<?>> RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP = new HashMap<String, Class<?>>();

    // Start of user code class_attributes
    // End of user code

    // Start of user code class_methods
    // End of user code

    @Override
    public Set<Object> getSingletons() {
        return Collections.singleton(new ApplicationBinder());
    }
    static
    {
        RESOURCE_CLASSES.addAll(JenaProvidersRegistry.getProviders());
        RESOURCE_CLASSES.addAll(Json4JProvidersRegistry.getProviders());
        RESOURCE_CLASSES.add(ResourcesService.class);
        RESOURCE_CLASSES.add(LinksService.class);
        RESOURCE_CLASSES.add(WsResource.class);
        RESOURCE_CLASSES.add(WsLinkType.class);

        // Catalog resources
        RESOURCE_CLASSES.add(ServiceProviderCatalogService.class);
        RESOURCE_CLASSES.add(ServiceProviderService.class);
        RESOURCE_CLASSES.add(ResourceShapeService.class);

        // Swagger resources
        RESOURCE_CLASSES.add(OpenApiResource.class);
        RESOURCE_CLASSES.add(AcceptHeaderOpenApiResource.class);

        // OAuth resources
        RESOURCE_CLASSES.add(RootServicesService.class);
        try {
            RESOURCE_CLASSES.add(Class.forName("org.eclipse.lyo.server.oauth.webapp.services.ConsumersService"));
            RESOURCE_CLASSES.add(Class.forName("org.eclipse.lyo.server.oauth.webapp.services.OAuthService"));
        } catch (ClassNotFoundException e) {
            // Start of user code OAuthServiceClasses_notFound
            // End of user code
        }
        
        // Start of user code Custom Resource Classes
        // End of user code

        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_ALLOWED_VALUES,           AllowedValues.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_COMPACT,                  Compact.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_CREATION_FACTORY,         CreationFactory.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_DIALOG,                   Dialog.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_ERROR,                    Error.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_EXTENDED_ERROR,           ExtendedError.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_OAUTH_CONFIGURATION,      OAuthConfiguration.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_PREFIX_DEFINITION,        PrefixDefinition.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_PREVIEW,                  Preview.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_PROPERTY,                 Property.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_PUBLISHER,                Publisher.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_QUERY_CAPABILITY,         QueryCapability.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_RESOURCE_SHAPE,           ResourceShape.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_SERVICE,                  Service.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_SERVICE_PROVIDER,         ServiceProvider.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(OslcConstants.PATH_SERVICE_PROVIDER_CATALOG, ServiceProviderCatalog.class);

        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Oslc_amDomainConstants.LINKTYPE_PATH, LinkType.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(FoafDomainConstants.PERSON_PATH, Person.class);
        RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.put(Oslc_amDomainConstants.RESOURCE_PATH, Resource.class);
    }

    @Inject
    public Application(ServiceLocator locator)
           throws OslcCoreApplicationException,
                  URISyntaxException
    {
        ServiceLocatorUtilities.enableImmediateScope(locator);
        final String BASE_URI = "http://localhost/validatingResourceShapes";
        for (final Map.Entry<String, Class<?>> entry : RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP.entrySet()) {
            ResourceShapeFactory.createResourceShape(BASE_URI, OslcConstants.PATH_RESOURCE_SHAPES, entry.getKey(), entry.getValue());
        }
    }

    @Override 
    public Set<Class<?>> getClasses() { 
        return RESOURCE_CLASSES; 
    }

    public static Map<String, Class<?>> getResourceShapePathToResourceClassMap() {
        return RESOURCE_SHAPE_PATH_TO_RESOURCE_CLASS_MAP;
    }
}
