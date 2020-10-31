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
 *	   Sam Padgett	       - initial API and implementation
 *     Michael Fiedler     - adapted for OSLC4J
 *     Jad El-khoury        - initial implementation of code generator (https://bugs.eclipse.org/bugs/show_bug.cgi?id=422448)
 *     Matthieu Helleboid   - Support for multiple Service Providers.
 *     Anass Radouani       - Support for multiple Service Providers.
 *
 * This file is generated by org.eclipse.lyo.oslc4j.codegenerator
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.cm.gen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContextEvent;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import co.oslc.refimpl.cm.gen.servlet.ServiceProviderCatalogSingleton;
import co.oslc.refimpl.cm.gen.ServiceProviderInfo;
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

public class OSLCCMServer2020RefImplManager {

    private static final Logger log = LoggerFactory.getLogger(OSLCCMServer2020RefImplManager.class);

    
    // Start of user code class_attributes
    // End of user code
    
    
    // Start of user code class_methods
    // End of user code

    public static void contextInitializeServletListener(final ServletContextEvent servletContextEvent)
    {
        
        // Start of user code contextInitializeServletListener
        // TODO Implement code to establish connection to data backbone etc ...
        // End of user code
        
    }

    public static void contextDestroyServletListener(ServletContextEvent servletContextEvent) 
    {
        
        // Start of user code contextDestroyed
        // TODO Implement code to shutdown connections to data backbone etc...
        // End of user code
    }

    public static ServiceProviderInfo[] getServiceProviderInfos(HttpServletRequest httpServletRequest)
    {
        ServiceProviderInfo[] serviceProviderInfos = {};
        
        // Start of user code "ServiceProviderInfo[] getServiceProviderInfos(...)"
        // TODO Implement code to return the set of ServiceProviders
        // End of user code
        return serviceProviderInfos;
    }

    public static List<ChangeRequest> queryChangeRequests(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<ChangeRequest> resources = null;
        
        
        // Start of user code queryChangeRequests
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<Defect> queryDefects(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<Defect> resources = null;
        
        
        // Start of user code queryDefects
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<Task> queryTasks(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<Task> resources = null;
        
        
        // Start of user code queryTasks
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<Enhancement> queryEnhancements(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<Enhancement> resources = null;
        
        
        // Start of user code queryEnhancements
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<ReviewTask> queryReviewTasks(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<ReviewTask> resources = null;
        
        
        // Start of user code queryReviewTasks
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<ChangeNotice> queryChangeNotices(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<ChangeNotice> resources = null;
        
        
        // Start of user code queryChangeNotices
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<ChangeRequest> ChangeRequestSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<ChangeRequest> resources = null;
        
        
        // Start of user code ChangeRequestSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<Defect> DefectSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<Defect> resources = null;
        
        
        // Start of user code DefectSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<Task> TaskSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<Task> resources = null;
        
        
        // Start of user code TaskSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<ReviewTask> ReviewTaskSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<ReviewTask> resources = null;
        
        
        // Start of user code ReviewTaskSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<ChangeNotice> ChangeNoticeSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<ChangeNotice> resources = null;
        
        
        // Start of user code ChangeNoticeSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<Enhancement> EnhancementSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<Enhancement> resources = null;
        
        
        // Start of user code EnhancementSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static ChangeRequest createChangeRequest(HttpServletRequest httpServletRequest, final ChangeRequest aResource)
    {
        ChangeRequest newResource = null;
        
        
        // Start of user code createChangeRequest
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }

    public static Defect createDefectFromDialog(HttpServletRequest httpServletRequest, final Defect aResource)
    {
        Defect newResource = null;
        
        
        // Start of user code createDefectFromDialog
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }
    public static Task createTaskFromDialog(HttpServletRequest httpServletRequest, final Task aResource)
    {
        Task newResource = null;
        
        
        // Start of user code createTaskFromDialog
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }
    public static ReviewTask createReviewTaskFromDialog(HttpServletRequest httpServletRequest, final ReviewTask aResource)
    {
        ReviewTask newResource = null;
        
        
        // Start of user code createReviewTaskFromDialog
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }
    public static ChangeNotice createChangeNoticeFromDialog(HttpServletRequest httpServletRequest, final ChangeNotice aResource)
    {
        ChangeNotice newResource = null;
        
        
        // Start of user code createChangeNoticeFromDialog
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }
    public static Enhancement createEnhancementFromDialog(HttpServletRequest httpServletRequest, final Enhancement aResource)
    {
        Enhancement newResource = null;
        
        
        // Start of user code createEnhancementFromDialog
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }



    public static ChangeRequest getChangeRequest(HttpServletRequest httpServletRequest, final String id)
    {
        ChangeRequest aResource = null;
        
        
        // Start of user code getChangeRequest
        // TODO Implement code to return a resource
        // return 'null' if the resource was not found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return aResource;
    }

    public static Boolean deleteChangeRequest(HttpServletRequest httpServletRequest, final String id)
    {
        Boolean deleted = false;
        
        // Start of user code deleteChangeRequest
        // TODO Implement code to delete a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return deleted;
    }

    public static ChangeRequest updateChangeRequest(HttpServletRequest httpServletRequest, final ChangeRequest aResource, final String id) {
        ChangeRequest updatedResource = null;
        
        // Start of user code updateChangeRequest
        // TODO Implement code to update and return a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return updatedResource;
    }

    public static String getETagFromChangeNotice(final ChangeNotice aResource)
    {
        String eTag = null;
        // Start of user code getETagFromChangeNotice
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromChangeRequest(final ChangeRequest aResource)
    {
        String eTag = null;
        // Start of user code getETagFromChangeRequest
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromDefect(final Defect aResource)
    {
        String eTag = null;
        // Start of user code getETagFromDefect
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromEnhancement(final Enhancement aResource)
    {
        String eTag = null;
        // Start of user code getETagFromEnhancement
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromReviewTask(final ReviewTask aResource)
    {
        String eTag = null;
        // Start of user code getETagFromReviewTask
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromTask(final Task aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTask
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }

}
