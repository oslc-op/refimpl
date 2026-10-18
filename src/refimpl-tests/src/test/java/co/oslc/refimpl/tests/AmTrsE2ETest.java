package co.oslc.refimpl.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.List;
import org.apache.jena.sys.JenaSystem;
import org.eclipse.lyo.client.OslcClient;
import org.eclipse.lyo.core.trs.ChangeLog;
import org.eclipse.lyo.core.trs.ChangeEvent;
import org.eclipse.lyo.core.trs.Creation;
import org.eclipse.lyo.core.trs.TrackedResourceSet;
import org.eclipse.lyo.trs.client.util.TrackedResourceClient;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

public class AmTrsE2ETest extends OslcTest {

    static {
        JenaSystem.init();
    }

    @Test
    public void testTrsClientSeesCreationEvent() throws Exception {
        var serviceHost = environment.getServiceHost(AM_SVC, AM_PORT);
        var servicePort = environment.getServicePort(AM_SVC, AM_PORT);
        
        String trsUrl = "http://%s:%d/services/trs".formatted(serviceHost, servicePort);
        String createUrl = "http://%s:%d/services/service1/resources/create".formatted(serviceHost, servicePort);

        // 1. Initialize Lyo TrackedResourceClient with Basic Auth
        OslcClient oslcClient = new OslcClient();
        oslcClient.getClient().register(HttpAuthenticationFeature.basic("admin", "admin"));
        TrackedResourceClient trsClient = new TrackedResourceClient(oslcClient);

        // 2. Fetch remote TRS and verify connection
        TrackedResourceSet trs = trsClient.extractRemoteTrs(URI.create(trsUrl));
        assertNotNull(trs);
        
        // 3. Post a new resource to AM using REST-Assured with Basic Auth
        String resourcePayload = 
            "@prefix dcterms: <http://purl.org/dc/terms/> .\n" +
            "@prefix oslc_am: <http://open-services.net/ns/am#> .\n" +
            "\n" +
            "[] a oslc_am:Resource ;\n" +
            "   dcterms:title \"E2E Test Resource\" ;\n" +
            "   dcterms:identifier \"e2e-res-1\" .";

        io.restassured.response.Response postResponse = given()
            .auth().preemptive().basic("admin", "admin")
            .contentType("text/turtle")
            .body(resourcePayload)
            .post(createUrl);

        assertEquals(201, postResponse.getStatusCode());
        String location = postResponse.getHeader("Location");
        assertNotNull(location);

        // 4. Poll the change log via the TRS client to find the Creation event
        // Timeout: fail if not detected after 60 seconds (well under the 5m limit)
        long startTime = System.currentTimeMillis();
        long timeoutMs = 60000;
        boolean found = false;

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            TrackedResourceSet updatedTrs = trsClient.extractRemoteTrs(URI.create(trsUrl));
            if (updatedTrs.getChangeLog() != null) {
                ChangeLog changeLog = updatedTrs.getChangeLog();
                for (ChangeEvent event : changeLog.getChange()) {
                    if (event instanceof Creation && location.equals(event.getChanged().toString())) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                break;
            }
            Thread.sleep(1000); // Poll every 1 second
        }

        assertTrue(found, "TRS Client did not see the Creation event for " + location + " within " + (timeoutMs/1000) + "s");
    }
}
