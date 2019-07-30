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
    public static ChangeNotice createChangeNotice(final String serviceProviderId, final String changeNoticeId)
           throws URISyntaxException
    {
        return new ChangeNotice(constructURIForChangeNotice(serviceProviderId, changeNoticeId));
    }
    
    public static URI constructURIForChangeNotice(final String serviceProviderId, final String changeNoticeId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("changeNoticeId", changeNoticeId);
        String instanceURI = "serviceProviders/{serviceProviderId}/resources/changeNotices/{changeNoticeId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForChangeNotice(final String serviceProviderId, final String changeNoticeId , final String label)
    {
        return new Link(constructURIForChangeNotice(serviceProviderId, changeNoticeId), label);
    }
    
    public static Link constructLinkForChangeNotice(final String serviceProviderId, final String changeNoticeId)
    {
        return new Link(constructURIForChangeNotice(serviceProviderId, changeNoticeId));
    }
    

    //methods for ChangeRequest resource
    public static ChangeRequest createChangeRequest(final String serviceProviderId, final String changeRequestId)
           throws URISyntaxException
    {
        return new ChangeRequest(constructURIForChangeRequest(serviceProviderId, changeRequestId));
    }
    
    public static URI constructURIForChangeRequest(final String serviceProviderId, final String changeRequestId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("changeRequestId", changeRequestId);
        String instanceURI = "serviceProviders/{serviceProviderId}/resources/changeRequests/{changeRequestId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForChangeRequest(final String serviceProviderId, final String changeRequestId , final String label)
    {
        return new Link(constructURIForChangeRequest(serviceProviderId, changeRequestId), label);
    }
    
    public static Link constructLinkForChangeRequest(final String serviceProviderId, final String changeRequestId)
    {
        return new Link(constructURIForChangeRequest(serviceProviderId, changeRequestId));
    }
    

    //methods for Defect resource
    public static Defect createDefect(final String serviceProviderId, final String defectId)
           throws URISyntaxException
    {
        return new Defect(constructURIForDefect(serviceProviderId, defectId));
    }
    
    public static URI constructURIForDefect(final String serviceProviderId, final String defectId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("defectId", defectId);
        String instanceURI = "serviceProviders/{serviceProviderId}/resources/defects/{defectId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForDefect(final String serviceProviderId, final String defectId , final String label)
    {
        return new Link(constructURIForDefect(serviceProviderId, defectId), label);
    }
    
    public static Link constructLinkForDefect(final String serviceProviderId, final String defectId)
    {
        return new Link(constructURIForDefect(serviceProviderId, defectId));
    }
    

    //methods for Enhancement resource
    public static Enhancement createEnhancement(final String serviceProviderId, final String enhancementId)
           throws URISyntaxException
    {
        return new Enhancement(constructURIForEnhancement(serviceProviderId, enhancementId));
    }
    
    public static URI constructURIForEnhancement(final String serviceProviderId, final String enhancementId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("enhancementId", enhancementId);
        String instanceURI = "serviceProviders/{serviceProviderId}/resources/enhancements/{enhancementId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForEnhancement(final String serviceProviderId, final String enhancementId , final String label)
    {
        return new Link(constructURIForEnhancement(serviceProviderId, enhancementId), label);
    }
    
    public static Link constructLinkForEnhancement(final String serviceProviderId, final String enhancementId)
    {
        return new Link(constructURIForEnhancement(serviceProviderId, enhancementId));
    }
    

    //methods for ReviewTask resource
    public static ReviewTask createReviewTask(final String serviceProviderId, final String reviewTaskId)
           throws URISyntaxException
    {
        return new ReviewTask(constructURIForReviewTask(serviceProviderId, reviewTaskId));
    }
    
    public static URI constructURIForReviewTask(final String serviceProviderId, final String reviewTaskId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("reviewTaskId", reviewTaskId);
        String instanceURI = "serviceProviders/{serviceProviderId}/resources/reviewTasks/{reviewTaskId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForReviewTask(final String serviceProviderId, final String reviewTaskId , final String label)
    {
        return new Link(constructURIForReviewTask(serviceProviderId, reviewTaskId), label);
    }
    
    public static Link constructLinkForReviewTask(final String serviceProviderId, final String reviewTaskId)
    {
        return new Link(constructURIForReviewTask(serviceProviderId, reviewTaskId));
    }
    

    //methods for Task resource
    public static Task createTask(final String serviceProviderId, final String taskId)
           throws URISyntaxException
    {
        return new Task(constructURIForTask(serviceProviderId, taskId));
    }
    
    public static URI constructURIForTask(final String serviceProviderId, final String taskId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("taskId", taskId);
        String instanceURI = "serviceProviders/{serviceProviderId}/resources/tasks/{taskId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForTask(final String serviceProviderId, final String taskId , final String label)
    {
        return new Link(constructURIForTask(serviceProviderId, taskId), label);
    }
    
    public static Link constructLinkForTask(final String serviceProviderId, final String taskId)
    {
        return new Link(constructURIForTask(serviceProviderId, taskId));
    }
    

}
