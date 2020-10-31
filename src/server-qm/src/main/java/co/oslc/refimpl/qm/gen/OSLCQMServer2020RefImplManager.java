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
// End of user code

// Start of user code pre_class_code
// End of user code

public class OSLCQMServer2020RefImplManager {

    private static final Logger log = LoggerFactory.getLogger(OSLCQMServer2020RefImplManager.class);

    
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

    public static List<TestCase> queryTestCases(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<TestCase> resources = null;
        
        
        // Start of user code queryTestCases
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<TestCase> TestCaseSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<TestCase> resources = null;
        
        
        // Start of user code TestCaseSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static TestCase createTestCase(HttpServletRequest httpServletRequest, final TestCase aResource)
    {
        TestCase newResource = null;
        
        
        // Start of user code createTestCase
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }



    public static List<TestPlan> queryTestPlans(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<TestPlan> resources = null;
        
        
        // Start of user code queryTestPlans
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<TestPlan> TestPlanSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<TestPlan> resources = null;
        
        
        // Start of user code TestPlanSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static TestPlan createTestPlan(HttpServletRequest httpServletRequest, final TestPlan aResource)
    {
        TestPlan newResource = null;
        
        
        // Start of user code createTestPlan
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }



    public static List<TestScript> queryTestScripts(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<TestScript> resources = null;
        
        
        // Start of user code queryTestScripts
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<TestScript> TestScriptSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<TestScript> resources = null;
        
        
        // Start of user code TestScriptSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static TestScript createTestScript(HttpServletRequest httpServletRequest, final TestScript aResource)
    {
        TestScript newResource = null;
        
        
        // Start of user code createTestScript
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }



    public static List<TestResult> queryTestResults(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<TestResult> resources = null;
        
        
        // Start of user code queryTestResults
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<TestResult> TestResultSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<TestResult> resources = null;
        
        
        // Start of user code TestResultSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static TestResult createTestResult(HttpServletRequest httpServletRequest, final TestResult aResource)
    {
        TestResult newResource = null;
        
        
        // Start of user code createTestResult
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }



    public static List<TestExecutionRecord> queryTestExecutionRecords(HttpServletRequest httpServletRequest, String where, String prefix, int page, int limit)
    {
        List<TestExecutionRecord> resources = null;
        
        
        // Start of user code queryTestExecutionRecords
        // TODO Implement code to return a set of resources.
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static List<TestExecutionRecord> TestExecutionRecordSelector(HttpServletRequest httpServletRequest, String terms)   
    {
        List<TestExecutionRecord> resources = null;
        
        
        // Start of user code TestExecutionRecordSelector
        // TODO Implement code to return a set of resources, based on search criteria 
        // An empty List should imply that no resources where found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return resources;
    }
    public static TestExecutionRecord createTestExecutionRecord(HttpServletRequest httpServletRequest, final TestExecutionRecord aResource)
    {
        TestExecutionRecord newResource = null;
        
        
        // Start of user code createTestExecutionRecord
        // TODO Implement code to create a resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return newResource;
    }




    public static TestCase getTestCase(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestCase aResource = null;
        
        
        // Start of user code getTestCase
        // TODO Implement code to return a resource
        // return 'null' if the resource was not found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return aResource;
    }


    public static TestPlan getTestPlan(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestPlan aResource = null;
        
        
        // Start of user code getTestPlan
        // TODO Implement code to return a resource
        // return 'null' if the resource was not found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return aResource;
    }


    public static TestScript getTestScript(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestScript aResource = null;
        
        
        // Start of user code getTestScript
        // TODO Implement code to return a resource
        // return 'null' if the resource was not found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return aResource;
    }


    public static TestResult getTestResult(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestResult aResource = null;
        
        
        // Start of user code getTestResult
        // TODO Implement code to return a resource
        // return 'null' if the resource was not found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return aResource;
    }


    public static TestExecutionRecord getTestExecutionRecord(HttpServletRequest httpServletRequest, final String spSlug, final String id)
    {
        TestExecutionRecord aResource = null;
        
        
        // Start of user code getTestExecutionRecord
        // TODO Implement code to return a resource
        // return 'null' if the resource was not found.
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return aResource;
    }



    public static String getETagFromTestCase(final TestCase aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestCase
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromTestExecutionRecord(final TestExecutionRecord aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestExecutionRecord
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromTestPlan(final TestPlan aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestPlan
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromTestResult(final TestResult aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestResult
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }
    public static String getETagFromTestScript(final TestScript aResource)
    {
        String eTag = null;
        // Start of user code getETagFromTestScript
        // TODO Implement code to return an ETag for a particular resource
        // If you encounter problems, consider throwing the runtime exception WebApplicationException(message, cause, final httpStatus)
        // End of user code
        return eTag;
    }

}
