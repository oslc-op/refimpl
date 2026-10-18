package co.oslc.refimpl.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.lyo.oslc.domains.am.Oslc_amDomainConstants;
import org.eclipse.lyo.oslc4j.core.model.OslcConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Acceptance tests for the AM server's Query Capability paging.
 *
 * Verifies that:
 *  1. Querying with paging returns the expected page size.
 *  2. The 1st page includes an oslc:nextPage link pointing to page 1.
 *  3. The 2nd page renders distinct QC results from the 1st page (not duplicated).
 */
public class AmResourcePagingTest extends OslcTest {

    static final String RESOURCES_BASE = "/services/service1/resources";

    @BeforeAll
    static void setUp() {
        String host = environment.getServiceHost(AM_SVC, AM_PORT);
        int    port = environment.getServicePort(AM_SVC, AM_PORT);
        RestAssured.baseURI = "http://" + host;
        RestAssured.port    = port;
        RestAssured.authentication = RestAssured.preemptive().basic("admin", "admin");
    }

    @Test
    void queryCapabilityReturnsDistinctPagesForResources() {
        String runId = UUID.randomUUID().toString().substring(0, 8);
        for (int i = 0; i < 5; i++) {
            String resId = "paging-" + runId + "-" + i;
            String turtle = """
                    @prefix oslc_am: <http://open-services.net/ns/am#> .
                    @prefix dcterms: <http://purl.org/dc/terms/> .

                    <> a oslc_am:Resource ;
                       dcterms:identifier "%s" ;
                       dcterms:title      "Paging test resource %s" .
                    """.formatted(resId, resId);

            given()
                .contentType("text/turtle")
                .accept("text/turtle")
                .body(turtle)
            .when()
                .post(RESOURCES_BASE + "/create")
            .then()
                .statusCode(201);
        }

        Response page1 = given()
            .accept("text/turtle")
            .queryParam("oslc.paging", "true")
            .queryParam("oslc.pageSize", 2)
        .when()
            .get(RESOURCES_BASE + "/query");

        assertEquals(200, page1.getStatusCode());
        List<String> page1Uris = extractUrisOfType(page1.getBody().asString(), Oslc_amDomainConstants.RESOURCE_TYPE);
        assertEquals(2, page1Uris.size());

        String nextPageUri = extractNextPageUri(page1.getBody().asString());
        assertNotNull(nextPageUri, "1st page response must contain oslc:nextPage");
        assertTrue(nextPageUri.contains("page=1"), "nextPage URI should reference page=1");

        Response page2 = given()
            .accept("text/turtle")
            .queryParam("oslc.paging", "true")
            .queryParam("oslc.pageSize", 2)
            .queryParam("page", 1)
        .when()
            .get(RESOURCES_BASE + "/query");

        assertEquals(200, page2.getStatusCode());
        List<String> page2Uris = extractUrisOfType(page2.getBody().asString(), Oslc_amDomainConstants.RESOURCE_TYPE);
        assertEquals(2, page2Uris.size());

        // 2nd page must NOT render the same QC results as 1st page
        assertNotEquals(page1Uris, page2Uris, "2nd page must not render the same QC results as 1st page");
        for (String uri : page1Uris) {
            assertFalse(page2Uris.contains(uri), "Page 2 should not contain resource from page 1: " + uri);
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
