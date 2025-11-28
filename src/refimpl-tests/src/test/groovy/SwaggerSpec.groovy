import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.AriaRole
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration
import java.util.Base64

@Testcontainers
class SwaggerSpec extends Specification {
    public static final int STARTUP_TIMEOUT = 120

    public static final String RM_SVC = "server-rm"
    public static final String CM_SVC = "server-cm"
    public static final String QM_SVC = "server-qm"
    public static final String AM_SVC = "server-am"

    static final int RM_PORT = 8080
    static final int CM_PORT = 8080
    static final int QM_PORT = 8080
    static final int AM_PORT = 8080
    static final Map<String, Integer> fixedPorts = [
        (RM_SVC): 8800,
        (CM_SVC): 8801,
        (QM_SVC): 8802,
        (AM_SVC): 8803
    ]
    static private File composeFile = new File("src/test/resources/docker-compose.yml")

    @Shared
    static private ComposeContainer environment =
            new ComposeContainer(composeFile)
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
                    .withLocalCompose(true)

    @Shared
    Playwright playwright

    @Shared
    Browser browser

    def setupSpec() {
        println "Starting environment..."
        environment.start()
        println "Environment started."
        println "Initializing Playwright..."
        playwright = Playwright.create()
        println "Playwright initialized."
        println "Launching Browser..."
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true))
        println "Browser launched."
    }

    def cleanupSpec() {
        println "Closing browser..."
        browser?.close()
        println "Closing Playwright..."
        playwright?.close()
        println "Stopping environment..."
        environment.stop()
        println "Environment stopped."
    }

    void waitForService(String url) {
        int maxRetries = 60
        int retryDelay = 1000 // 1 second
        int responseCode = 0
        for (int i = 0; i < maxRetries; i++) {
            try {
                def connection = new URL(url).openConnection()
                responseCode = connection.getResponseCode()
                if (responseCode >= 200 && responseCode < 300) {
                    println "Service at $url is ready (HTTP $responseCode)."
                    return
                }
                println "Waiting for service at $url... (HTTP $responseCode)"
            } catch (Exception e) {
                println "Waiting for service at $url... (${e.message})"
            }
            Thread.sleep(retryDelay)
        }
        throw new RuntimeException("Service at $url did not start in time (last HTTP $responseCode)")
    }

    def "Swagger UI should be accessible for #svc"() {
        setup:
        def serviceHost = "localhost"
        def servicePort = fixedPorts[svc]
        def swaggerUrl = "http://${serviceHost}:${servicePort}/swagger-ui/index.jsp"
        def expectedYamlUrl = "http://${serviceHost}:${servicePort}/services/openapi.yaml"
        def rootServicesUrl = "http://${serviceHost}:${servicePort}/services/rootservices"

        waitForService(rootServicesUrl)

        def authHeader = "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes())
        def context = browser.newContext(new Browser.NewContextOptions()
                .setExtraHTTPHeaders(["Authorization": authHeader]))
        def page = context.newPage()

        // Debugging: Log console messages (WARN+) and network errors
        page.onConsoleMessage { msg ->
            if (["warning", "error"].contains(msg.type())) {
                println "Browser Console [${msg.type()}]: ${msg.text()}"
            }
        }
        page.onRequestFailed { request ->
            println "Request Failed: ${request.url()} - ${request.failure()}"
        }
        page.onResponse { response ->
            if (response.status() >= 400) {
                println "Network Error: ${response.url()} returned ${response.status()} ${response.statusText()}"
            }
        }

        when:
        page.navigate(swaggerUrl)
        def title = page.title()
        // Wait for the Swagger UI to load
        page.waitForSelector("#swagger-ui")

        // Check the input box for the YAML URL
        def inputBox = page.getByRole(AriaRole.TEXTBOX)
        def actualYamlUrl = inputBox.inputValue()

        // 1. Check that the yaml document is loaded
        // Expected heading format: "RM 1.0.0 OAS 3.0"
        def prefix = svc.replace("server-", "").toUpperCase()
        def expectedHeading = "${prefix} 1.0.0 OAS 3.0"

        // Wait for the heading to appear.
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(expectedHeading)).waitFor()

        assert actualYamlUrl == expectedYamlUrl

        // 2. Confirm that the following endpoint is available on the page
        // /serviceProviders/{serviceProviderId}/service2/requirementCollections/selector
        // This is only for RM.
        if (svc == RM_SVC) {
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("GET /serviceProviders/{serviceProviderId}/service2/requirementCollections/selector")).first().waitFor()
        }

        then:
        title == "Swagger UI"
        page.isVisible("#swagger-ui")

        cleanup:
        page.close()
        context.close()

        where:
        svc    | port
        RM_SVC | RM_PORT
        CM_SVC | CM_PORT
        QM_SVC | QM_PORT
        AM_SVC | AM_PORT
    }
}
