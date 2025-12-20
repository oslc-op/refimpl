package co.oslc.refimpl.client

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import kotlinx.coroutines.*
import org.apache.commons.lang3.StringUtils
import org.apache.http.HttpStatus
import org.apache.http.conn.ssl.DefaultHostnameVerifier
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.ssl.SSLContextBuilder
import org.eclipse.lyo.oslc.domains.am.LinkType
import org.eclipse.lyo.oslc.domains.am.Resource
import org.eclipse.lyo.oslc.domains.cm.ChangeRequest
import org.eclipse.lyo.oslc.domains.qm.*
import org.eclipse.lyo.oslc.domains.rm.Requirement
import org.eclipse.lyo.oslc.domains.rm.RequirementCollection
import org.eclipse.lyo.client.IOslcClient
import org.eclipse.lyo.client.OslcClientFactory
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape
import org.eclipse.lyo.oslc4j.core.model.*
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider
import org.glassfish.jersey.client.ClientConfig
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature
import java.net.ConnectException
import java.net.URI
import java.util.*
import javax.net.ssl.SSLContext
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.core.Response
import kotlin.collections.HashSet
import kotlin.system.measureNanoTime


private val SPC_RM_DEFAULT = "http://localhost:8800/services/catalog/singleton"
private val SPC_CM_DEFAULT = "http://localhost:8801/services/catalog/singleton"
private val SPC_AM_DEFAULT = "http://localhost:8803/services/catalog/singleton"
private val SPC_QM_DEFAULT = "http://localhost:8802/services/catalog/singleton"
private val INIT_DEFAULT = "am,cm,rm,qm"

private const val N_RESOURCES = 50


class RefImplClientArgs(parser: ArgParser) {

//    val v by parser.flagging("enable verbose mode")

    val rm by parser.storing("OSLC RM SPCatalog URI").default(SPC_RM_DEFAULT)
    val cm by parser.storing("OSLC CM SPCatalog URI").default(SPC_CM_DEFAULT)
    val qm by parser.storing("OSLC QM SPCatalog URI").default(SPC_QM_DEFAULT)
    val am by parser.storing("OSLC AM SPCatalog URI").default(SPC_AM_DEFAULT)
    val initServers by parser.storing("Only initialize a subset of the OSLC Servers (comma-separated, e.g. am,rm)")
        .default(INIT_DEFAULT)

    val selfAssignedCert by parser.flagging("Disable TLS security").default(false)

//    val source by parser.positional("source filename")

}

fun basicAuthClientBuilder(username: String, password: String, selfAssignedSSL: Boolean): IOslcClient {
    val oslcClientBuilder = OslcClientFactory.oslcClientBuilder()

    val apacheConnectorProvider = ApacheConnectorProvider()

    //FIXME: see https://github.com/eclipse/lyo.client/issues/108
    val clientConfig = ClientConfig()
//            .connectorProvider(apacheConnectorProvider)
//            .property(ApacheClientProperties.DISABLE_COOKIES, false)
//            .property(ApacheClientProperties.REQUEST_CONFIG, RequestConfig.custom()
//                    .setConnectTimeout(1000)
//                    .setSocketTimeout(2000)
//                    .build())
    val clientBuilder = ClientBuilder.newBuilder()
    clientBuilder.withConfig(clientConfig)

    // Setup SSL support to ignore self-assigned SSL certificates - for testing only!!
    if (selfAssignedSSL) {
        val sslContextBuilder = SSLContextBuilder()
        sslContextBuilder.loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE)
        clientBuilder.sslContext(sslContextBuilder.build())
        clientBuilder.hostnameVerifier(NoopHostnameVerifier.INSTANCE)
    } else {
//        SSLContext sc = SSLContext.getInstance("TLSv1.2")
//        val sslContextBuilder = SSLContextBuilder()
//        clientBuilder.sslContext(sslContextBuilder.build())
//        clientBuilder.hostnameVerifier(DefaultHostnameVerifier())
    }

    if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
        val authFeature: HttpAuthenticationFeature = HttpAuthenticationFeature.basic(username, password)
        clientBuilder.register(authFeature)
//        clientBuilder.
    }

    oslcClientBuilder.setClientBuilder(clientBuilder)
    return oslcClientBuilder.build()
}


fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::RefImplClientArgs).run {
        println("Populating OSLC RefImpl servers with sample data.\n")
        val client = basicAuthClientBuilder("admin", "admin", this.selfAssignedCert)

        val rmTraverser = ServiceProviderCatalogTraverser(this.rm, client)
        val cmTraverser = ServiceProviderCatalogTraverser(this.cm, client)
        val qmTraverser = ServiceProviderCatalogTraverser(this.qm, client)
        val amTraverser = ServiceProviderCatalogTraverser(this.am, client)

        val initList = this.initServers.split(",").map { s -> s.trim().lowercase() }
        val initAM = initList.contains("am")
        val initCM = initList.contains("cm")
        val initRM = initList.contains("rm")
        val initQM = initList.contains("qm")

        var rmRequirements: List<Link> = emptyList()
        var cmChangeReq: List<Link> = emptyList()
        if (initRM) {
            rmRequirements = CreationFactoryPopulator(
                client, rmTraverser, N_RESOURCES, SimpleResourceGenWithBaseUri(::genRequirementWithBaseUri, this.rm),
                Requirement::class.java
            ).populate()
            val rmRequirementColl = CreationFactoryPopulator(
                client, rmTraverser, N_RESOURCES,
                SimpleResourceGen(::genRequirementColl), RequirementCollection::class.java
            ).populate()
        }
        if (initCM) {
            cmChangeReq = CreationFactoryPopulator(
                client, cmTraverser, N_RESOURCES, ChangeRequestGen(rmRequirements),
                ChangeRequest::class.java
            ).populate()
            if (initRM) {
                linkChangeRequestsToRequirements(client, cmChangeReq, rmRequirements)
            }
        }

        var qmTestPlans: List<Link> = emptyList()
        if (initQM) {
            qmTestPlans = CreationFactoryPopulator(
                client, qmTraverser, N_RESOURCES,
                SimpleResourceGen(::genPlan), TestPlan::class.java
            ).populate()
            val qmTestCases = CreationFactoryPopulator(
                client, qmTraverser, N_RESOURCES,
                SimpleResourceGen(::genTestCase), TestCase::class.java
            ).populate()
            val qmTestResults = CreationFactoryPopulator(
                client, qmTraverser, N_RESOURCES,
                SimpleResourceGen(::genTestResult), TestResult::class.java
            ).populate()
            val qmTestExecRecords = CreationFactoryPopulator(
                client, qmTraverser, N_RESOURCES,
                SimpleResourceGen(::genTestExecutionRecord), TestExecutionRecord::class.java
            ).populate()
            val qmTestScripts = CreationFactoryPopulator(
                client, qmTraverser, N_RESOURCES,
                SimpleResourceGen(::genTestScript), TestScript::class.java
            ).populate()
        }

        if (initAM) {
            val amResources = CreationFactoryPopulator(
                client, amTraverser, N_RESOURCES,
                SimpleResourceGen(::genAMResource), Resource::class.java
            ).populate()
            val amLinks = CreationFactoryPopulator(
                client, amTraverser, N_RESOURCES,
                SimpleResourceGen(::genAMLink), LinkType::class.java
            ).populate()
        }
        if (initCM && initQM) {
            linkTestPlanstoChangeRequests(client, qmTestPlans, cmChangeReq)
        }
    }
}

fun linkChangeRequestsToRequirements(client:IOslcClient, cmChangeReq: List<Link>, rmRequirements: List<Link>) {
    assert(cmChangeReq.size == rmRequirements.size)

    val changeRequests = cmChangeReq.shuffled()
    changeRequests.zip(rmRequirements).forEach {
        linkChangeRequestsToRequirementsSingle(client, it.first, it.second)
    }
}

fun linkChangeRequestsToRequirementsSingle(client:IOslcClient, chgRequestLink: Link, reqLink: Link) {
    val chgRequest = doGetResource(client, chgRequestLink.value, ChangeRequest::class.java)!!

    chgRequest.addImplementsRequirement(reqLink)
    client.updateResource(chgRequest.about.toString(), chgRequest, OslcMediaType.APPLICATION_RDF_XML)
    println("Created a link (${chgRequestLink.value} :ChangeRequest)-[:implementsRequirement]-(${reqLink.value} :Requirement)")
}

fun linkTestPlanstoChangeRequests(client:IOslcClient, qmTestPlans: List<Link>, cmChangeReq: List<Link>) {
    assert(qmTestPlans.size == cmChangeReq.size)

    val shuffled = qmTestPlans.shuffled()
    shuffled.zip(cmChangeReq).forEach {
        linkTestPlanstoChangeRequestsSingle(client, it.first, it.second)
    }
}

fun linkTestPlanstoChangeRequestsSingle(client:IOslcClient, planLink: Link, chgRequestLink: Link) {
//    val chgRequest = client.getResource(chgRequestLink, ChangeRequest::class.java)
    val testPlan = doGetResource(client, planLink.value, TestPlan::class.java)!!

    testPlan.addRelatedChangeRequest(chgRequestLink)
    client.updateResource(testPlan.about.toString(), testPlan, OslcMediaType.APPLICATION_RDF_XML)
    println("Created a link (${planLink.value} :TestPlan)-[:relatedChangeRequest]-(${chgRequestLink.value} :ChangeRequest)")
}

class ServiceProviderCatalogTraverser(private val spCatalog: String, private val client: IOslcClient) {
    private val providers: MutableSet<ServiceProvider> = HashSet()

    fun fetchAllProviders(): Set<ServiceProvider> {
        if (providers.isEmpty()) {
            val rootCatalogURI = URI.create(spCatalog)
            runBlocking {
                launch(Dispatchers.IO) {
                    fetchProvidersRecursive(rootCatalogURI, this)
                }
            }
        }
        return providers.toSet()
    }

    private suspend fun fetchProvidersRecursive(catalogURI: URI, coroutineScope: CoroutineScope) {
        val catalog: ServiceProviderCatalog? = doGetResource(client, catalogURI, ServiceProviderCatalog::class.java)
        println("Scanned catalog ${catalog?.title}")
        synchronized(providers) {
            providers.addAll(catalog!!.serviceProviders)
        }
        catalog?.referencedServiceProviderCatalogs?.forEach {
            coroutineScope.launch {
                fetchProvidersRecursive(it, coroutineScope)
            }
        }
    }


}

fun <T : AbstractResource> doGetResource(client: IOslcClient, catalogURI: URI, clazz: Class<T>): T? {
    val maxRetries = 5
    var lastException: Exception? = null

    for (attempt in 1..maxRetries) {
        try {
            val response = client.getResource(catalogURI.toString())

            when {
                response.status == HttpStatus.SC_OK -> {
                    return response.readEntity(clazz)
                }
                response.status >= 500 -> {
                    // Server error - retry
                    val delayMs = (1000 * Math.pow(2.0, (attempt - 1).toDouble())).toLong()
                    println("Server error ${response.status} for $catalogURI, retrying in ${delayMs}ms (attempt $attempt/$maxRetries)")
                    if (attempt < maxRetries) {
                        Thread.sleep(delayMs)
                        continue
                    } else {
                        println("Cannot read $catalogURI (HTTP ${response.status}) - max retries exceeded")
                        throw IllegalStateException("Max retries exceeded for $catalogURI")
                    }
                }
                else -> {
                    // Client error (4xx) - don't retry
                    println("Cannot read $catalogURI (HTTP ${response.status}) - client error, not retrying")
                    throw IllegalStateException("Client error ${response.status} for $catalogURI")
                }
            }
        } catch (e: ConnectException) {
            lastException = e
            val delayMs = (1000 * Math.pow(2.0, (attempt - 1).toDouble())).toLong()
            println("Connection failed for $catalogURI, retrying in ${delayMs}ms (attempt $attempt/$maxRetries)")
            if (attempt < maxRetries) {
                Thread.sleep(delayMs)
                continue
            } else {
                println("Cannot connect to $catalogURI - max retries exceeded")
                throw IllegalStateException("Max retries exceeded for $catalogURI", e)
            }
        } catch (e: Exception) {
            // For other exceptions, don't retry
            println("Unexpected error reading $catalogURI: ${e.message}")
            throw e
        }
    }

    // This should never be reached, but just in case
    throw IllegalStateException("Failed to get resource after $maxRetries attempts", lastException)
}

class CreationFactoryPopulator<T : AbstractResource>(private val client: IOslcClient,
                                                     private val catalogTraverser: ServiceProviderCatalogTraverser,
                                                     private val genCount: Int, private val genFunc: RandomResourceGen<T>,
                                                     private val clazz: Class<T>) {
    fun populate(): List<Link> {
        val factories = filterRelevantFactories()
        val resources: MutableList<Link> = ArrayList()
        val nanoTime: Long = measureNanoTime {
            runBlocking {
                factories.forEach {
                    launch(Dispatchers.IO) {
                        val postResources: Set<Link> = postResources(client, it.second, it.first, genCount, genFunc, this)
                        synchronized(resources) {
                            resources.addAll(postResources)
                        }
                    }
                }
            }
        }
        println("Created ${resources.size} in ${nanoTime / 1000 / 1000}ms")
        return resources.toList()
    }

    private fun filterRelevantFactories(): List<Pair<ServiceProvider, CreationFactory>> {
        val describedByShape: Set<String> = clazz.getAnnotation(OslcResourceShape::class.java).describes.toSet()
        // TODO: 2020-04-02 these may be too lax semantics and we may need to compare strictly by the shape URI
        val factories = discoverCreationFactories().filter { (_, cf) ->
            cf.resourceTypes.any { describedByShape.contains(it.toString()) }
        }
        return factories
    }

    private fun discoverCreationFactories(): List<Pair<ServiceProvider, CreationFactory>> {
        val creationFactorties: List<Pair<ServiceProvider, CreationFactory>> = catalogTraverser.fetchAllProviders()
                .flatMap { sp -> sp.services.toList().map { svc -> Pair(sp, svc) } }
                .flatMap { (sp, svc) -> svc.creationFactories.toList().map { cf -> Pair(sp, cf) } }
        return creationFactorties
    }

}

fun <T : AbstractResource> postResources(client: IOslcClient, cf: CreationFactory, sp: ServiceProvider, count: Int,
                                         generator: RandomResourceGen<T>, scope: CoroutineScope): Set<Link> {
    val createdUrls = HashSet<Link>()
    val resources = generator.generate(sp, count)
    val responsesAsync: List<Deferred<Pair<Response?, T>>> = resources.map { r ->
        scope.async {
            val cfURI = cf.creation.toString()
            var response: Response? = null
            val maxRetries = 5

            for (attempt in 1..maxRetries) {
                try {
                    response = client.createResource(cfURI, r, "text/turtle")
                    if (response != null) {
                        when {
                            response.status < 400 -> {
                                val headers = response.headers
                                val location: MutableList<Any>? = headers["Location"]
                                if (location != null) {
                                    val url: String = location.single() as String
                                    println("$url created via $cfURI")
                                } else {
                                    println("WARNING! A Resource was created but no Location was given")
                                }
                                break
                            }
                            response.status >= 500 -> {
                                // Server error - retry
                                val delayMs = (500 * Math.pow(2.0, (attempt - 1).toDouble())).toLong()
                                println("Server error ${response.status} creating resource via $cfURI, retrying in ${delayMs}ms (attempt $attempt/$maxRetries)")
                                if (attempt < maxRetries) {
                                    Thread.sleep(delayMs)
                                    continue
                                } else {
                                    println("Failed to create a resource via $cfURI after $maxRetries attempts")
                                    break
                                }
                            }
                            else -> {
                                // Client error (4xx) - don't retry
                                println("Failed to create a resource via $cfURI (HTTP ${response.status})")
                                break
                            }
                        }
                    }
                } catch (e: ConnectException) {
                    val delayMs = (500 * Math.pow(2.0, (attempt - 1).toDouble())).toLong()
                    println("Connection failed creating resource via $cfURI, retrying in ${delayMs}ms (attempt $attempt/$maxRetries)")
                    if (attempt < maxRetries) {
                        Thread.sleep(delayMs)
                        continue
                    } else {
                        println("Failed to create a resource via $cfURI after $maxRetries connection attempts")
                        break
                    }
                } catch (e: Exception) {
                    println("Unexpected error creating resource via $cfURI: ${e.message}")
                    break
                }
            }
            Pair(response, r)
        }
    }
    runBlocking(scope.coroutineContext) {
        val responses: List<Pair<Response?, T>> = responsesAsync.awaitAll()
        responses.forEach { (response, x) ->
            if (response != null) {
                if (response.status < 400) {
                    val headers = response.headers
                    val location: MutableList<Any>? = headers["Location"]
                    if (location != null) {
                        val url: String = location.single() as String
                        createdUrls.add(Link(URI.create(url), shortCode(x)))
                    }
                }
            }
        }
    }
    return createdUrls
}

fun shortCode(x: AbstractResource): String = when(x) {
    is Requirement -> x.shortTitle
    is RequirementCollection -> x.shortTitle
    is ChangeRequest -> x.shortTitle
    is TestPlan -> "TP-${x.identifier}"
    else -> "External link"
}


