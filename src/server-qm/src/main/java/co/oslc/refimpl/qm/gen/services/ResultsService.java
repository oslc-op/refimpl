// Start of user code Copyright
/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
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
 *     Michael Fiedler     - initial API and implementation for Bugzilla adapter
 *     Jad El-khoury       - initial implementation of code generator (https://bugs.eclipse.org/bugs/show_bug.cgi?id=422448)
 *     Jim Amsden          - Support for UI Preview (494303)
 *
 * This file is generated by org.eclipse.lyo.oslc4j.codegenerator
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.qm.gen.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jakarta.inject.Inject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.UriBuilder;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONArray;
import org.eclipse.lyo.oslc4j.provider.json4j.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.lyo.oslc4j.core.OSLC4JConstants;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.annotation.OslcCreationFactory;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialog;
import org.eclipse.lyo.oslc4j.core.annotation.OslcDialogs;
import org.eclipse.lyo.oslc4j.core.annotation.OslcQueryCapability;
import org.eclipse.lyo.oslc4j.core.annotation.OslcService;
import org.eclipse.lyo.oslc4j.core.model.Compact;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.Preview;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;

import co.oslc.refimpl.qm.gen.RestDelegate;
import co.oslc.refimpl.qm.gen.ServerConstants;
import org.eclipse.lyo.oslc.domains.qm.Oslc_qmDomainConstants;
import org.eclipse.lyo.oslc.domains.qm.Oslc_qmDomainConstants;
import co.oslc.refimpl.qm.gen.servlet.ServiceProviderCatalogSingleton;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

// Start of user code imports
// End of user code

// Start of user code pre_class_code
// End of user code
@OslcService(Oslc_qmDomainConstants.QUALITY_MANAGEMENT_NAMSPACE)
@Path("service4/testResults")
public class ResultsService
{
    @Context private HttpServletRequest httpServletRequest;
    @Context private HttpServletResponse httpServletResponse;
    @Context private UriInfo uriInfo;
    @Inject  private RestDelegate delegate;

    private static final Logger log = LoggerFactory.getLogger(ResultsService.class);

    // Start of user code class_attributes
    // End of user code

    // Start of user code class_methods
    // End of user code

    public ResultsService()
    {
        super();
    }

    private void addCORSHeaders (final HttpServletResponse httpServletResponse) {
        //UI preview can be blocked by CORS policy.
        //add select CORS headers to every response that is embedded in an iframe.
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, HEAD");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
    }

    @OslcQueryCapability
    (
        title = "TestResultQC",
        label = "Test Result Query Capability",
        resourceShape = OslcConstants.PATH_RESOURCE_SHAPES + "/" + Oslc_qmDomainConstants.TESTRESULT_PATH,
        resourceTypes = {Oslc_qmDomainConstants.TESTRESULT_TYPE},
        usages = {}
    )
    @GET
    @Path("query")
    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON_LD, OslcMediaType.TEXT_TURTLE, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
    @Operation(
        summary = "Query capability for resources of type {" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "}",
        description = "Query capability for resources of type {" + "<a href=\"" + Oslc_qmDomainConstants.TESTRESULT_TYPE + "\">" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "</a>" + "}" +
            ", with respective resource shapes {" + "<a href=\"" + "../services/" + OslcConstants.PATH_RESOURCE_SHAPES + "/" + Oslc_qmDomainConstants.TESTRESULT_PATH + "\">" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "</a>" + "}",
        responses = { 
            @ApiResponse(description = "default response", content = {@Content(mediaType = OslcMediaType.APPLICATION_RDF_XML), @Content(mediaType = OslcMediaType.APPLICATION_XML), @Content(mediaType = OslcMediaType.APPLICATION_JSON), @Content(mediaType = OslcMediaType.TEXT_TURTLE), @Content(mediaType = MediaType.TEXT_HTML)})
        }
    )
    public TestResult[] queryTestResults(
                                                    
                                                     @QueryParam("oslc.where") final String where,
                                                     @QueryParam("oslc.prefix") final String prefix,
                                                     @QueryParam("oslc.paging") final String pagingString,
                                                     @QueryParam("page") final String pageString,
                                                     @QueryParam("oslc.pageSize") final String pageSizeString) throws IOException, ServletException
    {
        boolean paging=false;
        int page=0;
        int pageSize=20;
        if (null != pagingString) {
            paging = Boolean.parseBoolean(pagingString);
        }
        if (null != pageString) {
            page = Integer.parseInt(pageString);
        }
        if (null != pageSizeString) {
            pageSize = Integer.parseInt(pageSizeString);
        }

        // Start of user code queryTestResults
        // Here additional logic can be implemented that complements main action taken in RestDelegate
        // End of user code

        List<TestResult> resources = delegate.queryTestResults(httpServletRequest, where, prefix, paging, page, pageSize);
        UriBuilder uriBuilder = UriBuilder.fromUri(uriInfo.getAbsolutePath())
            .queryParam("oslc.paging", "true")
            .queryParam("oslc.pageSize", pageSize)
            .queryParam("page", page);
        if (null != where) {
            uriBuilder.queryParam("oslc.where", where);
        }
        if (null != prefix) {
            uriBuilder.queryParam("oslc.prefix", prefix);
        }
        httpServletRequest.setAttribute("queryUri", uriBuilder.build().toString());
        if ((OSLC4JUtils.hasLyoStorePagingPreciseLimit() && resources.size() >= pageSize) 
            || (!OSLC4JUtils.hasLyoStorePagingPreciseLimit() && resources.size() > pageSize)) {
            resources = resources.subList(0, pageSize);
            uriBuilder.replaceQueryParam("page", page + 1);
            httpServletRequest.setAttribute(OSLC4JConstants.OSLC4J_NEXT_PAGE, uriBuilder.build().toString());
        }
        return resources.toArray(new TestResult [resources.size()]);
    }

    @GET
    @Path("query")
    @Produces({ MediaType.TEXT_HTML })
    @Operation(
        summary = "Query capability for resources of type {" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "}",
        description = "Query capability for resources of type {" + "<a href=\"" + Oslc_qmDomainConstants.TESTRESULT_TYPE + "\">" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "</a>" + "}" +
            ", with respective resource shapes {" + "<a href=\"" + "../services/" + OslcConstants.PATH_RESOURCE_SHAPES + "/" + Oslc_qmDomainConstants.TESTRESULT_PATH + "\">" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "</a>" + "}",
        responses = { 
            @ApiResponse(description = "default response", content = {@Content(mediaType = OslcMediaType.APPLICATION_RDF_XML), @Content(mediaType = OslcMediaType.APPLICATION_XML), @Content(mediaType = OslcMediaType.APPLICATION_JSON), @Content(mediaType = OslcMediaType.TEXT_TURTLE), @Content(mediaType = MediaType.TEXT_HTML)})
        }
    )
    public void queryTestResultsAsHtml(
                                    
                                       @QueryParam("oslc.where") final String where,
                                       @QueryParam("oslc.prefix") final String prefix,
                                       @QueryParam("oslc.paging") final String pagingString,
                                       @QueryParam("page") final String pageString,
                                       @QueryParam("oslc.pageSize") final String pageSizeString) throws ServletException, IOException
    {
        boolean paging=false;
        int page=0;
        int pageSize=20;
        if (null != pagingString) {
            paging = Boolean.parseBoolean(pagingString);
        }
        if (null != pageString) {
            page = Integer.parseInt(pageString);
        }
        if (null != pageSizeString) {
            pageSize = Integer.parseInt(pageSizeString);
        }

        // Start of user code queryTestResultsAsHtml
        // End of user code

        List<TestResult> resources = delegate.queryTestResults(httpServletRequest, where, prefix, paging, page, pageSize);

        if (resources!= null) {
            // Start of user code queryTestResultsAsHtml_setAttributes
            // End of user code

            UriBuilder uriBuilder = UriBuilder.fromUri(uriInfo.getAbsolutePath())
                .queryParam("oslc.paging", "true")
                .queryParam("oslc.pageSize", pageSize)
                .queryParam("page", page);
            if (null != where) {
                uriBuilder.queryParam("oslc.where", where);
            }
            if (null != prefix) {
                uriBuilder.queryParam("oslc.prefix", prefix);
            }
            httpServletRequest.setAttribute("queryUri", uriBuilder.build().toString());

        if ((OSLC4JUtils.hasLyoStorePagingPreciseLimit() && resources.size() >= pageSize) 
            || (!OSLC4JUtils.hasLyoStorePagingPreciseLimit() && resources.size() > pageSize)) {
                resources = resources.subList(0, pageSize);
                uriBuilder.replaceQueryParam("page", page + 1);
                httpServletRequest.setAttribute(OSLC4JConstants.OSLC4J_NEXT_PAGE, uriBuilder.build().toString());
            }
            httpServletRequest.setAttribute("resources", resources);
            RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/co/oslc/refimpl/qm/gen/testresultscollection.jsp");
            rd.forward(httpServletRequest,httpServletResponse);
            return;
        }
        throw new WebApplicationException(Status.NOT_FOUND);
    }

    @OslcDialog
    (
         title = "TestResultSD",
         label = "Test Result Selection Dialog",
         uri = "service4/testResults/selector",
         hintWidth = "0px",
         hintHeight = "0px",
         resourceTypes = {Oslc_qmDomainConstants.TESTRESULT_TYPE},
         usages = {}
    )
    @GET
    @Path("selector")
    @Consumes({ MediaType.TEXT_HTML, MediaType.WILDCARD })
    public Response TestResultSelector(
        @QueryParam("terms") final String terms
        
        ) throws ServletException, IOException, JSONException
    {
        // Start of user code TestResultSelector_init
            // End of user code

        httpServletRequest.setAttribute("selectionUri",UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path(uriInfo.getPath()).build().toString());
        // Start of user code TestResultSelector_setAttributes
            // End of user code

        if (terms != null ) {
            httpServletRequest.setAttribute("terms", terms);
            final List<TestResult> resources = delegate.TestResultSelector(httpServletRequest, terms);
            if (resources!= null) {
                JSONArray resourceArray = new JSONArray();
                for (TestResult resource : resources) {
                    JSONObject r = new JSONObject();
                    r.put("oslc:label", resource.toString());
                    r.put("rdf:resource", resource.getAbout().toString());
                    r.put("Label", resource.toString());
                    // Start of user code TestResultSelector_setResponse
                    //TODO: Add any other attributes that are to be displayed in the search result
                    // End of user code
                    resourceArray.add(r);
                }
                JSONObject response = new JSONObject();
                response.put("oslc:results", resourceArray);
                return Response.ok(response.write()).build();
            }
            log.error("A empty search should return an empty list and not NULL!");
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);

        } else {
            RequestDispatcher rd = httpServletRequest.getRequestDispatcher("/co/oslc/refimpl/qm/gen/testresultselector.jsp");
            rd.forward(httpServletRequest, httpServletResponse);
            return null;
        }
    }

    /**
     * Create a single TestResult via RDF/XML, XML or JSON POST
     *
     * @throws IOException
     * @throws ServletException
     */
    @OslcCreationFactory
    (
         title = "TestResultCF",
         label = "Test Result Creation Factory",
         resourceShapes = {OslcConstants.PATH_RESOURCE_SHAPES + "/" + Oslc_qmDomainConstants.TESTRESULT_PATH},
         resourceTypes = {Oslc_qmDomainConstants.TESTRESULT_TYPE},
         usages = {}
    )
    @POST
    @Path("create")
    @Consumes({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON_LD, OslcMediaType.TEXT_TURTLE, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON })
    @Produces({OslcMediaType.APPLICATION_RDF_XML, OslcMediaType.APPLICATION_JSON_LD, OslcMediaType.TEXT_TURTLE, OslcMediaType.APPLICATION_XML, OslcMediaType.APPLICATION_JSON})
    @Operation(
        summary = "Creation factory for resources of type {" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "}",
        description = "Creation factory for resources of type {" + "<a href=\"" + Oslc_qmDomainConstants.TESTRESULT_TYPE + "\">" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "</a>" + "}" +
            ", with respective resource shapes {" + "<a href=\"" + "../services/" + OslcConstants.PATH_RESOURCE_SHAPES + "/" + Oslc_qmDomainConstants.TESTRESULT_PATH + "\">" + Oslc_qmDomainConstants.TESTRESULT_LOCALNAME + "</a>" + "}",
        responses = { 
            @ApiResponse(description = "default response", content = {@Content(mediaType = OslcMediaType.APPLICATION_RDF_XML), @Content(mediaType = OslcMediaType.APPLICATION_XML), @Content(mediaType = OslcMediaType.APPLICATION_JSON), @Content(mediaType = OslcMediaType.TEXT_TURTLE)})
        }
    )
    public Response createTestResult(
            
            final TestResult aResource
        ) throws IOException, ServletException
    {
        TestResult newResource = delegate.createTestResult(httpServletRequest, aResource);
        httpServletResponse.setHeader("ETag", delegate.getETagFromTestResult(newResource));
        return Response.created(newResource.getAbout()).entity(newResource).header(ServerConstants.HDR_OSLC_VERSION, ServerConstants.OSLC_VERSION_V2).build();
    }

}
