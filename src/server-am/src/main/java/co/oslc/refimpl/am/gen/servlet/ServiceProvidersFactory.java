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
 *     Russell Boykin       - initial API and implementation
 *     Alberto Giammaria    - initial API and implementation
 *     Chris Peters         - initial API and implementation
 *     Gianluca Bernardini  - initial API and implementation
 *     Michael Fiedler      - Bugzilla adapter implementation
 *     Jad El-khoury        - initial implementation of code generator (https://bugs.eclipse.org/bugs/show_bug.cgi?id=422448)
 *     Matthieu Helleboid   - Allow Service Provider Factory class to be specific for each defined ServiceProvider
 *     Anass Radouani       - Allow Service Provider Factory class to be specific for each defined ServiceProvider
 *
 * This file is generated by org.eclipse.lyo.oslc4j.codegenerator
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.am.gen.servlet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.PrefixDefinition;
import org.eclipse.lyo.oslc4j.core.model.Publisher;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderFactory;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;

import co.oslc.refimpl.am.gen.ServiceProviderInfo;

import org.eclipse.lyo.oslc.domains.am.Oslc_amDomainConstants;
import org.eclipse.lyo.oslc.domains.DctermsDomainConstants;
import org.eclipse.lyo.oslc.domains.FoafDomainConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcDomainConstants;
import org.eclipse.lyo.oslc.domains.RdfsDomainConstants;
import co.oslc.refimpl.am.gen.services.ResourcesService;
import co.oslc.refimpl.am.gen.services.LinksService;

// Start of user code imports
// End of user code

public class ServiceProvidersFactory
{
    private static Class<?>[] RESOURCE_CLASSES =
    {
        ResourcesService.class, LinksService.class
    };

    private ServiceProvidersFactory()
    {
        super();
    }

    public static URI constructURI(final String serviceProviderId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        String instanceURI = "serviceProviders/{serviceProviderId}";

        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }

    public static URI constructURI(final ServiceProviderInfo serviceProviderInfo)
    {
        return constructURI(serviceProviderInfo.serviceProviderId);
    }

    public static String constructIdentifier(final String serviceProviderId)
    {
        return serviceProviderId;
    }

    public static String constructIdentifier(final ServiceProviderInfo serviceProviderInfo)
    {
        return constructIdentifier(serviceProviderInfo.serviceProviderId);
    }

    public static ServiceProvider createServiceProvider(final ServiceProviderInfo serviceProviderInfo) 
            throws OslcCoreApplicationException, URISyntaxException, IllegalArgumentException {
        // Start of user code init
        // End of user code
        URI serviceProviderURI = constructURI(serviceProviderInfo);
        String identifier = constructIdentifier(serviceProviderInfo);
        String basePath = OSLC4JUtils.getServletURI();
        String title = serviceProviderInfo.name;
        String description = String.format("%s (id: %s; kind: %s)",
            "Service Provider",
            identifier,
            "Service Provider");
        Publisher publisher = null;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("serviceProviderId", serviceProviderInfo.serviceProviderId);

        ServiceProvider serviceProvider = ServiceProviderFactory.createServiceProvider(basePath,
                                                    "",
                                                    title,
                                                    description,
                                                    publisher,
                                                    RESOURCE_CLASSES,
                                                    parameterMap);

        final PrefixDefinition[] prefixDefinitions =
        {
            new PrefixDefinition(OslcConstants.DCTERMS_NAMESPACE_PREFIX, new URI(OslcConstants.DCTERMS_NAMESPACE)),
            new PrefixDefinition(OslcConstants.OSLC_CORE_NAMESPACE_PREFIX, new URI(OslcConstants.OSLC_CORE_NAMESPACE)),
            new PrefixDefinition(OslcConstants.OSLC_DATA_NAMESPACE_PREFIX, new URI(OslcConstants.OSLC_DATA_NAMESPACE)),
            new PrefixDefinition(OslcConstants.RDF_NAMESPACE_PREFIX, new URI(OslcConstants.RDF_NAMESPACE)),
            new PrefixDefinition(OslcConstants.RDFS_NAMESPACE_PREFIX, new URI(OslcConstants.RDFS_NAMESPACE)),
            new PrefixDefinition(Oslc_amDomainConstants.ARCHITECTURE_MANAGEMENT_NAMSPACE_PREFIX, new URI(Oslc_amDomainConstants.ARCHITECTURE_MANAGEMENT_NAMSPACE))
,
            new PrefixDefinition(DctermsDomainConstants.DUBLIN_CORE_NAMSPACE_PREFIX, new URI(DctermsDomainConstants.DUBLIN_CORE_NAMSPACE))
,
            new PrefixDefinition(FoafDomainConstants.FOAF_NAMSPACE_PREFIX, new URI(FoafDomainConstants.FOAF_NAMSPACE))
,
            new PrefixDefinition(OslcDomainConstants.OSLC_NAMSPACE_PREFIX, new URI(OslcDomainConstants.OSLC_NAMSPACE))
,
            new PrefixDefinition(RdfsDomainConstants.RDFS_NAMSPACE_PREFIX, new URI(RdfsDomainConstants.RDFS_NAMSPACE))
        };
        serviceProvider.setPrefixDefinitions(prefixDefinitions);

        serviceProvider.setAbout(serviceProviderURI);
        serviceProvider.setIdentifier(identifier);
        serviceProvider.setCreated(new Date());
        serviceProvider.setDetails(new URI[] {serviceProviderURI});

        // Start of user code finalize
        // End of user code
        return serviceProvider;
    }
}
