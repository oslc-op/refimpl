package co.oslc.refimpl.lib;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;

import java.util.List;

public interface ResourceRepository <R extends AbstractResource> {
    List<R> fetchResourcesForSP(String serviceProvider);
    List<R> fetchResourcePageForSP(String serviceProvider, int page, int pageSize);
    void addResource(String serviceProvider, String id, R resource);
    R getResource(String serviceProvider, String id);
    void deleteResource(String serviceProvider, String id);
    boolean hasResource(String serviceProvider, String id);
    String calculateETag(R resource);

    List<R> findResources(String serviceProvider, String terms, int limit);

    void updateResource(String spDefault, String id, R resource);
}
