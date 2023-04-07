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

public class ResourcesFactory {

    private String basePath;

    // Start of user code class_attributes
    // End of user code

    public ResourcesFactory(String basePath) {
        this.basePath = basePath;
    }

    // Start of user code class_methods
    // End of user code

    //methods for LinkType resource
    
    public LinkType createLinkType(final String id) {
        return new LinkType(constructURIForLinkType(id));
    }
    
    public URI constructURIForLinkType(final String id) {
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "lt/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(this.basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public Link constructLinkForLinkType(final String id , final String label) {
        return new Link(constructURIForLinkType(id), label);
    }
    
    public Link constructLinkForLinkType(final String id) {
        return new Link(constructURIForLinkType(id));
    }
    

    //methods for Resource resource
    
    public Resource createResource(final String id) {
        return new Resource(constructURIForResource(id));
    }
    
    public URI constructURIForResource(final String id) {
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "resource/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(this.basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public Link constructLinkForResource(final String id , final String label) {
        return new Link(constructURIForResource(id), label);
    }
    
    public Link constructLinkForResource(final String id) {
        return new Link(constructURIForResource(id));
    }
    

}
