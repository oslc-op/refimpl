import javax.ws.rs.core.Response
@GrabResolver(name = 'lyo', root = 'https://repo.eclipse.org/content/repositories/lyo-releases/')
@GrabResolver(name = 'lyo-snapshots', root = 'https://repo.eclipse.org/content/repositories/lyo-snapshots/')
@Grab(group = 'org.eclipse.lyo.oslc4j.core', module = 'oslc4j-core', version = '4.0.0-SNAPSHOT')
@Grab(group = 'org.eclipse.lyo.oslc4j.core', module = 'oslc4j-jena-provider', version = '4.0.0-SNAPSHOT')
@Grab(group = 'org.eclipse.lyo.clients', module = 'oslc4j-client', version = '4.0.0-SNAPSHOT')
@Grab('org.glassfish.jersey.core:jersey-client:2.25.1')
@Grab(group = 'org.slf4j', module = 'slf4j-simple', version = '1.7.25')

import org.eclipse.lyo.oslc4j.client.OslcClient
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper

println("Populating OSLC RefImpl servers with sample data.\n")

def client = new OslcClient()

def rm_spc = 'http://localhost:8800/services/catalog/singleton'

def rm_sp = client.lookupServiceProviderUrl(rm_spc, "Default ServiceProvider")
def response = client.getResource(rm_spc)
def rm_spc_entity = response.readEntity(ServiceProviderCatalog.class)

println("Fetched the ${rm_spc_entity.title}")
rm_spc_entity.serviceProviders.each {
  println("-> contains ${it.title}")
  def spResponse = client.getResource(it.about.toString())
  def serviceProvider = spResponse.readEntity(ServiceProvider.class)
  serviceProvider.services.each { s ->
    println("   -> contains a Service with:")
    s.queryCapabilities.each {
      println("      -> ${it.label}")
    }
    s.creationFactories.each {
      println("      -> ${it.label}")
    }
    s.selectionDialogs.each {
      println("      -> ${it.label}")
    }
    s.creationDialogs.each {
      println("      -> ${it.label}")
    }
  }
}
