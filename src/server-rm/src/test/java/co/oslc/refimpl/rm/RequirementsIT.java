package co.oslc.refimpl.rm;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.sys.JenaSystem;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static co.oslc.misc.OslcMatchers.isRdf;
import static io.restassured.RestAssured.given;
public class RequirementsIT {
    private static final Logger log = LoggerFactory.getLogger(RequirementsIT.class);


    static {
        // to remove init from the perf timing
        JenaSystem.init();
    }

    @Test
    public void testSvcProvHasServices() {
        Response response = givenOslcRequest()
                .auth().basic("admin", "admin")
            .when()
                .get("http://localhost:8800/services/serviceProviders/sp_single");

        response.then()
            .assertThat()
            .statusCode(200)
            .contentType(isRdf())
            .body(hasCreationFactory(response.getContentType()));
    }

    private RequestSpecification givenOslcRequest() {
        return given()
            .accept("text/turtle;q=1.0, application/rdf+xml;q=0.9, application/ld+json;q=0.8, application/n-triples;q=0.5")
            .header("OSLC-Core-Version", "3.0");
    }


    private Matcher<ValidatableResponse> hasCreationFactory(String contentType) {
        return new BaseMatcher<>() {
            @Override
            public boolean matches(Object o) {
                long t1 = System.nanoTime();
                Model model = parseJenaModel((String) o, contentType);
                long t2 = System.nanoTime();
                ServiceProvider sp = JenaModelHelper.unmarshalSingle(model, ServiceProvider.class);
                long t3 = System.nanoTime();
                log.trace("Model parsing took {}ms, JMH took {}ms", TimeUnit.NANOSECONDS.toMillis(t2-t1),
                    TimeUnit.NANOSECONDS.toMillis(t3-t2));
                return Arrays.stream(sp.getServices()).anyMatch(service -> service.getCreationFactories().length > 0);
            }

            @Override
            public void describeTo(Description description) {
                // TODO: add
            }
        };
    }

    // TODO: refactor/extract
    private static Model parseJenaModel(String value, String contentType) {
        Model model = ModelFactory.createDefaultModel();
        // TODO: consider passing the GET URI as a base
        RDFDataMgr.read(model, new StringReader(value), null, RDFLanguages.contentTypeToLang(contentType));
        return model;
    }
}
