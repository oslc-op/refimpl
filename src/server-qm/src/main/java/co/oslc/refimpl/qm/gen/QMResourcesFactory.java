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

package co.oslc.refimpl.qm.gen;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
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

public class QMResourcesFactory {

    // Start of user code class_attributes
    // End of user code
    
    // Start of user code class_methods
    // End of user code

    //methods for TestCase resource
    public static TestCase createTestCase(final String serviceProviderId, final String testCaseId)
           throws URISyntaxException
    {
        return new TestCase(constructURIForTestCase(serviceProviderId, testCaseId));
    }
    
    public static URI constructURIForTestCase(final String serviceProviderId, final String testCaseId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("testCaseId", testCaseId);
        String instanceURI = "serviceProviders/{serviceProviderId}/service1/testCases/{testCaseId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForTestCase(final String serviceProviderId, final String testCaseId , final String label)
    {
        return new Link(constructURIForTestCase(serviceProviderId, testCaseId), label);
    }
    
    public static Link constructLinkForTestCase(final String serviceProviderId, final String testCaseId)
    {
        return new Link(constructURIForTestCase(serviceProviderId, testCaseId));
    }
    

    //methods for TestExecutionRecord resource
    public static TestExecutionRecord createTestExecutionRecord(final String serviceProviderId, final String testExecutionRecordId)
           throws URISyntaxException
    {
        return new TestExecutionRecord(constructURIForTestExecutionRecord(serviceProviderId, testExecutionRecordId));
    }
    
    public static URI constructURIForTestExecutionRecord(final String serviceProviderId, final String testExecutionRecordId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("testExecutionRecordId", testExecutionRecordId);
        String instanceURI = "serviceProviders/{serviceProviderId}/service5/testExecutionRecords/{testExecutionRecordId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForTestExecutionRecord(final String serviceProviderId, final String testExecutionRecordId , final String label)
    {
        return new Link(constructURIForTestExecutionRecord(serviceProviderId, testExecutionRecordId), label);
    }
    
    public static Link constructLinkForTestExecutionRecord(final String serviceProviderId, final String testExecutionRecordId)
    {
        return new Link(constructURIForTestExecutionRecord(serviceProviderId, testExecutionRecordId));
    }
    

    //methods for TestPlan resource
    public static TestPlan createTestPlan(final String serviceProviderId, final String testPlanId)
           throws URISyntaxException
    {
        return new TestPlan(constructURIForTestPlan(serviceProviderId, testPlanId));
    }
    
    public static URI constructURIForTestPlan(final String serviceProviderId, final String testPlanId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("testPlanId", testPlanId);
        String instanceURI = "serviceProviders/{serviceProviderId}/service2/testPlans/{testPlanId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForTestPlan(final String serviceProviderId, final String testPlanId , final String label)
    {
        return new Link(constructURIForTestPlan(serviceProviderId, testPlanId), label);
    }
    
    public static Link constructLinkForTestPlan(final String serviceProviderId, final String testPlanId)
    {
        return new Link(constructURIForTestPlan(serviceProviderId, testPlanId));
    }
    

    //methods for TestResult resource
    public static TestResult createTestResult(final String serviceProviderId, final String testResultId)
           throws URISyntaxException
    {
        return new TestResult(constructURIForTestResult(serviceProviderId, testResultId));
    }
    
    public static URI constructURIForTestResult(final String serviceProviderId, final String testResultId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("testResultId", testResultId);
        String instanceURI = "serviceProviders/{serviceProviderId}/service4/testResults/{testResultId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForTestResult(final String serviceProviderId, final String testResultId , final String label)
    {
        return new Link(constructURIForTestResult(serviceProviderId, testResultId), label);
    }
    
    public static Link constructLinkForTestResult(final String serviceProviderId, final String testResultId)
    {
        return new Link(constructURIForTestResult(serviceProviderId, testResultId));
    }
    

    //methods for TestScript resource
    public static TestScript createTestScript(final String serviceProviderId, final String testScriptId)
           throws URISyntaxException
    {
        return new TestScript(constructURIForTestScript(serviceProviderId, testScriptId));
    }
    
    public static URI constructURIForTestScript(final String serviceProviderId, final String testScriptId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("testScriptId", testScriptId);
        String instanceURI = "serviceProviders/{serviceProviderId}/service3/testScripts/{testScriptId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForTestScript(final String serviceProviderId, final String testScriptId , final String label)
    {
        return new Link(constructURIForTestScript(serviceProviderId, testScriptId), label);
    }
    
    public static Link constructLinkForTestScript(final String serviceProviderId, final String testScriptId)
    {
        return new Link(constructURIForTestScript(serviceProviderId, testScriptId));
    }
    

}
