package co.oslc.refimpl.cm.gen.servlet;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Allows OSLC requests to be made to the server from the web browser from another domain.
 */
@Provider
public class OslcCorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        String originAllow = "*";
        final String requestOrigin = requestContext.getHeaderString("Origin");
        if (requestOrigin != null) {
            //needed to enable Access-Control-Allow-Credentials
            originAllow = requestOrigin;
        }
        responseContext.getHeaders().add("Access-Control-Allow-Origin", originAllow);
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add(
            "Access-Control-Allow-Headers",
            "origin, content-type, accept, authorization, oslc-core-version, Configuration-Context, OSLC-Configuration-Context"
        );
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}