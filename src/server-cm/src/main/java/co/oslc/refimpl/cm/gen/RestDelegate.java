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


import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContextEvent;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import co.oslc.refimpl.cm.gen.servlet.ServiceProviderCatalogSingleton;
import co.oslc.refimpl.cm.gen.ServiceProviderInfo;
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
import java.net.URI;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import co.oslc.refimpl.lib.MemResourceRepository;
import co.oslc.refimpl.lib.ResourceRepository;
// End of user code

// Start of user code pre_class_code
// End of user code

public class RestDelegate {

    private static final Logger log = LoggerFactory.getLogger(RestDelegate.class);

    
    
    @Inject ResourcesFactory resourcesFactory;
    // Start of user code class_attributes
    public static final String SP_DEFAULT = "sp_single";
    private static final Logger LOG = LoggerFactory.getLogger(RestDelegate.class);

    private static final ResourceRepository<ChangeRequest> changeRequestRepository = new MemResourceRepository<>();
    // End of user code
    
    public RestDelegate() {
        log.trace("Delegate is initialized");
    }
    
    
    // Start of user code class_methods
    // End of user code

    //The methods contextInitializeServletListener() and contextDestroyServletListener() no longer exits
    //Migrate any user-specific code blocks to the class co.oslc.refimpl.cm.gen.servlet.ServletListener
    //Any user-specific code should be found in *.lost files.

    public static ServiceProviderInfo[] getServiceProviderInfos(HttpServletRequest httpServletRequest)
    {
        ServiceProviderInfo[] serviceProviderInfos = {};
        
        // Start of user code "ServiceProviderInfo[] getServiceProviderInfos(...)"
        ServiceProviderInfo spInfo = new ServiceProviderInfo();
        spInfo.serviceProviderId = SP_DEFAULT;
        spInfo.name = "Default ServiceProvider";
        serviceProviderInfos = new ServiceProviderInfo[] {spInfo};
        // End of user code
        return serviceProviderInfos;
    }

    public List<ChangeRequest> queryChangeRequests(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<ChangeRequest> resources = null;
        
        
        // Start of user code queryChangeRequests
        resources = changeRequestRepository.fetchResourcePageForSP(SP_DEFAULT, page, limit);
        // End of user code
        return resources;
    }
    public List<Defect> queryDefects(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<Defect> resources = null;
        
        
        // Start of user code queryDefects
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        LOG.warn("QC for Defect resources not implemented");
        resources = new ArrayList<>();
        // End of user code
        return resources;
    }
    public List<Task> queryTasks(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<Task> resources = null;
        
        
        // Start of user code queryTasks
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        LOG.warn("QC for Task resources not implemented");
        resources = new ArrayList<>();
        // End of user code
        return resources;
    }
    public List<Enhancement> queryEnhancements(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<Enhancement> resources = null;
        
        
        // Start of user code queryEnhancements
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        LOG.warn("QC for Enhancement resources not implemented");
        resources = new ArrayList<>();
        // End of user code
        return resources;
    }
    public List<ReviewTask> queryReviewTasks(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<ReviewTask> resources = null;
        
        
        // Start of user code queryReviewTasks
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        LOG.warn("QC for Review resources not implemented");
        resources = new ArrayList<>();
        // End of user code
        return resources;
    }
    public List<ChangeNotice> queryChangeNotices(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<ChangeNotice> resources = null;
        
        
        // Start of user code queryChangeNotices
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        LOG.warn("QC for ChangeNotice resources not implemented");
        resources = new ArrayList<>();
        // End of user code
        return resources;
    }
    public List<ChangeRequest> ChangeRequestSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<ChangeRequest> resources = null;
        
        
        // Start of user code ChangeRequestSelector
//        resources = changeRequestRepository.fetchResourcePageForSP(SP_DEFAULT, 1, 20);
        resources = changeRequestRepository.findResources(SP_DEFAULT, terms, 20);
        // End of user code
        return resources;
    }
    public List<Defect> DefectSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<Defect> resources = null;
        
        
        // Start of user code DefectSelector
        // TODO Implement code to return a set of resources, based on search criteria
        // End of user code
        return resources;
    }
    public List<Task> TaskSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<Task> resources = null;
        
        
        // Start of user code TaskSelector
        // TODO Implement code to return a set of resources, based on search criteria
        // End of user code
        return resources;
    }
    public List<ReviewTask> ReviewTaskSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<ReviewTask> resources = null;
        
        
        // Start of user code ReviewTaskSelector
        // TODO Implement code to return a set of resources, based on search criteria
        // End of user code
        return resources;
    }
    public List<ChangeNotice> ChangeNoticeSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<ChangeNotice> resources = null;
        
        
        // Start of user code ChangeNoticeSelector
        // TODO Implement code to return a set of resources, based on search criteria
        // End of user code
        return resources;
    }
    public List<Enhancement> EnhancementSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<Enhancement> resources = null;
        
        
        // Start of user code EnhancementSelector
        // TODO Implement code to return a set of resources, based on search criteria
        // End of user code
        return resources;
    }
    public ChangeRequest createChangeRequest(HttpServletRequest httpServletRequest, final ChangeRequest aResource)
    {
        ChangeRequest newResource = null;
        
        
        // Start of user code createChangeRequest
        String id = aResource.getIdentifier();
        if(id == null) {
            id = UUID.randomUUID().toString();
            aResource.setIdentifier(id);
        }
        URI uri = resourcesFactory.constructURIForChangeRequest(id);
        aResource.setAbout(uri);
        aResource.setCreated(new Date());
        changeRequestRepository.addResource(SP_DEFAULT, id, aResource);
        newResource = aResource;
        // End of user code
        return newResource;
    }

    public Defect createDefectFromDialog(HttpServletRequest httpServletRequest, final Defect aResource)
    {
        Defect newResource = null;
        
        
        // Start of user code createDefectFromDialog
        // TODO Implement code to create a resource
        // End of user code
        return newResource;
    }
    public Task createTaskFromDialog(HttpServletRequest httpServletRequest, final Task aResource)
    {
        Task newResource = null;
        
        
        // Start of user code createTaskFromDialog
        // TODO Implement code to create a resource
        // End of user code
        return newResource;
    }
    public ReviewTask createReviewTaskFromDialog(HttpServletRequest httpServletRequest, final ReviewTask aResource)
    {
        ReviewTask newResource = null;
        
        
        // Start of user code createReviewTaskFromDialog
        // TODO Implement code to create a resource
        // End of user code
        return newResource;
    }
    public ChangeNotice createChangeNoticeFromDialog(HttpServletRequest httpServletRequest, final ChangeNotice aResource)
    {
        ChangeNotice newResource = null;
        
        
        // Start of user code createChangeNoticeFromDialog
        // TODO Implement code to create a resource
        // End of user code
        return newResource;
    }
    public Enhancement createEnhancementFromDialog(HttpServletRequest httpServletRequest, final Enhancement aResource)
    {
        Enhancement newResource = null;
        
        
        // Start of user code createEnhancementFromDialog
        // TODO Implement code to create a resource
        // End of user code
        return newResource;
    }



    public ChangeRequest getChangeRequest(HttpServletRequest httpServletRequest, final String id)
    {
        ChangeRequest aResource = null;
        
        
        // Start of user code getChangeRequest
        aResource = changeRequestRepository.getResource(SP_DEFAULT, id);
        // End of user code
        return aResource;
    }

    public Boolean deleteChangeRequest(HttpServletRequest httpServletRequest, final String id)
    {
        Boolean deleted = false;
        
        // Start of user code deleteChangeRequest
        changeRequestRepository.deleteResource(SP_DEFAULT, id);
        deleted = true;
        // End of user code
        return deleted;
    }

    public ChangeRequest updateChangeRequest(HttpServletRequest httpServletRequest, final ChangeRequest aResource, final String id) {
        ChangeRequest updatedResource = null;
        
        // Start of user code updateChangeRequest
        if(!resourcesFactory.constructURIForChangeRequest(id).equals(aResource.getAbout())) {
            throw new WebApplicationException("Subject URI shall match the endpoint", Response.Status.BAD_REQUEST);
        }
        aResource.setModified(new Date());
        changeRequestRepository.updateResource(SP_DEFAULT, id, aResource);
        updatedResource = aResource;
        // End of user code
        return updatedResource;
    }

    public String getETagFromChangeNotice(final ChangeNotice aResource)
    {
        String eTag = null;
        // Start of user code getETagFromChangeNotice
        eTag = changeRequestRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public String getETagFromChangeRequest(final ChangeRequest aResource)
    {
        String eTag = null;
        // Start of user code getETagFromChangeRequest
        eTag = changeRequestRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public String getETagFromDefect(final Defect aResource)
    {
        String eTag = null;
        // Start of user code getETagFromDefect
        eTag = changeRequestRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public String getETagFromEnhancement(final Enhancement aResource)
    {
        String eTag = null;
        // Start of user code getETagFromEnhancement
        eTag = changeRequestRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public String getETagFromReviewTask(final ReviewTask aResource)
    {
        String eTag = null;
        // Start of user code getETagFromReviewTask
        eTag = changeRequestRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public String getETagFromTask(final Task aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTask
        eTag = changeRequestRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }

}
