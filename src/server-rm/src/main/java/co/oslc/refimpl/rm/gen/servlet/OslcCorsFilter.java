package co.oslc.refimpl.rm.gen.servlet;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Allows OSLC requests to be made to the server from the web browser from another domain.
 */
@Provider
public class OslcCorsFilter implements ContainerResponseFilter {
//    
    @Inject
    private LyoGeneratedAppConfig config;

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        if (config.corsFriends().isEmpty()) {
            return;
        }
        
        String originAllow = "*";
        final String requestOrigin = requestContext.getHeaderString("Origin");
        if (requestOrigin != null) {
            if (!config.corsFriends().contains("*") && !config.corsFriends().contains(requestOrigin) ) {
                return;
            }
            originAllow = requestOrigin;
        } else {
            if (!config.corsFriends().contains("*")) {
                return;
            }
        }
        responseContext.getHeaders().add(
            "Access-Control-Allow-Origin", originAllow);
        responseContext.getHeaders().add(
            "Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add(
            "Access-Control-Allow-Headers",
            "origin, content-type, accept, authorization, oslc-core-version, Configuration-Context, OSLC-Configuration-Context");
        responseContext.getHeaders().add(
            "Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
