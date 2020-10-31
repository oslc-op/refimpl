package co.oslc.refimpl.lib;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple Lyo resource repository that stores resources in memory and selects them by string id.
 *
 * @param <R>
 *         Generic resource type
 */
public class MemResourceRepository<R extends AbstractResource> implements ResourceRepository<R> {

    private final Map<String, Map<String, R>> resources = new HashMap<>();

    @Override
    public List<R> fetchResourcesForSP(String serviceProvider) {
        return new ArrayList<>(resources.getOrDefault(serviceProvider, new HashMap<>()).values());
    }

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
}
