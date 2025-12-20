package co.oslc.refimpl.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class SwaggerTest {

    public static final int STARTUP_TIMEOUT = 120;
    public static final String RM_SVC = "server-rm";
    public static final String CM_SVC = "server-cm";
    public static final String QM_SVC = "server-qm";
    public static final String AM_SVC = "server-am";

    static final int RM_PORT = 8080;
    static final int CM_PORT = 8080;
    static final int QM_PORT = 8080;
    static final int AM_PORT = 8080;

    static final Map<String, Integer> fixedPorts = Map.of(
        RM_SVC, 8800,
        CM_SVC, 8801,
        QM_SVC, 8802,
        AM_SVC, 8803
    );

    @Container
    public static ComposeContainer environment = new ComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withExposedService(RM_SVC, RM_PORT,
                    Wait.forLogMessage(".*main: Started oejs.Server@.*", 1)
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

    static Playwright playwright;
    static Browser browser;

    @BeforeAll
    static void setupSpec() {
        System.out.println("Initializing Playwright...");
        playwright = Playwright.create();
        System.out.println("Playwright initialized.");
        System.out.println("Launching Browser...");
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        System.out.println("Browser launched.");
    }

    @AfterAll
    static void cleanupSpec() {
        System.out.println("Closing browser...");
        if (browser != null) browser.close();
        System.out.println("Closing Playwright...");
        if (playwright != null) playwright.close();
    }

    void waitForService(String url) {
        int maxRetries = 60;
        int retryDelay = 1000; // 1 second

        try (var client = java.net.http.HttpClient.newHttpClient()) {
            var request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .GET()
                    .build();

            for (int i = 0; i < maxRetries; i++) {
                try {
                    var response = client.send(request, java.net.http.HttpResponse.BodyHandlers.discarding());
                    int responseCode = response.statusCode();
                    if (responseCode >= 200 && responseCode < 300) {
                        System.out.println("Service at " + url + " is ready (HTTP " + responseCode + ").");
                        return;
                    }
                    System.out.println("Waiting for service at " + url + "... (HTTP " + responseCode + ")");
                } catch (Exception e) {
                    System.out.println("Waiting for service at " + url + "... (" + e.getMessage() + ")");
                }
                try {
                    Thread.sleep(retryDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("Service at " + url + " did not start in time");
    }

    @ParameterizedTest
    @CsvSource({
        RM_SVC,
        CM_SVC,
        QM_SVC,
        AM_SVC
    })
    void swaggerUiShouldBeAccessible(String svc) {
        var serviceHost = "localhost";
        var servicePort = fixedPorts.get(svc);
        var swaggerUrl = "http://%s:%d/swagger-ui/index.jsp".formatted(serviceHost, servicePort);
        var expectedYamlUrl = "http://%s:%d/services/openapi.yaml".formatted(serviceHost, servicePort);
        var rootServicesUrl = "http://%s:%d/services/rootservices".formatted(serviceHost, servicePort);

        waitForService(rootServicesUrl);

        var authHeader = "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes());
        try (var context = browser.newContext(new Browser.NewContextOptions()
                .setExtraHTTPHeaders(Map.of("Authorization", authHeader)));
             var page = context.newPage()) {

            // Debugging: Log console messages (WARN+) and network errors
            page.onConsoleMessage(msg -> {
                if ("warning".equals(msg.type()) || "error".equals(msg.type())) {
                    System.out.println("Browser Console [" + msg.type() + "]: " + msg.text());
                }
            });
            page.onRequestFailed(request -> {
                System.out.println("Request Failed: " + request.url() + " - " + request.failure());
            });
            page.onResponse(response -> {
                if (response.status() >= 400) {
                    System.out.println("Network Error: " + response.url() + " returned " + response.status() + " " + response.statusText());
                }
            });

            page.navigate(swaggerUrl);
            var title = page.title();
            // Wait for the Swagger UI to load
            page.waitForSelector("#swagger-ui");

            // Check the input box for the YAML URL
            var inputBox = page.getByRole(AriaRole.TEXTBOX);
            var actualYamlUrl = inputBox.inputValue();

            // 1. Check that the yaml document is loaded
            // Expected heading format: "RM 1.0.0 OAS 3.0"
            var prefix = svc.replace("server-", "").toUpperCase();
            var expectedHeading = prefix + " 1.0.0 OAS 3.0";

            // Wait for the heading to appear.
            page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(expectedHeading)).waitFor();

            assertEquals(expectedYamlUrl, actualYamlUrl);

            // 2. Confirm that the following endpoint is available on the page
            // /serviceProviders/{serviceProviderId}/service2/requirementCollections/selector
            // This is only for RM.
            if (RM_SVC.equals(svc)) {
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("GET /serviceProviders/{serviceProviderId}/service2/requirementCollections/selector")).first().waitFor();
            }

            assertEquals("Swagger UI", title);
            assertTrue(page.isVisible("#swagger-ui"));
        }
    }
}
