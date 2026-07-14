package co.oslc.refimpl.am.gen;

import co.oslc.refimpl.am.gen.servlet.Application;
import co.oslc.refimpl.am.gen.servlet.ApplicationBinder;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import org.eclipse.lyo.core.trs.Base;
import org.eclipse.lyo.core.trs.TrackedResourceSet;
import org.eclipse.lyo.core.trs.ChangeLog;
import org.eclipse.lyo.core.trs.ChangeEvent;
import org.eclipse.lyo.core.trs.Creation;
import org.eclipse.lyo.core.trs.Modification;
import org.eclipse.lyo.core.trs.Deletion;
import org.eclipse.lyo.oslc.domains.am.Resource;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;

public class TrsGrizzlyTest extends JerseyTest {

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

        ResourceConfig rc = ResourceConfig.forApplicationClass(Application.class);
        return rc;
    }

    @Test
    public void testTrsDeserializationToPOJOs() {
        // 1. Fetch TrackedResourceSet and verify it deserializes into POJO
        jakarta.ws.rs.core.Response response = target("/trs").request("text/turtle").get();
        assertEquals(200, response.getStatus());
        
        TrackedResourceSet trs = response.readEntity(TrackedResourceSet.class);
        assertNotNull(trs);
        assertNotNull(trs.getBase());
        
        // 2. Fetch Base and verify it deserializes into POJO
        jakarta.ws.rs.core.Response baseResponse = target("/trs/base").request("text/turtle").get();
        assertEquals(200, baseResponse.getStatus());
        
        Base base = baseResponse.readEntity(Base.class);
        assertNotNull(base);
    }

    @Test
    public void testRestAssuredCRUDUpdatesChangeLog() {
        RestAssured.baseURI = getBaseUri().toString();

        // 1. Create a Resource via REST-Assured
        String resourcePayload = 
            "@prefix dcterms: <http://purl.org/dc/terms/> .\n" +
            "@prefix oslc: <http://open-services.net/ns/core#> .\n" +
            "@prefix oslc_am: <http://open-services.net/ns/am#> .\n" +
            "\n" +
            "[] a oslc_am:Resource ;\n" +
            "   dcterms:title \"RestAssured Resource\" ;\n" +
            "   dcterms:identifier \"ra-res-1\" .";

        io.restassured.response.Response postResponse = given()
            .contentType("text/turtle")
            .body(resourcePayload)
            .post("/service1/resources/create");

        assertEquals(201, postResponse.getStatusCode());
        String location = postResponse.getHeader("Location");
        assertNotNull(location);

        // 2. Read the TRS via JAX-RS client and verify the Creation event exists
        TrackedResourceSet trs1 = target("/trs").request("text/turtle").get(TrackedResourceSet.class);
        assertNotNull(trs1);
        ChangeLog changeLog1 = trs1.getChangeLog();
        assertNotNull(changeLog1);
        assertTrue(changeLog1.getChange().size() >= 1);
        ChangeEvent event1 = changeLog1.getChange().get(changeLog1.getChange().size() - 1);
        assertTrue(event1 instanceof Creation, "Event should be Creation");
        assertEquals(location, event1.getChanged().toString());

        // 3. Update the Resource via REST-Assured
        String updatedPayload = 
            "@prefix dcterms: <http://purl.org/dc/terms/> .\n" +
            "@prefix oslc_am: <http://open-services.net/ns/am#> .\n" +
            "\n" +
            "<" + location + "> a oslc_am:Resource ;\n" +
            "   dcterms:title \"RestAssured Resource Modified\" ;\n" +
            "   dcterms:identifier \"ra-res-1\" .";

        io.restassured.response.Response putResponse = given()
            .contentType("text/turtle")
            .body(updatedPayload)
            .put(location);

        assertEquals(200, putResponse.getStatusCode());

        // 4. Read the TRS and verify the Modification event exists
        TrackedResourceSet trs2 = target("/trs").request("text/turtle").get(TrackedResourceSet.class);
        ChangeLog changeLog2 = trs2.getChangeLog();
        assertTrue(changeLog2.getChange().size() >= 2);
        ChangeEvent event2 = changeLog2.getChange().get(changeLog2.getChange().size() - 1);
        assertTrue(event2 instanceof Modification, "Event should be Modification");
        assertEquals(location, event2.getChanged().toString());

        // 5. Delete the Resource via REST-Assured
        io.restassured.response.Response deleteResponse = given()
            .delete(location);
        assertEquals(200, deleteResponse.getStatusCode());

        // 6. Read the TRS and verify the Deletion event exists
        TrackedResourceSet trs3 = target("/trs").request("text/turtle").get(TrackedResourceSet.class);
        ChangeLog changeLog3 = trs3.getChangeLog();
        assertTrue(changeLog3.getChange().size() >= 3);
        ChangeEvent event3 = changeLog3.getChange().get(changeLog3.getChange().size() - 1);
        assertTrue(event3 instanceof Deletion, "Event should be Deletion");
        assertEquals(location, event3.getChanged().toString());
    }
}
