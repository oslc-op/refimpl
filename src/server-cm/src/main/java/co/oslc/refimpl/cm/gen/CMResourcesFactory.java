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
import org.eclipse.lyo.oslc.domains.cm.Defect;
import org.eclipse.lyo.oslc4j.core.model.Discussion;
import org.eclipse.lyo.oslc.domains.cm.Enhancement;
import org.eclipse.lyo.oslc.domains.Person;
import org.eclipse.lyo.oslc.domains.cm.Priority;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc.domains.cm.ReviewTask;
import org.eclipse.lyo.oslc.domains.cm.State;
import org.eclipse.lyo.oslc.domains.cm.Task;

// Start of user code imports
// End of user code

// Start of user code pre_class_code
// End of user code

public class CMResourcesFactory {

    // Start of user code class_attributes
    // End of user code
    
    // Start of user code class_methods
    // End of user code

    //methods for ChangeNotice resource
    
    public static ChangeNotice createChangeNotice(final String id)
    {
        return new ChangeNotice(constructURIForChangeNotice(id));
    }
    
    public static URI constructURIForChangeNotice(final String id)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "change_request/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForChangeNotice(final String id , final String label)
    {
        return new Link(constructURIForChangeNotice(id), label);
    }
    
    public static Link constructLinkForChangeNotice(final String id)
    {
        return new Link(constructURIForChangeNotice(id));
    }
    

    //methods for ChangeRequest resource
    
    public static ChangeRequest createChangeRequest(final String id)
    {
        return new ChangeRequest(constructURIForChangeRequest(id));
    }
    
    public static URI constructURIForChangeRequest(final String id)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "change_request/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForChangeRequest(final String id , final String label)
    {
        return new Link(constructURIForChangeRequest(id), label);
    }
    
    public static Link constructLinkForChangeRequest(final String id)
    {
        return new Link(constructURIForChangeRequest(id));
    }
    

    //methods for Defect resource
    
    public static Defect createDefect(final String id)
    {
        return new Defect(constructURIForDefect(id));
    }
    
    public static URI constructURIForDefect(final String id)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "change_request/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForDefect(final String id , final String label)
    {
        return new Link(constructURIForDefect(id), label);
    }
    
    public static Link constructLinkForDefect(final String id)
    {
        return new Link(constructURIForDefect(id));
    }
    

    //methods for Enhancement resource
    
    public static Enhancement createEnhancement(final String id)
    {
        return new Enhancement(constructURIForEnhancement(id));
    }
    
    public static URI constructURIForEnhancement(final String id)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "change_request/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForEnhancement(final String id , final String label)
    {
        return new Link(constructURIForEnhancement(id), label);
    }
    
    public static Link constructLinkForEnhancement(final String id)
    {
        return new Link(constructURIForEnhancement(id));
    }
    

    //methods for ReviewTask resource
    
    public static ReviewTask createReviewTask(final String id)
    {
        return new ReviewTask(constructURIForReviewTask(id));
    }
    
    public static URI constructURIForReviewTask(final String id)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "change_request/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForReviewTask(final String id , final String label)
    {
        return new Link(constructURIForReviewTask(id), label);
    }
    
    public static Link constructLinkForReviewTask(final String id)
    {
        return new Link(constructURIForReviewTask(id));
    }
    

    //methods for Task resource
    
    public static Task createTask(final String id)
    {
        return new Task(constructURIForTask(id));
    }
    
    public static URI constructURIForTask(final String id)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("id", id);
        String instanceURI = "change_request/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForTask(final String id , final String label)
    {
        return new Link(constructURIForTask(id), label);
    }
    
    public static Link constructLinkForTask(final String id)
    {
        return new Link(constructURIForTask(id));
    }
    

}
