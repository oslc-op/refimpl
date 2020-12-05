import static io.restassured.RestAssured.*;
import static io.restassured.config.XmlConfig.xmlConfig;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import io.restassured.RestAssured;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;
import org.hamcrest.*;
import org.junit.Test;

import java.io.InputStream;

public class SpCatalogIT {

    private static final String URI_SPC = "http://localhost:8800/services/catalog/singleton";

    @Test
    public void testAuthChallenge() {
        given().
            when().get(URI_SPC).
            then().statusCode(401);
    }

    @Test
    public void testBasicAuthOK() {
        given().auth().basic("admin", "admin").
            when().get(URI_SPC).
            then().statusCode(200);
    }

    @Test
    public void testServesRdf() {
        given()
            .auth().basic("admin", "admin")
            .accept("application/rdf+xml")
        .when()
            .get(URI_SPC)
        .then()
            .statusCode(200)
            .contentType("application/rdf+xml");
    }

    @Test
    public void testContent() {
        final InputStream inputStream = given()
                .auth().basic("admin", "admin")
                .accept("application/rdf+xml")
            .when()
                .get(URI_SPC)
                .asInputStream();

        Model m = ModelFactory.createDefaultModel();
        RDFDataMgr.read(m, inputStream, Lang.RDFXML);

        final ServiceProviderCatalog catalog = JenaModelHelper.unmarshalSingle(m, ServiceProviderCatalog.class);

        assertEquals(URI_SPC, catalog.getAbout().toString());
    }


    @Test
    public void testServesRootServices() {
        given()
            .auth().basic("admin", "admin")
            .accept("application/rdf+xml")
            .config(RestAssured.config().xmlConfig(xmlConfig()
                .declareNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")
                .declareNamespace("oslc_rm", "http://open-services.net/xmlns/rm/1.0/")
            ))
        .when()
            .get("http://localhost:8800/services/rootservices")
        .then()
            .statusCode(200)
            .contentType("application/rdf+xml")
//            .log().all()
            .body("rdf:Description.oslc_rm:rmServiceProviders.@rdf:resource", equalTo(URI_SPC));
//            .body(pointsToCatalog(""));
    }

//    private Matcher<?> pointsToCatalog(String s) {
//        return new RootServicesCatalogMatcher(s);
//    }
//
//    public class RootServicesCatalogMatcher extends BaseMatcher {
//
//        private final String spcUri;
//
//        public RootServicesCatalogMatcher(String spcUri) {
//            this.spcUri = spcUri;
//        }
//
//        @Override
//        public boolean matches(Object item) {
//            return false;
//        }
//
//        @Override
//        public void describeTo(Description description) {
//
//        }
//    }
}
