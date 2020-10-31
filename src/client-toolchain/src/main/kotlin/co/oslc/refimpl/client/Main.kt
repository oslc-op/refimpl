package co.oslc.refimpl.client

import kotlinx.coroutines.*
import org.eclipse.lyo.oslc.domains.cm.ChangeRequest
import org.eclipse.lyo.oslc.domains.qm.TestCase
import org.eclipse.lyo.oslc.domains.qm.TestExecutionRecord
import org.eclipse.lyo.oslc.domains.qm.TestPlan
import org.eclipse.lyo.oslc.domains.qm.TestResult
import org.eclipse.lyo.oslc.domains.rm.Requirement
import org.eclipse.lyo.oslc.domains.rm.RequirementCollection
import org.eclipse.lyo.oslc4j.client.OslcClient
import org.eclipse.lyo.oslc4j.core.annotation.OslcResourceShape
import org.eclipse.lyo.oslc4j.core.model.*
import java.net.ConnectException
import java.net.URI
import java.util.*
import javax.ws.rs.core.Response
import kotlin.collections.HashSet
import kotlin.system.measureNanoTime

private val SPC_RM = "http://localhost:8800/services/catalog/singleton"
private val SPC_CM = "http://localhost:8801/services/catalog/singleton"
private val SPC_QM = "http://localhost:8802/services/catalog/singleton"

fun main() {
    println("Populating OSLC RefImpl servers with sample data.\n")
    val client = OslcClient()

    val rmTraverser = ServiceProviderCatalogTraverser(SPC_RM, client)
    val reqPopulator = CreationFactoryPopulator(client, rmTraverser, 30, SimpleResourceGen(::genRequirement),
            Requirement::class.java)
    val requirements = reqPopulator.populate()
    val reqCollPopulator = CreationFactoryPopulator(client, rmTraverser, 30,
            SimpleResourceGen(::genRequirementColl), RequirementCollection::class.java)
    reqCollPopulator.populate()

    val cmTraverser = ServiceProviderCatalogTraverser(SPC_CM, client)
    val chReqPopulator = CreationFactoryPopulator(client, cmTraverser, 30, ChangeRequestGen(requirements),
            ChangeRequest::class.java)
    chReqPopulator.populate()

    val qmTraverser = ServiceProviderCatalogTraverser(SPC_QM, client)

    val qmGenerators = listOf(
            CreationFactoryPopulator(client, qmTraverser, 30,
                    SimpleResourceGen(::genPlan), TestPlan::class.java),
            CreationFactoryPopulator(client, qmTraverser, 30,
                    SimpleResourceGen(::genTestCase), TestCase::class.java),
            CreationFactoryPopulator(client, qmTraverser, 30,
                    SimpleResourceGen(::genTestResult), TestResult::class.java),
            CreationFactoryPopulator(client, qmTraverser, 30,
                    SimpleResourceGen(::genTestExecutionRecord), TestExecutionRecord::class.java)
    )
    qmGenerators.forEach { it.populate() }

}


class ServiceProviderCatalogTraverser(private val spCatalog: String, private val client: OslcClient) {
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
        val catalog = client.getResource(spCatalog, ServiceProviderCatalog::class.java)
        println("Scanned catalog ${catalog.title}")
        synchronized(providers) {
            providers.addAll(catalog.serviceProviders)
        }
        catalog.referencedServiceProviderCatalogs.forEach {
            coroutineScope.launch {
                fetchProvidersRecursive(it, coroutineScope)
            }
        }
    }

}

class CreationFactoryPopulator<T : AbstractResource>(private val client: OslcClient,
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

    private inline fun filterRelevantFactories(): List<Pair<ServiceProvider, CreationFactory>> {
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

fun <T : AbstractResource> postResources(client: OslcClient, cf: CreationFactory, sp: ServiceProvider, count: Int,
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


