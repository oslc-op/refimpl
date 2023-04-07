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

package co.oslc.refimpl.cm.gen;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc.domains.Agent;
import org.eclipse.lyo.oslc.domains.cm.ChangeNotice;
import org.eclipse.lyo.oslc.domains.cm.ChangeRequest;
import org.eclipse.lyo.oslc.domains.config.ChangeSet;
import org.eclipse.lyo.oslc.domains.RdfsClass;
import org.eclipse.lyo.oslc.domains.config.Component;
import org.eclipse.lyo.oslc.domains.config.ConceptResource;
import org.eclipse.lyo.oslc.domains.config.Configuration;
import org.eclipse.lyo.oslc.domains.config.Contribution;
import org.eclipse.lyo.oslc.domains.cm.Defect;
import org.eclipse.lyo.oslc4j.core.model.Discussion;
import org.eclipse.lyo.oslc.domains.cm.Enhancement;
import org.eclipse.lyo.oslc.domains.Person;
import org.eclipse.lyo.oslc.domains.cm.Priority;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc.domains.cm.ReviewTask;
import org.eclipse.lyo.oslc.domains.config.Selections;
import org.eclipse.lyo.oslc.domains.cm.State;
import org.eclipse.lyo.oslc.domains.cm.Task;
import org.eclipse.lyo.oslc.domains.config.VersionResource;

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

    //methods for ChangeNotice resource
    
    

    //methods for ChangeRequest resource
    
    public ChangeRequest createChangeRequest(final String id) {
        return new ChangeRequest(constructURIForChangeRequest(id));
    }
    
    public URI constructURIForChangeRequest(final String id) {
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "change_request/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(this.basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public Link constructLinkForChangeRequest(final String id , final String label) {
        return new Link(constructURIForChangeRequest(id), label);
    }
    
    public Link constructLinkForChangeRequest(final String id) {
        return new Link(constructURIForChangeRequest(id));
    }
    

    //methods for Defect resource
    
    

    //methods for Enhancement resource
    
    

    //methods for ReviewTask resource
    
    

    //methods for Task resource
    
    

}
