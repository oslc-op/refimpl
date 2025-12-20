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

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        String servletURI = OSLC4JUtils.getServletURI();
        String quarkusVersion = io.quarkus.runtime.Quarkus.class.getPackage().getImplementationVersion();
        if (quarkusVersion == null) {
            quarkusVersion = "3.17.4";
        }
        
        return index
            .data("servletURI", servletURI)
            .data("quarkusVersion", quarkusVersion);
    }
}
