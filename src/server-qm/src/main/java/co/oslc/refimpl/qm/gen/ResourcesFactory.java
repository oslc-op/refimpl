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
import jakarta.ws.rs.core.UriBuilder;

import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc.domains.Agent;
import org.eclipse.lyo.oslc.domains.cm.ChangeRequest;
import org.eclipse.lyo.oslc.domains.config.ChangeSet;
import org.eclipse.lyo.oslc.domains.RdfsClass;
import org.eclipse.lyo.oslc.domains.config.Component;
import org.eclipse.lyo.oslc.domains.config.ConceptResource;
import org.eclipse.lyo.oslc.domains.config.Configuration;
import org.eclipse.lyo.oslc.domains.config.Contribution;
import org.eclipse.lyo.oslc.domains.cm.Defect;
import org.eclipse.lyo.oslc4j.core.model.Discussion;
import org.eclipse.lyo.oslc.domains.Person;
import org.eclipse.lyo.oslc.domains.cm.Priority;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc.domains.rm.RequirementCollection;
import org.eclipse.lyo.oslc.domains.config.Selections;
import org.eclipse.lyo.oslc.domains.cm.State;
import org.eclipse.lyo.oslc.domains.qm.TestCase;
import org.eclipse.lyo.oslc.domains.qm.TestExecutionRecord;
import org.eclipse.lyo.oslc.domains.qm.TestPlan;
import org.eclipse.lyo.oslc.domains.qm.TestResult;
import org.eclipse.lyo.oslc.domains.qm.TestScript;
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

    //methods for TestCase resource
    
    public TestCase createTestCase(final String spSlug, final String id) {
        return new TestCase(constructURIForTestCase(spSlug, id));
    }
    
    public URI constructURIForTestCase(final String spSlug, final String id) {
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("spSlug", spSlug);
        pathParameters.put("id", id);
        String instanceURI = "cases/{spSlug}.{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(this.basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public Link constructLinkForTestCase(final String spSlug, final String id , final String label) {
        return new Link(constructURIForTestCase(spSlug, id), label);
    }
    
    public Link constructLinkForTestCase(final String spSlug, final String id) {
        return new Link(constructURIForTestCase(spSlug, id));
    }
    

    //methods for TestExecutionRecord resource
    
    public TestExecutionRecord createTestExecutionRecord(final String spSlug, final String id) {
        return new TestExecutionRecord(constructURIForTestExecutionRecord(spSlug, id));
    }
    
    public URI constructURIForTestExecutionRecord(final String spSlug, final String id) {
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("spSlug", spSlug);
        pathParameters.put("id", id);
        String instanceURI = "exec-records/{spSlug}-{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(this.basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public Link constructLinkForTestExecutionRecord(final String spSlug, final String id , final String label) {
        return new Link(constructURIForTestExecutionRecord(spSlug, id), label);
    }
    
    public Link constructLinkForTestExecutionRecord(final String spSlug, final String id) {
        return new Link(constructURIForTestExecutionRecord(spSlug, id));
    }
    

    //methods for TestPlan resource
    
    public TestPlan createTestPlan(final String spSlug, final String id) {
        return new TestPlan(constructURIForTestPlan(spSlug, id));
    }
    
    public URI constructURIForTestPlan(final String spSlug, final String id) {
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("spSlug", spSlug);
        pathParameters.put("id", id);
        String instanceURI = "plans/{spSlug}/{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(this.basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public Link constructLinkForTestPlan(final String spSlug, final String id , final String label) {
        return new Link(constructURIForTestPlan(spSlug, id), label);
    }
    
    public Link constructLinkForTestPlan(final String spSlug, final String id) {
        return new Link(constructURIForTestPlan(spSlug, id));
    }
    

    //methods for TestResult resource
    
    public TestResult createTestResult(final String spSlug, final String id) {
        return new TestResult(constructURIForTestResult(spSlug, id));
    }
    
    public URI constructURIForTestResult(final String spSlug, final String id) {
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("spSlug", spSlug);
        pathParameters.put("id", id);
        String instanceURI = "results/{spSlug}-{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(this.basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public Link constructLinkForTestResult(final String spSlug, final String id , final String label) {
        return new Link(constructURIForTestResult(spSlug, id), label);
    }
    
    public Link constructLinkForTestResult(final String spSlug, final String id) {
        return new Link(constructURIForTestResult(spSlug, id));
    }
    

    //methods for TestScript resource
    
    public TestScript createTestScript(final String spSlug, final String id) {
        return new TestScript(constructURIForTestScript(spSlug, id));
    }
    
    public URI constructURIForTestScript(final String spSlug, final String id) {
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("spSlug", spSlug);
        pathParameters.put("id", id);
        String instanceURI = "scripts/{spSlug}_{id}";
    
        final UriBuilder builder = UriBuilder.fromUri(this.basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public Link constructLinkForTestScript(final String spSlug, final String id , final String label) {
        return new Link(constructURIForTestScript(spSlug, id), label);
    }
    
    public Link constructLinkForTestScript(final String spSlug, final String id) {
        return new Link(constructURIForTestScript(spSlug, id));
    }
    

}
