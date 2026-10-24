package co.oslc.refimpl.rm.gen.servlet;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Home page resource for the RM OSLC Adaptor.
 * Serves the main index page using Qute template.
 */
@ApplicationScoped
@Path("/")
public class HomePageResource {

    @Inject
    Template index;

    @Context
    UriInfo uriInfo;
    
    @ConfigProperty(name = "app.version", defaultValue = "N/A")
    String projectVersion;
    
    @ConfigProperty(name = "app.lyo.version", defaultValue = "N/A")
    String lyoVersion;
    
    @ConfigProperty(name = "app.quarkus.version", defaultValue = "N/A")
    String quarkusVersion;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return index
            .data("projectVersion", projectVersion)
            .data("lyoVersion", lyoVersion)
            .data("quarkusVersion", quarkusVersion);
    }
}
