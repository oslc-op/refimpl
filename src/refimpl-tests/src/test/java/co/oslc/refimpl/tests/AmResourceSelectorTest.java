package co.oslc.refimpl.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Acceptance tests for the AM server's Resource Selection dialog (issue #499).
 *
 * Verifies that:
 *  1. A resource POSTed to the creation factory is queryable via the selector.
 *  2. Searching by a matching term returns a non-empty result set.
 *  3. Searching by a non-matching term returns an empty result set.
 */
@Testcontainers
public class AmResourceSelectorTest {

    static final String AM_SVC = "server-am";
    static final int    AM_PORT = 8080;

    /** Base path for the AM service-1 resources endpoints. */
    static final String RESOURCES_BASE = "/services/service1/resources";

    @Container
    public static ComposeContainer environment =
            new ComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService(AM_SVC, AM_PORT,
                            Wait.forLogMessage(".*main: Started oejs.Server@.*", 1)
                                    .withStartupTimeout(Duration.ofSeconds(120)))
                    .withLocalCompose(true);

    /** Unique identifier embedded in the resource created during this test run. */
    static String uniqueId;

    @BeforeAll
    static void setUp() {
        String host = environment.getServiceHost(AM_SVC, AM_PORT);
        int    port = environment.getServicePort(AM_SVC, AM_PORT);
        RestAssured.baseURI = "http://" + host;
        RestAssured.port    = port;
        RestAssured.authentication = RestAssured.preemptive().basic("admin", "admin");

        // Use a unique identifier so the test is isolated across runs.
        uniqueId = "selector-test-" + UUID.randomUUID().toString().substring(0, 8);

        // POST a new resource to the AM creation factory.
        // The payload is a minimal Turtle representation of an AM Resource.
        String turtle = """
                @prefix oslc_am: <http://open-services.net/ns/am#> .
                @prefix dcterms: <http://purl.org/dc/terms/> .
                @prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
                <> a oslc_am:Resource ;
                   dcterms:identifier "%s" ;
                   dcterms:title      "Selector test resource %s" .
                """.formatted(uniqueId, uniqueId);

        given()
            .contentType("text/turtle")
            .accept("text/turtle")
            .body(turtle)
        .when()
            .post(RESOURCES_BASE + "/create")
        .then()
            .statusCode(201);
    }

    /**
     * Querying the selector with {@code terms} equal to the unique identifier must
     * return a JSON object whose {@code oslc:results} array contains at least one
     * entry whose {@code rdf:resource} is non-empty (the previously created resource).
     */
    @Test
    void selectorReturnsResourceWhenTermMatches() {
        given()
            .accept(ContentType.JSON)
            .queryParam("terms", uniqueId)
        .when()
            .get(RESOURCES_BASE + "/selector")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("'oslc:results'", not(empty()))
            .body("'oslc:results'[0].'rdf:resource'", not(emptyOrNullString()));
    }

    /**
     * Querying the selector with {@code terms} that cannot match any stored resource
     * must return a JSON object with an empty {@code oslc:results} array (not an
     * error).
     */
    @Test
    void selectorReturnsEmptyResultsForNonMatchingTerm() {
        String noMatchTerm = "zzz-no-match-" + UUID.randomUUID();

        given()
            .accept(ContentType.JSON)
            .queryParam("terms", noMatchTerm)
        .when()
            .get(RESOURCES_BASE + "/selector")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("'oslc:results'", empty());
    }

    /**
     * Querying with {@code terms=""} (empty string) must also return the created
     * resource (the selector shows all resources when no filter is applied).
     */
    @Test
    void selectorReturnsAllResourcesWhenTermsIsEmpty() {
        Response response = given()
            .accept(ContentType.JSON)
            .queryParam("terms", "")
        .when()
            .get(RESOURCES_BASE + "/selector")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("'oslc:results'", not(empty()))
            .extract().response();

        // The unique resource we created must appear in the full list.
        String body = response.asString();
        assertTrue(body.contains(uniqueId),
                "Selector should include the previously created resource when terms is empty");
    }
}
