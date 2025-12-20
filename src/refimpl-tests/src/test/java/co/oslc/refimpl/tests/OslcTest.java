package co.oslc.refimpl.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class OslcTest {

    public static final int STARTUP_TIMEOUT = 120;
    public static final String RM_SVC = "server-rm";
    public static final String CM_SVC = "server-cm";
    public static final String QM_SVC = "server-qm";
    public static final String AM_SVC = "server-am";

    static final int RM_PORT = 8080;
    static final int CM_PORT = 8080;
    static final int QM_PORT = 8080;
    static final int AM_PORT = 8080;

    @Container
    public static ComposeContainer environment = new ComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService(RM_SVC, RM_PORT,
                    Wait.forLogMessage(".*(Started oejs.Server@|Quarkus.*started in).*", 1)
                            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT)))
            .withExposedService(CM_SVC, CM_PORT,
                    Wait.forLogMessage(".*main: Started oejs.Server@.*", 1)
                            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT)))
            .withExposedService(QM_SVC, QM_PORT,
                    Wait.forLogMessage(".*main: Started oejs.Server@.*", 1)
                            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT)))
            .withExposedService(AM_SVC, AM_PORT,
                    Wait.forLogMessage(".*main: Started oejs.Server@.*", 1)
                            .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT)))
            .withLocalCompose(true);

    @ParameterizedTest
    @CsvSource({
        RM_SVC + ", " + RM_PORT,
        CM_SVC + ", " + CM_PORT,
        QM_SVC + ", " + QM_PORT,
        AM_SVC + ", " + AM_PORT
    })
    void rootservicesDataTableTest(String svc, int port) {
        assertEquals(200, testRootServices(svc, port));
    }

    @Test
    void cmRootservicesTest() {
        var svc = CM_SVC;
        var port = CM_PORT;
        var serviceHost = environment.getServiceHost(svc, port);
        var servicePort = environment.getServicePort(svc, port);
        System.out.println(serviceHost);
        var catalogUrl = "http://%s:%d/services/rootservices".formatted(serviceHost, servicePort);
        var getRC = pingUrl(catalogUrl);
        assertEquals(200, getRC);
    }

    private int testRootServices(String svc, int port) {
        var serviceHost = environment.getServiceHost(svc, port);
        var servicePort = environment.getServicePort(svc, port);
        System.out.println(serviceHost);
        var catalogUrl = "http://%s:%d/services/rootservices".formatted(serviceHost, servicePort);
        return pingUrl(catalogUrl);
    }

    private int pingUrl(String catalogUrl) {
        try (var client = java.net.http.HttpClient.newHttpClient()) {
            var request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(catalogUrl))
                    .GET()
                    .build();
            var response = client.send(request, java.net.http.HttpResponse.BodyHandlers.discarding());
            var getRC = response.statusCode();
            System.out.println("HTTP " + getRC + " for " + catalogUrl);
            return getRC;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
