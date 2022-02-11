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

package co.oslc.refimpl.qm.gen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContextEvent;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import co.oslc.refimpl.qm.gen.servlet.ServiceProviderCatalogSingleton;
import co.oslc.refimpl.qm.gen.ServiceProviderInfo;
import org.eclipse.lyo.oslc.domains.Agent;
import org.eclipse.lyo.oslc.domains.cm.ChangeRequest;
import org.eclipse.lyo.oslc.domains.config.ChangeSet;
import org.eclipse.lyo.oslc.domains.cm.Defect;
import org.eclipse.lyo.oslc4j.core.model.Discussion;
import org.eclipse.lyo.oslc.domains.Person;
import org.eclipse.lyo.oslc.domains.cm.Priority;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc.domains.rm.RequirementCollection;
import org.eclipse.lyo.oslc.domains.cm.State;
import org.eclipse.lyo.oslc.domains.qm.TestCase;
import org.eclipse.lyo.oslc.domains.qm.TestExecutionRecord;
import org.eclipse.lyo.oslc.domains.qm.TestPlan;
import org.eclipse.lyo.oslc.domains.qm.TestResult;
import org.eclipse.lyo.oslc.domains.qm.TestScript;



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

public class QMManager {

    private static final Logger log = LoggerFactory.getLogger(QMManager.class);


    // Start of user code class_attributes
    public static final String SP_DEFAULT = "sp_single";
    public static final int SELECTOR_LIMIT = 30;

    private static final ResourceRepository<TestCase> testCaseRepository = new MemResourceRepository<>();
    private static final ResourceRepository<TestExecutionRecord> testExecutionRecordRepository = new MemResourceRepository<>();
    private static final ResourceRepository<TestPlan> testPlanRepository = new MemResourceRepository<>();
    private static final ResourceRepository<TestResult> testResultRepository = new MemResourceRepository<>();
    private static final ResourceRepository<TestScript> testScriptRepository = new MemResourceRepository<>();
    // End of user code


    // Start of user code class_methods
    // End of user code

    public static void contextInitializeServletListener(final ServletContextEvent servletContextEvent)
    {

        // Start of user code contextInitializeServletListener
        OSLC4JUtils.setLyoStorePagingPreciseLimit(false);
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
        ServiceProviderInfo providerInfo = new ServiceProviderInfo();
        providerInfo.name = SP_DEFAULT;
        providerInfo.serviceProviderId = "Default SP";
        serviceProviderInfos = new ServiceProviderInfo[] {providerInfo};
        // End of user code
        return serviceProviderInfos;
    }

    public static List<TestCase> queryTestCases(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<TestCase> resources = null;


        // Start of user code queryTestCases
        resources = testCaseRepository.fetchResourcePageForSP(SP_DEFAULT, page, limit);
        // End of user code
        return resources;
    }
    public static List<TestCase> TestCaseSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<TestCase> resources = null;


        // Start of user code TestCaseSelector
        resources = testCaseRepository.findResources(SP_DEFAULT, terms, SELECTOR_LIMIT);
        // End of user code
        return resources;
    }
    public static TestCase createTestCase(HttpServletRequest httpServletRequest, final TestCase aResource)
    {
        TestCase newResource = null;


        // Start of user code createTestCase
        String id = aResource.getIdentifier();
        if(id == null) {
            id = UUID.randomUUID().toString();
            aResource.setIdentifier(id);
        }
        URI uri = QMResourcesFactory.constructURIForTestCase(SP_DEFAULT, id);
        aResource.setAbout(uri);
        aResource.setCreated(new Date());
        testCaseRepository.addResource(SP_DEFAULT, id, aResource);
        newResource = aResource;
        // End of user code
        return newResource;
    }



    public static List<TestPlan> queryTestPlans(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<TestPlan> resources = null;


        // Start of user code queryTestPlans
        resources = testPlanRepository.fetchResourcePageForSP(SP_DEFAULT, page, limit);
        // End of user code
        return resources;
    }
    public static List<TestPlan> TestPlanSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<TestPlan> resources = null;


        // Start of user code TestPlanSelector
        resources = testPlanRepository.findResources(SP_DEFAULT, terms, SELECTOR_LIMIT);
        // End of user code
        return resources;
    }
    public static TestPlan createTestPlan(HttpServletRequest httpServletRequest, final TestPlan aResource)
    {
        TestPlan newResource = null;


        // Start of user code createTestPlan
        String id = aResource.getIdentifier();
        if(id == null) {
            id = UUID.randomUUID().toString();
            aResource.setIdentifier(id);
        }
        URI uri = QMResourcesFactory.constructURIForTestPlan(SP_DEFAULT, id);
        aResource.setAbout(uri);
        aResource.setCreated(new Date());
        testPlanRepository.addResource(SP_DEFAULT, id, aResource);
        newResource = aResource;
        // End of user code
        return newResource;
    }



    public static List<TestScript> queryTestScripts(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<TestScript> resources = null;


        // Start of user code queryTestScripts
        resources = testScriptRepository.fetchResourcePageForSP(SP_DEFAULT, page, limit);
        // End of user code
        return resources;
    }
    public static List<TestScript> TestScriptSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<TestScript> resources = null;


        // Start of user code TestScriptSelector
        resources = testScriptRepository.findResources(SP_DEFAULT, terms, SELECTOR_LIMIT);
        // End of user code
        return resources;
    }
    public static TestScript createTestScript(HttpServletRequest httpServletRequest, final TestScript aResource)
    {
        TestScript newResource = null;


        // Start of user code createTestScript
        String id = aResource.getIdentifier();
        if(id == null) {
            id = UUID.randomUUID().toString();
            aResource.setIdentifier(id);
        }
        URI uri = QMResourcesFactory.constructURIForTestScript(SP_DEFAULT, id);
        aResource.setAbout(uri);
        aResource.setCreated(new Date());
        testScriptRepository.addResource(SP_DEFAULT, id, aResource);
        newResource = aResource;
        // End of user code
        return newResource;
    }



    public static List<TestResult> queryTestResults(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<TestResult> resources = null;


        // Start of user code queryTestResults
        resources = testResultRepository.fetchResourcePageForSP(SP_DEFAULT, page, limit);
        // End of user code
        return resources;
    }
    public static List<TestResult> TestResultSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<TestResult> resources = null;


        // Start of user code TestResultSelector
        resources = testResultRepository.findResources(SP_DEFAULT, terms, SELECTOR_LIMIT);
        // End of user code
        return resources;
    }
    public static TestResult createTestResult(HttpServletRequest httpServletRequest, final TestResult aResource)
    {
        TestResult newResource = null;


        // Start of user code createTestResult
        String id = aResource.getIdentifier();
        if(id == null) {
            id = UUID.randomUUID().toString();
            aResource.setIdentifier(id);
        }
        URI uri = QMResourcesFactory.constructURIForTestResult(SP_DEFAULT, id);
        aResource.setAbout(uri);
        aResource.setCreated(new Date());
        testResultRepository.addResource(SP_DEFAULT, id, aResource);
        newResource = aResource;
        // End of user code
        return newResource;
    }



    public static List<TestExecutionRecord> queryTestExecutionRecords(HttpServletRequest httpServletRequest, String where, String prefix, boolean paging, int page, int limit)
    {
        List<TestExecutionRecord> resources = null;


        // Start of user code queryTestExecutionRecords
        resources = testExecutionRecordRepository.fetchResourcePageForSP(SP_DEFAULT, page, limit);
        // End of user code
        return resources;
    }
    public static List<TestExecutionRecord> TestExecutionRecordSelector(HttpServletRequest httpServletRequest, String terms)
    {
        List<TestExecutionRecord> resources = null;


        // Start of user code TestExecutionRecordSelector
        resources = testExecutionRecordRepository.findResources(SP_DEFAULT, terms, SELECTOR_LIMIT);
        // End of user code
        return resources;
    }
    public static TestExecutionRecord createTestExecutionRecord(HttpServletRequest httpServletRequest, final TestExecutionRecord aResource)
    {
        TestExecutionRecord newResource = null;


        // Start of user code createTestExecutionRecord
        String id = aResource.getIdentifier();
        if(id == null) {
            id = UUID.randomUUID().toString();
            aResource.setIdentifier(id);
        }
        URI uri = QMResourcesFactory.constructURIForTestExecutionRecord(SP_DEFAULT, id);
        aResource.setAbout(uri);
        aResource.setCreated(new Date());
        testExecutionRecordRepository.addResource(SP_DEFAULT, id, aResource);
        newResource = aResource;
        // End of user code
        return newResource;
    }




    public static TestCase getTestCase(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestCase aResource = null;


        // Start of user code getTestCase
        if(testCaseRepository.hasResource(spSlug, id)) {
            aResource = testCaseRepository.getResource(spSlug, id);
        }
        // End of user code
        return aResource;
    }

    public static Boolean deleteTestCase(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        Boolean deleted = false;

        // Start of user code deleteTestCase
        testCaseRepository.deleteResource(spSlug, id);
        deleted = true;
        // End of user code
        return deleted;
    }

    public static TestCase updateTestCase(HttpServletRequest httpServletRequest, final TestCase aResource, final String spSlug, final String id) {
        TestCase updatedResource = null;

        // Start of user code updateTestCase
        if(!QMResourcesFactory.constructURIForTestCase(spSlug, id).equals(aResource.getAbout())) {
            throw new WebApplicationException("Subject URI shall match the endpoint", Response.Status.BAD_REQUEST);
        }
        aResource.setModified(new Date());
        testCaseRepository.updateResource(SP_DEFAULT, id, aResource);
        updatedResource = aResource;
        // End of user code
        return updatedResource;
    }
    public static TestPlan getTestPlan(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestPlan aResource = null;


        // Start of user code getTestPlan
        if(testPlanRepository.hasResource(spSlug, id)) {
            aResource = testPlanRepository.getResource(spSlug, id);
        }
        // End of user code
        return aResource;
    }

    public static Boolean deleteTestPlan(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        Boolean deleted = false;

        // Start of user code deleteTestPlan
        testPlanRepository.deleteResource(spSlug, id);
        deleted = true;
        // End of user code
        return deleted;
    }

    public static TestPlan updateTestPlan(HttpServletRequest httpServletRequest, final TestPlan aResource, final String spSlug, final String id) {
        TestPlan updatedResource = null;

        // Start of user code updateTestPlan
        if(!QMResourcesFactory.constructURIForTestPlan(spSlug, id).equals(aResource.getAbout())) {
            throw new WebApplicationException("Subject URI shall match the endpoint", Response.Status.BAD_REQUEST);
        }
        aResource.setModified(new Date());
        testPlanRepository.updateResource(spSlug, id, aResource);
        updatedResource = aResource;
        // End of user code
        return updatedResource;
    }
    public static TestScript getTestScript(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestScript aResource = null;


        // Start of user code getTestScript
        if(testScriptRepository.hasResource(spSlug, id)) {
            aResource = testScriptRepository.getResource(spSlug, id);
        }
        // End of user code
        return aResource;
    }

    public static Boolean deleteTestScript(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        Boolean deleted = false;

        // Start of user code deleteTestScript
        testScriptRepository.deleteResource(spSlug, id);
        deleted = true;
        // End of user code
        return deleted;
    }

    public static TestScript updateTestScript(HttpServletRequest httpServletRequest, final TestScript aResource, final String spSlug, final String id) {
        TestScript updatedResource = null;

        // Start of user code updateTestScript
        if(!QMResourcesFactory.constructURIForTestScript(spSlug, id).equals(aResource.getAbout())) {
            throw new WebApplicationException("Subject URI shall match the endpoint", Response.Status.BAD_REQUEST);
        }
        aResource.setModified(new Date());
        testScriptRepository.updateResource(SP_DEFAULT, id, aResource);
        updatedResource = aResource;
        // End of user code
        return updatedResource;
    }
    public static TestResult getTestResult(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestResult aResource = null;


        // Start of user code getTestResult
        if(testResultRepository.hasResource(spSlug, id)) {
            aResource = testResultRepository.getResource(spSlug, id);
        }
        // End of user code
        return aResource;
    }

    public static Boolean deleteTestResult(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        Boolean deleted = false;

        // Start of user code deleteTestResult
        testResultRepository.deleteResource(spSlug, id);
        deleted = true;
        // End of user code
        return deleted;
    }

    public static TestResult updateTestResult(HttpServletRequest httpServletRequest, final TestResult aResource, final String spSlug, final String id) {
        TestResult updatedResource = null;

        // Start of user code updateTestResult
        if(!QMResourcesFactory.constructURIForTestResult(spSlug, id).equals(aResource.getAbout())) {
            throw new WebApplicationException("Subject URI shall match the endpoint", Response.Status.BAD_REQUEST);
        }
        aResource.setModified(new Date());
        testResultRepository.updateResource(SP_DEFAULT, id, aResource);
        updatedResource = aResource;
        // End of user code
        return updatedResource;
    }
    public static TestExecutionRecord getTestExecutionRecord(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestExecutionRecord aResource = null;


        // Start of user code getTestExecutionRecord
        aResource = testExecutionRecordRepository.getResource(spSlug, id);
        // End of user code
        return aResource;
    }

    public static Boolean deleteTestExecutionRecord(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        Boolean deleted = false;

        // Start of user code deleteTestExecutionRecord
        testExecutionRecordRepository.deleteResource(spSlug, id);
        deleted = true;
        // End of user code
        return deleted;
    }

    public static TestExecutionRecord updateTestExecutionRecord(HttpServletRequest httpServletRequest, final TestExecutionRecord aResource, final String spSlug, final String id) {
        TestExecutionRecord updatedResource = null;

        // Start of user code updateTestExecutionRecord
        if(!QMResourcesFactory.constructURIForTestExecutionRecord(spSlug, id).equals(aResource.getAbout())) {
            throw new WebApplicationException("Subject URI shall match the endpoint", Response.Status.BAD_REQUEST);
        }
        aResource.setModified(new Date());
        testExecutionRecordRepository.updateResource(SP_DEFAULT, id, aResource);
        updatedResource = aResource;
        // End of user code
        return updatedResource;
    }

    public static String getETagFromTestCase(final TestCase aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestCase
        eTag = testCaseRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public static String getETagFromTestExecutionRecord(final TestExecutionRecord aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestExecutionRecord
        eTag = testExecutionRecordRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public static String getETagFromTestPlan(final TestPlan aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestPlan
        eTag = testPlanRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public static String getETagFromTestResult(final TestResult aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestResult
        eTag = testResultRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }
    public static String getETagFromTestScript(final TestScript aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestScript
        eTag = testScriptRepository.calculateETag(aResource);
        // End of user code
        return eTag;
    }

}
