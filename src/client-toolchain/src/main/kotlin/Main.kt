import kotlinx.coroutines.*
import org.eclipse.lyo.oslc.domains.qm.TestPlan
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

fun main() {
    println("Populating OSLC RefImpl servers with sample data.\n")
    val client = OslcClient()

    populateRM(client)
    populateQM(client)
}


fun populateRM(client: OslcClient) {
    val rmTraverser = ServiceProviderCatalogTraverser("http://localhost:8800/services/catalog/singleton", client)
    val reqPopulator = CreationFactoryPopulator(client, rmTraverser, 50, RandomResourceGen(::genRequirement),
            Requirement::class.java)
    reqPopulator.populate()
    val reqCollPopulator = CreationFactoryPopulator(client, rmTraverser, 30,
            RandomResourceGen(::genRequirementColl), RequirementCollection::class.java)
    reqCollPopulator.populate()
}

private fun populateQM(client: OslcClient) {
    val qmTraverser = ServiceProviderCatalogTraverser("http://localhost:8802/services/catalog/singleton", client)
    var planPopulator = CreationFactoryPopulator(client, qmTraverser, 50, RandomResourceGen(::genPlan), TestPlan::class.java)
    planPopulator.populate()
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

class CreationFactoryPopulator<T : AbstractResource>(private val client: OslcClient, private val catalogTraverser: ServiceProviderCatalogTraverser, private val genCount: Int, private val genFunc: RandomResourceGen<T>, private val clazz: Class<T>) {
    fun populate() {
        val factories = filterRelevantFactories()
        val resources: MutableList<Link> = ArrayList()
        val nanoTime: Long = measureNanoTime {
            runBlocking {
                factories.forEach {
                    launch(Dispatchers.IO) {
                        synchronized(resources) {
                            val postResources: Set<Link> = postResources(client, it.second, it.first, genCount, genFunc, this)
                            resources.addAll(postResources)
                        }
                    }
                }
            }
        }
        println("Created ${resources.size} in ${nanoTime / 1000 / 1000}ms")
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

fun <T : AbstractResource> postResources(client: OslcClient, cf: CreationFactory, sp: ServiceProvider, count: Int, generator: RandomResourceGen<T>, scope: CoroutineScope): Set<Link> {
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
                            if(location != null) {
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
                    if(location != null) {
                        val url: String = location.single() as String
                        createdUrls.add(Link(URI.create(url), "TBD"))
                    }
                }
            }
        }
    }
    return createdUrls
}

fun genRequirement(sp: ServiceProvider, id: Int): Requirement {
    val r = Requirement()
    r.apply {
        shortTitle = "${sp.identifier.toUpperCase()}-$id"
        identifier = "req_$id"
        title = "Requirement no. $id"
        description = "Requirement no. $id was generated by a 'client-toolchain' project. Edit its to change how these resources look."
        modified = Date()
    }
    return r
}

fun genRequirementColl(sp: ServiceProvider, id: Int): RequirementCollection {
    val r = RequirementCollection()
    r.apply {
        shortTitle = "${sp.identifier.toUpperCase()}-C$id"
        identifier = "rq_coll_$id"
        title = "Requirement Collection no. $id"
        description = "Requirement Collecion no. $id was generated by a 'client-toolchain' project. Edit its to change how these resources look."
        modified = Date()
    }
    return r
}

fun genPlan(sp: ServiceProvider, id: Int): TestPlan {
    var r = TestPlan()
    r.apply {
        title = "Test Plan no. $id"
        created = Date()
        // TODO: 2020-04-02 set more fields
        // TODO: 2020-04-02 find a way to link resources
    }
    return r
}