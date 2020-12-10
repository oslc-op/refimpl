package co.oslc.refimpl.cm.gen.servlet;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Allows Delegated UI from the server to be loaded on another domain.
 */
@Provider
public class OslcCspFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Content-Security-Policy", "frame-ancestors 'self' *");
    }
}