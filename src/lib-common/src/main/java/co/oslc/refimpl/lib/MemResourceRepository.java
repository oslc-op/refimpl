package co.oslc.refimpl.lib;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.provider.jena.JenaModelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple Lyo resource repository that stores resources in memory and selects them by string id.
 *
 * @param <R>
 *         Generic resource type
 */
public class MemResourceRepository<R extends AbstractResource> implements ResourceRepository<R> {

    private final Logger log = LoggerFactory.getLogger(MemResourceRepository.class);


    private final Map<String, Map<String, R>> resources = new HashMap<>();

    @Override
    public List<R> fetchResourcesForSP(String serviceProvider) {
        return new ArrayList<>(resources.getOrDefault(serviceProvider, new HashMap<>()).values());
    }

    /**
     * @param serviceProvider
     * @param page
     *         0-indexed
     * @param pageSize
     *         pageSize+1 results will be returned if this is not the last page
     * @return
     */
    @Override
    public List<R> fetchResourcePageForSP(String serviceProvider, int page, int pageSize) {
        Map<String, R> rs = resources.getOrDefault(serviceProvider, new HashMap<>());
        return rs.values().stream().skip((page) * pageSize)
                .limit(pageSize + 1).collect(Collectors.toList());
    }

    @Override
    public void addResource(String serviceProvider, String id, R resource) {
        synchronized (resources) {
            if (!resources.containsKey(serviceProvider)) {
                resources.put(serviceProvider, new HashMap<>());
            }
        }
        resources.get(serviceProvider).put(id, resource);
    }

    @Override
    public R getResource(String serviceProvider, String id) {
        Map<String, R> rMap = resources.get(serviceProvider);
        if (rMap == null) {
            throw new NoSuchElementException("Service Provider does not exist");
        }
        R resource = rMap.get(id);
        if (resource == null) {
            throw new NoSuchElementException("Resource does not exist under a given SP");
        }
        return resource;
    }

    @Override
    public void deleteResource(String serviceProvider, String id) {
        Map<String, R> rMap = resources.get(serviceProvider);
        if (rMap == null) {
            throw new NoSuchElementException("Service Provider does not exist");
        }
        R resource = rMap.remove(id);
        if (resource == null) {
            throw new NoSuchElementException("Resource does not exist under a given SP");
        }
    }

    @Override
    public boolean hasResource(String serviceProvider, String id) {
        Map<String, R> rMap = resources.get(serviceProvider);
        if (rMap == null) {
            return false;
        }
        R resource = rMap.get(id);
        return resource != null;
    }

    private String md5(String rdf) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(rdf.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String calculateETag(R resource) {
        StringWriter writer = new StringWriter(1000);
        try {
            // TODO: 2020-10-31 consider caching this value to reduce computational cost
            Model resourceModel = JenaModelHelper.createJenaModel(new AbstractResource[]{resource});
            RDFDataMgr.write(writer, resourceModel, RDFFormat.NTRIPLES);
            // MD5 is insecure but this use is not security-related
            return md5(writer.toString());
        } catch (DatatypeConfigurationException | IllegalAccessException |
                InvocationTargetException | OslcCoreApplicationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public List<R> findResources(String serviceProvider, String terms, int limit) {
        return fetchResourcePageForSP(serviceProvider, 0, limit)
                .stream()
                .filter(r -> r.toString().toLowerCase(Locale.ROOT).contains(terms.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    @Override
    public void updateResource(String provider, String id, R resource) {
        if(hasResource(provider, id)) {
            resources.get(provider).put(id, resource);
        }
        else {
            log.error("Resource {}/{} not found and cannot be updated", provider, id);
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }
}
