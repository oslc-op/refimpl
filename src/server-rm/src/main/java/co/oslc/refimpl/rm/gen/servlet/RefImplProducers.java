package co.oslc.refimpl.rm.gen.servlet;

import co.oslc.refimpl.rm.gen.ResourcesFactory;
import co.oslc.refimpl.rm.gen.RestDelegate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Arrays;
import java.util.Collections;
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
        return new LyoAppConfiguration(baseUrl, "services", friends);
    }

    @Produces
    @Singleton
    public ResourcesFactory produceResourcesFactory() {
        return new ResourcesFactory(OSLC4JUtils.getServletURI());
    }
}
