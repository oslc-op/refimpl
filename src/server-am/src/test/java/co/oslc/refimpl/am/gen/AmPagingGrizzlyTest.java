package co.oslc.refimpl.am.gen;

import co.oslc.refimpl.am.gen.servlet.Application;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.lyo.oslc.domains.am.Oslc_amDomainConstants;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class AmPagingGrizzlyTest extends JerseyTest {

    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    protected DeploymentContext configureDeployment() {
        return ServletDeploymentContext.builder(configure())
                .servlet(new ServletContainer(configure()))
                .build();
    }

    @Override
    protected void configureClient(final ClientConfig config) {
        JenaProvidersRegistry.getProviders().forEach(config::register);
    }

    @Override
    protected ResourceConfig configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        try {
            OSLC4JUtils.setPublicURI(getBaseUri().toString());
        } catch (MalformedURLException e) {
            System.err.println("Can't set the OSLC4J public URI");
        }
        OSLC4JUtils.setServletPath("/");

        return ResourceConfig.forApplicationClass(Application.class);
    }

    @BeforeEach
    public void setUpRestAssured() {
        RestAssured.baseURI = getBaseUri().toString();
    }

    @Test
    public void testResourcesQueryPagingRendersDistinctPages() {
        // Create 5 resources with distinct IDs
        String runId = UUID.randomUUID().toString().substring(0, 8);
        for (int i = 0; i < 5; i++) {
            String resId = "res-" + runId + "-" + i;
            String payload = """
                    @prefix dcterms: <http://purl.org/dc/terms/> .
                    @prefix oslc_am: <http://open-services.net/ns/am#> .

                    [] a oslc_am:Resource ;
                       dcterms:title "Paging Resource %s" ;
                       dcterms:identifier "%s" .
                    """.formatted(resId, resId);

            given()
                .contentType("text/turtle")
                .body(payload)
                .post("/service1/resources/create")
                .then()
                .statusCode(201);
        }

        // Query 1st page (page=0 or default) with pageSize=2
        Response page1Response = given()
                .accept("text/turtle")
                .queryParam("oslc.paging", "true")
                .queryParam("oslc.pageSize", 2)
                .get("/service1/resources/query");

        assertEquals(200, page1Response.getStatusCode());
        List<String> page1Uris = extractUrisOfType(page1Response.getBody().asString(), Oslc_amDomainConstants.RESOURCE_TYPE);
        assertEquals(2, page1Uris.size(), "1st page should return exactly pageSize items");

        // Verify oslc:nextPage is present and points to page=1
        String nextPageUri = extractNextPageUri(page1Response.getBody().asString());
        assertNotNull(nextPageUri, "1st page response must contain oslc:nextPage");
        assertTrue(nextPageUri.contains("page=1"), "nextPage URI should reference page=1");

        // Query 2nd page by following the nextPage URI
        Response page2Response = given()
                .accept("text/turtle")
                .get(nextPageUri);

        assertEquals(200, page2Response.getStatusCode());
        List<String> page2Uris = extractUrisOfType(page2Response.getBody().asString(), Oslc_amDomainConstants.RESOURCE_TYPE);
        assertEquals(2, page2Uris.size(), "2nd page should return exactly pageSize items");

        // CRITICAL CHECK: 2nd page must NOT render the same QC results as 1st page
        assertNotEquals(page1Uris, page2Uris, "2nd page must not render the same QC results as 1st page");
        for (String uri : page1Uris) {
            assertFalse(page2Uris.contains(uri), "Page 2 should not contain resource from page 1: " + uri);
        }

        // Verify 2nd page also has oslc:nextPage pointing to page=2
        String nextPage2Uri = extractNextPageUri(page2Response.getBody().asString());
        assertNotNull(nextPage2Uri, "2nd page response must contain oslc:nextPage");
        assertTrue(nextPage2Uri.contains("page=2"), "nextPage URI should reference page=2");

        // Query 3rd page by following nextPage2Uri
        Response page3Response = given()
                .accept("text/turtle")
                .get(nextPage2Uri);

        assertEquals(200, page3Response.getStatusCode());
        List<String> page3Uris = extractUrisOfType(page3Response.getBody().asString(), Oslc_amDomainConstants.RESOURCE_TYPE);
        assertFalse(page3Uris.isEmpty(), "3rd page should contain remaining resources");
        for (String uri : page3Uris) {
            assertFalse(page1Uris.contains(uri), "Page 3 should not contain resource from page 1: " + uri);
            assertFalse(page2Uris.contains(uri), "Page 3 should not contain resource from page 2: " + uri);
        }

        // On the last page, there should be no oslc:nextPage
        String lastNextPage = extractNextPageUri(page3Response.getBody().asString());
        assertNull(lastNextPage, "Last page should not contain oslc:nextPage");
    }

    @Test
    public void testLinkTypesQueryPagingRendersDistinctPages() {
        // Create 4 link types with distinct IDs
        String runId = UUID.randomUUID().toString().substring(0, 8);
        for (int i = 0; i < 4; i++) {
            String ltId = "lt-" + runId + "-" + i;
            String payload = """
                    @prefix dcterms: <http://purl.org/dc/terms/> .
                    @prefix oslc_am: <http://open-services.net/ns/am#> .

                    [] a oslc_am:LinkType ;
                       dcterms:title "Paging LinkType %s" ;
                       dcterms:identifier "%s" .
                    """.formatted(ltId, ltId);

            given()
                .contentType("text/turtle")
                .body(payload)
                .post("/service2/linkTypes/create")
                .then()
                .statusCode(201);
        }

        // Query page 0
        Response page1Response = given()
                .accept("text/turtle")
                .queryParam("oslc.paging", "true")
                .queryParam("oslc.pageSize", 2)
                .get("/service2/linkTypes/query");

        assertEquals(200, page1Response.getStatusCode());
        List<String> page1Uris = extractUrisOfType(page1Response.getBody().asString(), Oslc_amDomainConstants.LINKTYPE_TYPE);
        assertEquals(2, page1Uris.size());

        String nextPageUri = extractNextPageUri(page1Response.getBody().asString());
        assertNotNull(nextPageUri, "1st page response must contain oslc:nextPage");

        // Query page 1 by following nextPage
        Response page2Response = given()
                .accept("text/turtle")
                .get(nextPageUri);

        assertEquals(200, page2Response.getStatusCode());
        List<String> page2Uris = extractUrisOfType(page2Response.getBody().asString(), Oslc_amDomainConstants.LINKTYPE_TYPE);
        assertEquals(2, page2Uris.size());

        // Page 1 and Page 2 must not be equal and must be disjoint
        assertNotEquals(page1Uris, page2Uris, "2nd page must not render the same LinkType results as 1st page");
        for (String uri : page1Uris) {
            assertFalse(page2Uris.contains(uri), "Page 2 should not contain LinkType from page 1: " + uri);
        }
    }

    private static List<String> extractUrisOfType(String turtleBody, String rdfTypeUri) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, new StringReader(turtleBody), null, RDFLanguages.TURTLE);
        org.apache.jena.rdf.model.Resource typeResource = model.createResource(rdfTypeUri);
        return model.listSubjectsWithProperty(RDF.type, typeResource)
                .filterKeep(org.apache.jena.rdf.model.Resource::isURIResource)
                .mapWith(org.apache.jena.rdf.model.Resource::getURI)
                .toList();
    }

    private static String extractNextPageUri(String turtleBody) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, new StringReader(turtleBody), null, RDFLanguages.TURTLE);
        Property nextPageProp = model.createProperty(OslcConstants.OSLC_CORE_NAMESPACE, "nextPage");
        List<RDFNode> nodes = model.listObjectsOfProperty(nextPageProp).toList();
        if (nodes.isEmpty()) {
            return null;
        }
        return nodes.get(0).asResource().getURI();
    }
}
