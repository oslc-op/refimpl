package co.oslc.refimpl.client

import kotlinx.coroutines.*
import org.apache.commons.lang3.StringUtils
import org.apache.http.HttpStatus
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
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.core.Response
import kotlin.collections.HashSet
import kotlin.system.measureNanoTime


private val SPC_RM = "http://localhost:8800/services/catalog/singleton"
private val SPC_CM = "http://localhost:8801/services/catalog/singleton"
private val SPC_QM = "http://localhost:8802/services/catalog/singleton"
private val SPC_AM = "http://localhost:8803/services/catalog/singleton"

private const val N_RESOURCES = 50

val client = basicAuthClientBuilder("admin", "admin")

fun basicAuthClientBuilder(username: String, password: String): IOslcClient {
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
//    if (selfAssignedSSL) {
//        val sslContextBuilder = SSLContextBuilder()
//        sslContextBuilder.loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE)
//        clientBuilder.sslContext(sslContextBuilder.build())
//        clientBuilder.hostnameVerifier(NoopHostnameVerifier.INSTANCE)
//    }

    if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
        val authFeature: HttpAuthenticationFeature = HttpAuthenticationFeature.basic(username, password)
        clientBuilder.register(authFeature)
    }

    oslcClientBuilder.setClientBuilder(clientBuilder)
    return oslcClientBuilder.build()
}

val rmTraverser = ServiceProviderCatalogTraverser(SPC_RM, client)
val cmTraverser = ServiceProviderCatalogTraverser(SPC_CM, client)
val qmTraverser = ServiceProviderCatalogTraverser(SPC_QM, client)
val amTraverser = ServiceProviderCatalogTraverser(SPC_AM, client)


fun main() {
    println("Populating OSLC RefImpl servers with sample data.\n")

    val rmRequirements = CreationFactoryPopulator(client, rmTraverser, N_RESOURCES, SimpleResourceGen(::genRequirement),
            Requirement::class.java).populate()
    val rmRequirementColl = CreationFactoryPopulator(client, rmTraverser, N_RESOURCES,
            SimpleResourceGen(::genRequirementColl), RequirementCollection::class.java).populate()

    val cmChangeReq = CreationFactoryPopulator(client, cmTraverser, N_RESOURCES, ChangeRequestGen(rmRequirements),
            ChangeRequest::class.java).populate()


    linkChangeRequestsToRequirements(cmChangeReq, rmRequirements)

    val qmTestPlans = CreationFactoryPopulator(client, qmTraverser, N_RESOURCES,
            SimpleResourceGen(::genPlan), TestPlan::class.java).populate()
    val qmTestCases = CreationFactoryPopulator(client, qmTraverser, N_RESOURCES,
            SimpleResourceGen(::genTestCase), TestCase::class.java).populate()
    val qmTestResults = CreationFactoryPopulator(client, qmTraverser, N_RESOURCES,
            SimpleResourceGen(::genTestResult), TestResult::class.java).populate()
    val qmTestExecRecords = CreationFactoryPopulator(client, qmTraverser, N_RESOURCES,
            SimpleResourceGen(::genTestExecutionRecord), TestExecutionRecord::class.java).populate()
    val qmTestScripts = CreationFactoryPopulator(client, qmTraverser, N_RESOURCES,
            SimpleResourceGen(::genTestScript), TestScript::class.java).populate()


    val amResources = CreationFactoryPopulator(client, amTraverser, N_RESOURCES,
            SimpleResourceGen(::genAMResource), Resource::class.java).populate()
    val amLinks = CreationFactoryPopulator(client, amTraverser, N_RESOURCES,
            SimpleResourceGen(::genAMLink), LinkType::class.java).populate()

    linkTestPlanstoChangeRequests(qmTestPlans, cmChangeReq)
}

fun linkChangeRequestsToRequirements(cmChangeReq: List<Link>, rmRequirements: List<Link>) {
    assert(cmChangeReq.size == rmRequirements.size)

    val changeRequests = cmChangeReq.shuffled()
    changeRequests.zip(rmRequirements).forEach {
        linkChangeRequestsToRequirementsSingle(it.first, it.second)
    }
}

fun linkChangeRequestsToRequirementsSingle(chgRequestLink: Link, reqLink: Link) {
    val chgRequest = doGetResource(client, chgRequestLink.value, ChangeRequest::class.java)!!

    chgRequest.addImplementsRequirement(reqLink)
    client.updateResource(chgRequest.about.toString(), chgRequest, OslcMediaType.APPLICATION_RDF_XML)
    println("Created a link (${chgRequestLink.value} :ChangeRequest)-[:implementsRequirement]-(${reqLink.value} :Requirement)")
}

fun linkTestPlanstoChangeRequests(qmTestPlans: List<Link>, cmChangeReq: List<Link>) {
    assert(qmTestPlans.size == cmChangeReq.size)

    val shuffled = qmTestPlans.shuffled()
    shuffled.zip(cmChangeReq).forEach {
        linkTestPlanstoChangeRequestsSingle(it.first, it.second)
    }
}

fun linkTestPlanstoChangeRequestsSingle(planLink: Link, chgRequestLink: Link) {
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
    val response = client.getResource(catalogURI.toString())

    if (response.status != HttpStatus.SC_OK) {
        println("Cannot read $catalogURI (HTTP ${response.status})")
        throw IllegalStateException()
    }

    return response.readEntity(clazz)
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
            for (i in 1..5) { //retry up to 5 times
                try {
                    response = client.createResource(cfURI, r, "text/turtle")
                    if (response != null) {
                        if (response.status < 400) {
                            val headers = response.headers
                            val location: MutableList<Any>? = headers["Location"]
                            if (location != null) {
                                val url: String = location.single() as String
                                println("$url created via $cfURI")
                            } else {
                                println("WARNING! A Resource was created but no Location was given")
                            }

                        } else {
                            println("Failed to create a resource via $cfURI")
                        }
                        break
                    }
                } catch (e: ConnectException) {
                    println("Connection failed, retrying... ($cfURI)")
                }
            }
            Pair(response, r)
        }
    }
    runBlocking(scope.coroutineContext) {
        val responses: List<Pair<Response?, T>> = responsesAsync.awaitAll()
        responses.forEach { (response, _) ->
            if (response != null) {
                if (response.status < 400) {
                    val headers = response.headers
                    val location: MutableList<Any>? = headers["Location"]
                    if (location != null) {
                        val url: String = location.single() as String
                        createdUrls.add(Link(URI.create(url), "TBD"))
                    }
                }
            }
        }
    }
    return createdUrls
}


