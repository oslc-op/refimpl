import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

import java.time.Duration

@Testcontainers
class OslcSpec extends Specification {
    public static final String RM_SVC = "server-rm"
    public static final String CM_SVC = "server-cm"
    public static final String QM_SVC = "server-qm"
    public static final String AM_SVC = "server-am"

    static final int RM_PORT = 8080
    static final int CM_PORT = 8080
    static final int QM_PORT = 8080
    static final int AM_PORT = 8080

    public static final int STARTUP_TIMEOUT = 120

//    static private File composeFile = new File("../docker-compose.yml")
    static private File composeFile = new File("src/test/resources/docker-compose.yml")

    static private DockerComposeContainer environment =
            new DockerComposeContainer(composeFile)
                     .withExposedService(RM_SVC, RM_PORT,
                             Wait.forLogMessage(".*main: Started Server@.*", 1)
                                     .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT)))
                     .withExposedService(CM_SVC, CM_PORT,
                             Wait.forLogMessage(".*main: Started Server@.*", 1)
                                     .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT)))
                     .withExposedService(QM_SVC, QM_PORT,
                             Wait.forLogMessage(".*main: Started Server@.*", 1)
                                     .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT)))
                     .withExposedService(AM_SVC, AM_PORT,
                             Wait.forLogMessage(".*main: Started Server@.*", 1)
                                     .withStartupTimeout(Duration.ofSeconds(STARTUP_TIMEOUT)))
                    .withLocalCompose(true)
//                    .withEnv(System.getenv())

    def setupSpec() {
        environment.start()

        try {
            println(composeFile.canonicalPath)
        } catch(e) {
            println(e.message)
        }
    }

    def "Rootservices data table test"() {
        expect:
        testRootServices(svc, port) == 200

        where:
        svc    | port
        RM_SVC | RM_PORT
        CM_SVC | CM_PORT
        QM_SVC | QM_PORT
        AM_SVC | AM_PORT
    }

    // kept just for the given/when/then example, see the data table test
    def "CM rootservices test"() {
        given:
        def svc = CM_SVC
        def port = CM_PORT
        def serviceHost = environment.getServiceHost(svc, port)
        def servicePort = environment.getServicePort(svc, port)
        println(serviceHost)
        def catalogUrl = "http://${serviceHost}:${servicePort}/services/rootservices"

        when:
        def getRC = pingUrl(catalogUrl)

        then:
        getRC == 200
    }


    private int testRootServices(String svc, int port) {
        def serviceHost = environment.getServiceHost(svc, port)
        def servicePort = environment.getServicePort(svc, port)
        println(serviceHost)
        def catalogUrl = "http://${serviceHost}:${servicePort}/services/rootservices"
        def getRC = pingUrl(catalogUrl)
        getRC
    }

    def pingUrl(String catalogUrl) {
        def getRC
        try {
            def get = new URL(catalogUrl).openConnection();
            getRC = get.getResponseCode();
            println("HTTP ${getRC} for ${catalogUrl}")
            print(get.getInputStream().text)
        } catch (e) {
            println("HTTP conn failed for ${catalogUrl}")
            getRC = 599
        }
        return getRC
    }

    def cleanupSpec() {
        environment.stop()
    }
}
