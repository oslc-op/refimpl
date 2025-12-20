package co.oslc.refimpl.rm.gen.util;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * Utility methods for servlet handling, particularly for Undertow compatibility.
 */
public class ServletUtil {
    
    /**
     * Unwrap the request to get the original HttpServletRequest.
     * This is required for Undertow compatibility when forwarding requests.
     * 
     * Undertow validates that requests passed to RequestDispatcher.forward()
     * are either the original request or properly wrapped. JAX-RS injected
     * requests may not meet these criteria, so we unwrap to the original.
     * 
     * @param request the potentially wrapped request
     * @return the original unwrapped HttpServletRequest
     */
    public static HttpServletRequest unwrapRequest(HttpServletRequest request) {
        ServletRequest current = request;
        while (current instanceof HttpServletRequestWrapper) {
            current = ((HttpServletRequestWrapper) current).getRequest();
        }
        return (HttpServletRequest) current;
    }
}
