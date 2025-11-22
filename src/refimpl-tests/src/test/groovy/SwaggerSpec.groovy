import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration

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
        environment.start()
        playwright = Playwright.create()
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true))
    }

    def cleanupSpec() {
        browser?.close()
        playwright?.close()
        environment.stop()
    }

    def "Swagger UI should be accessible for #svc"() {
        setup:
        def serviceHost = environment.getServiceHost(svc, port)
        def servicePort = environment.getServicePort(svc, port)
        def swaggerUrl = "http://${serviceHost}:${servicePort}/swagger-ui/index.jsp"
        def page = browser.newPage()

        when:
        page.navigate(swaggerUrl)
        def title = page.title()
        // Wait for the Swagger UI to load
        page.waitForSelector("#swagger-ui")

        then:
        title == "Swagger UI"
        page.isVisible("#swagger-ui")

        cleanup:
        page.close()

        where:
        svc    | port
        RM_SVC | RM_PORT
        CM_SVC | CM_PORT
        QM_SVC | QM_PORT
        AM_SVC | AM_PORT
    }
}
