package co.oslc.refimpl.rm.gen.servlet;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;

import jakarta.inject.Inject;

@Path("/")
public class HomeResource {

    @Inject
    Template index;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return index.data("servletURI", OSLC4JUtils.getServletURI());
    }
}
