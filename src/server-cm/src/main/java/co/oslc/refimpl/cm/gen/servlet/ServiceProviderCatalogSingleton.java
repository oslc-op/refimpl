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
 *     Michael Fiedler      - adapted for Bugzilla service provider
 *     Jad El-khoury        - initial implementation of code generator (422448)
 *     Matthieu Helleboid   - initialize each service provider separately
 *     Anass Radouani       - initialize each service provider separately
 *
 * This file is generated by org.eclipse.lyo.oslc4j.codegenerator
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.cm.gen.servlet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;

import co.oslc.refimpl.cm.gen.OSLCCMServer2020RefImplManager;
import co.oslc.refimpl.cm.gen.ServiceProviderInfo;

// Start of user code imports
// End of user code

/**
 * This is the OSLC service provider catalog for the adapter.  Service providers are
 * not registered with the catalog until a request comes in to access either the catalog or a
 * specific service provider.   This request could be from an external consumer or an internal
 * request triggered by a consumer accessing a change request.
 *
 * Information about the desired list of ServiceProviders is retrieved from the Manager.getServiceProviderInfos() method.
 * A ServiceProvider is created and registered for each entry in that list.
 *
 * The registered service providers are refreshed on each catalog or service provider collection
 * request.
 */
public class ServiceProviderCatalogSingleton
{
    private static final ServiceProviderCatalog serviceProviderCatalog;
    private static final SortedMap<String, ServiceProvider> serviceProviders = new TreeMap<String, ServiceProvider>();
    // Start of user code class_attributes
    // End of user code

    static {
        serviceProviderCatalog = new ServiceProviderCatalog();
        URI catalogUri = UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path("/catalog/singleton").build();
        serviceProviderCatalog.setAbout(catalogUri);
        serviceProviderCatalog.setTitle("Service Provider Catalog");
        serviceProviderCatalog.setDescription("Service Provider Catalog");
    }

    // Start of user code class_methods
    // End of user code

    private ServiceProviderCatalogSingleton()
    {
        super();
    }


    public static URI getUri()
    {
        return serviceProviderCatalog.getAbout();
    }

    public static ServiceProviderCatalog getServiceProviderCatalog(HttpServletRequest httpServletRequest)
    {
        initServiceProviders(httpServletRequest);
        return serviceProviderCatalog;
    }

    public static ServiceProvider [] getServiceProviders(HttpServletRequest httpServletRequest)
    {
        synchronized(serviceProviders)
        {
            initServiceProviders(httpServletRequest);
            return serviceProviders.values().toArray(new ServiceProvider[ serviceProviders.size()]);
        }
    }

    public static boolean containsServiceProvider(final String identifier) {
        return serviceProviders.containsKey(identifier);
    }

    public static boolean containsServiceProvider(final ServiceProvider serviceProvider) {
        return containsServiceProvider(serviceProvider.getIdentifier());
    }

    // This version is for self-registration and thus package-protected
    public static ServiceProvider register(final ServiceProvider serviceProvider)
                                            throws URISyntaxException
    {
        if (containsServiceProvider(serviceProvider)) {
            throw new IllegalArgumentException(String.format("The SP '%s' was already registered", serviceProvider.getIdentifier()));
        }
        synchronized(serviceProviders)
        {
            if (containsServiceProvider(serviceProvider)) {
                throw new IllegalArgumentException(String.format("The SP '%s' was already registered", serviceProvider.getIdentifier()));
            }
            return registerNoSync(serviceProvider);
        }
    }

    /**
    * Register a service provider with the OSLC catalog
    *
    */
    private static ServiceProvider registerNoSync(final ServiceProvider serviceProvider)
    {
        final SortedSet<URI> serviceProviderDomains = getServiceProviderDomains(serviceProvider);
        serviceProviderCatalog.addServiceProvider(serviceProvider);
        serviceProviderCatalog.addDomains(serviceProviderDomains);
        serviceProviders.put(serviceProvider.getIdentifier(), serviceProvider);
        return serviceProvider;
    }

    public static void deregister(final ServiceProvider serviceProvider)
    {
        synchronized(serviceProviders)
        {
            final ServiceProvider deregisteredServiceProvider =
                serviceProviders.remove(serviceProvider.getIdentifier());

            if (deregisteredServiceProvider != null)
            {
                final SortedSet<URI> remainingDomains = new TreeSet<URI>();

                for (final ServiceProvider remainingServiceProvider : serviceProviders.values())
                {
                    remainingDomains.addAll(getServiceProviderDomains(remainingServiceProvider));
                }

                final SortedSet<URI> removedServiceProviderDomains = getServiceProviderDomains(deregisteredServiceProvider);

                removedServiceProviderDomains.removeAll(remainingDomains);
                serviceProviderCatalog.removeDomains(removedServiceProviderDomains);
                serviceProviderCatalog.removeServiceProvider(deregisteredServiceProvider);
            }
            else
            {
                throw new WebApplicationException(Status.NOT_FOUND);
            }
        }
    }


    public static ServiceProvider getServiceProvider(HttpServletRequest httpServletRequest, final String serviceProviderId)
    {
        ServiceProvider serviceProvider;

        synchronized(serviceProviders)
        {
            String identifier = ServiceProvidersFactory.constructIdentifier(serviceProviderId);
            serviceProvider = serviceProviders.get(identifier);

            //One retry refreshing the service providers
            if (serviceProvider == null)
            {
                getServiceProviders(httpServletRequest);
                serviceProvider = serviceProviders.get(identifier);
            }
        }

        if (serviceProvider != null)
        {
            return serviceProvider;
        }

        throw new WebApplicationException(Status.NOT_FOUND);
    }

    private static SortedSet<URI> getServiceProviderDomains(final ServiceProvider serviceProvider)
    {
        final SortedSet<URI> domains = new TreeSet<URI>();

        if (serviceProvider!=null) {
            final Service [] services = serviceProvider.getServices();
            for (final Service service : services)
            {
                final URI domain = service.getDomain();

                domains.add(domain);
            }
        }
        return domains;
    }

    /**
    * Retrieve the set of initial ServiceProviders as returned from the Manager.getServiceProviderInfos() method, and construct a service provider for each.
    *
    * Each ServiceProvider ID is added to the parameter map which will be used during service provider
    * creation to create unique URI paths for each ServiceProvider. 
    *
    */
    protected static void initServiceProviders (HttpServletRequest httpServletRequest)
    {
        try {
            // Start of user code initServiceProviders
            // End of user code

            ServiceProviderInfo [] serviceProviderInfos = OSLCCMServer2020RefImplManager.getServiceProviderInfos(httpServletRequest);
            //Register each service provider
            for (ServiceProviderInfo serviceProviderInfo : serviceProviderInfos) {
                if (!containsServiceProvider(ServiceProvidersFactory.constructIdentifier(serviceProviderInfo))) {
                    ServiceProvider aServiceProvider = ServiceProvidersFactory.createServiceProvider(serviceProviderInfo);
                    register(aServiceProvider);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(e,Status.INTERNAL_SERVER_ERROR);
        }
    }
}
