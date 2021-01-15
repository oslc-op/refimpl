package co.oslc.refimpl.cm.gen.servlet;

import co.oslc.refimpl.cm.gen.CMConstants;
import co.oslc.refimpl.cm.gen.auth.AuthenticationApplication;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import javax.xml.namespace.QName;
import java.io.IOException;

@Provider
public class ExtendedPropWriteInterceptor implements WriterInterceptor {
    private static final QName OAUTH_REALM_NAME = new QName("http://jazz.net/xmlns/prod/jazz/jfs/1.0/", "oauthRealmName");
    private final Logger log = LoggerFactory.getLogger(ExtendedPropWriteInterceptor.class);

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        log.info("Interceptor called");
        final Object entity = context.getEntity();
        if (entity instanceof ServiceProviderCatalog) {
            final ServiceProviderCatalog catalog = (ServiceProviderCatalog) entity;
            catalog.getExtendedProperties().put(OAUTH_REALM_NAME, AuthenticationApplication.OAUTH_REALM);
            context.setEntity(catalog);
        }
        context.proceed();
    }
}
