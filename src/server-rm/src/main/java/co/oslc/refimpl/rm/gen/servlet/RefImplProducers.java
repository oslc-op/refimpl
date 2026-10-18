package co.oslc.refimpl.rm.gen.servlet;

import co.oslc.refimpl.rm.gen.ResourcesFactory;
import co.oslc.refimpl.rm.gen.RestDelegate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.trs.server.InmemPagedTrs;
import org.eclipse.lyo.oslc4j.trs.server.PagedTrs;
import org.eclipse.lyo.oslc4j.trs.server.PagedTrsFactory;
import org.eclipse.lyo.oslc4j.trs.server.TrsEventHandler;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc.domains.rm.RequirementCollection;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class RefImplProducers {

    @ConfigProperty(name = "co.oslc.refimpl.rm.gen.servlet.baseurl", defaultValue = "http://localhost:8800/")
    String baseUrl;

    @ConfigProperty(name = "co.oslc.refimpl.rm.gen.servlet.cors.friends", defaultValue = "*")
    String corsFriends;

    @Produces
    @Singleton
    public LyoAppConfiguration produceConfiguration() {
        Set<String> friends;
        if (corsFriends == null || corsFriends.isEmpty()) {
            friends = Collections.emptySet();
        } else {
            friends = Arrays.stream(corsFriends.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());
        }
        return new LyoAppConfiguration(baseUrl, "/", friends);
    }

    @Produces
    @Singleton
    public ResourcesFactory produceResourcesFactory() {
        return new ResourcesFactory(OSLC4JUtils.getServletURI());
    }

    @Produces
    @Singleton
    public TrsEventHandler produceTrsEventHandler() {
        ArrayList<URI> uris = new ArrayList<>();
        Map<String, Requirement> requirements = RestDelegate.requirementsForSP(RestDelegate.SP_DEFAULT);
        for (Requirement requirement : requirements.values()) {
            uris.add(requirement.getAbout());
        }
        Map<String, RequirementCollection> requirementCollections = RestDelegate.requirementCollectionsForSP(RestDelegate.SP_DEFAULT);
        for (RequirementCollection requirementCollection : requirementCollections.values()) {
            uris.add(requirementCollection.getAbout());
        }

        InmemPagedTrs inmemTrs = new PagedTrsFactory().getInmemPagedTrs(5, 5, uris);
        return inmemTrs;
    }

    @Produces
    @Singleton
    public PagedTrs producePagedTrs(TrsEventHandler trsEventHandler) {
        return (InmemPagedTrs) trsEventHandler;
    }
}
