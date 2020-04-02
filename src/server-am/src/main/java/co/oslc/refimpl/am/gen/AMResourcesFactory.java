// Start of user code Copyright
/*******************************************************************************
 * Copyright (c) 2017 Jad El-khoury.
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
 *     Jad El-khoury        - initial implementation
 *     
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.am.gen;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc.domains.am.LinkType;
import org.eclipse.lyo.oslc.domains.Person;
import org.eclipse.lyo.oslc.domains.am.Resource;

// Start of user code imports
// End of user code

// Start of user code pre_class_code
// End of user code

public class AMResourcesFactory {

    // Start of user code class_attributes
    // End of user code
    
    // Start of user code class_methods
    // End of user code

    //methods for LinkType resource
    public static LinkType createLinkType(final String linkTypeId)
    {
        return new LinkType(constructURIForLinkType(linkTypeId));
    }
    
    public static URI constructURIForLinkType(final String linkTypeId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("linkTypeId", linkTypeId);
        String instanceURI = "service2/linkTypes/{linkTypeId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForLinkType(final String linkTypeId , final String label)
    {
        return new Link(constructURIForLinkType(linkTypeId), label);
    }
    
    public static Link constructLinkForLinkType(final String linkTypeId)
    {
        return new Link(constructURIForLinkType(linkTypeId));
    }
    
    

    //methods for Resource resource
    public static Resource createResource(final String resourceId)
    {
        return new Resource(constructURIForResource(resourceId));
    }
    
    public static URI constructURIForResource(final String resourceId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("resourceId", resourceId);
        String instanceURI = "service1/resources/{resourceId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForResource(final String resourceId , final String label)
    {
        return new Link(constructURIForResource(resourceId), label);
    }
    
    public static Link constructLinkForResource(final String resourceId)
    {
        return new Link(constructURIForResource(resourceId));
    }
    
    

}
