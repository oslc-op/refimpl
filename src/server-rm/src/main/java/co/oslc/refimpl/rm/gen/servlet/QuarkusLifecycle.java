package co.oslc.refimpl.rm.gen.servlet;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jena.sys.JenaSystem;

import java.net.MalformedURLException;

@Singleton
public class QuarkusLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(QuarkusLifecycle.class);

    @Inject
    LyoAppConfiguration config;

    void onStart(@Observes StartupEvent ev) {
        logger.info("Initializing QuarkusLifecycle...");

        JenaSystem.init();
        OSLC4JUtils.setLyoStorePagingPreciseLimit(false);

        String baseUrl = config.baseUrl();
        String servletUrlPattern = config.servletPath();

        try {
            logger.info("Setting public URI: " + baseUrl);
            OSLC4JUtils.setPublicURI(baseUrl);
            logger.info("Setting servlet path: " + servletUrlPattern);
            OSLC4JUtils.setServletPath(servletUrlPattern);
        } catch (MalformedURLException e) {
            logger.error("QuarkusLifecycle encountered MalformedURLException.", e);
        } catch (IllegalArgumentException e) {
            logger.error("QuarkusLifecycle encountered IllegalArgumentException.", e);
        }
    }
}
