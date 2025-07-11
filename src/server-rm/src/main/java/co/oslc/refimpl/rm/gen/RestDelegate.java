// Start of user code Copyright
/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *
 *  Contributors:
 *
 *	   Sam Padgett	       - initial API and implementation
 *     Michael Fiedler     - adapted for OSLC4J
 *     Jad El-khoury        - initial implementation of code generator (https://bugs.eclipse.org/bugs/show_bug.cgi?id=422448)
 *     Matthieu Helleboid   - Support for multiple Service Providers.
 *     Anass Radouani       - Support for multiple Service Providers.
 *
 * This file is generated by org.eclipse.lyo.oslc4j.codegenerator
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.rm.gen;


// spotless:off
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletContextEvent;
import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import co.oslc.refimpl.rm.gen.servlet.ServiceProviderCatalogSingleton;
import co.oslc.refimpl.rm.gen.ServiceProviderInfo;
import org.eclipse.lyo.oslc.domains.Person;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc.domains.rm.RequirementCollection;



// Start of user code imports
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// End of user code
// spotless:on

// Start of user code pre_class_code
// End of user code

public class RestDelegate {

    private static final Logger log = LoggerFactory.getLogger(RestDelegate.class);

    
    
    @Inject ResourcesFactory resourcesFactory;
    // Start of user code class_attributes
    public static final String SP_DEFAULT = "sp_single";

    private final static Map<String, Map<String, Requirement>> requirements = new HashMap<>();
    private final static Map<String, Map<String, RequirementCollection>> requirementCollections = new HashMap<>();
    private final static Directory searchIndex = new ByteBuffersDirectory();
    private static final StandardAnalyzer indexAnalyser = new StandardAnalyzer();
    private static final Executor indexer = Executors.newSingleThreadExecutor();
    // End of user code
    
    public RestDelegate() {
        log.trace("Delegate is initialized");
    }
    
    
    // Start of user code class_methods
    public static Map<String, Requirement> requirementsForSP(String serviceProvider) {
        if (!requirements.containsKey(serviceProvider)) {
            synchronized (requirements) {
                if (!requirements.containsKey(serviceProvider)) {
                    requirements.put(serviceProvider, Collections.synchronizedMap(new LinkedHashMap<>()));
                }
            }
        }
        return requirements.get(serviceProvider);
    }

    // TODO Andrew@2019-09-24: refactor
    public static Map<String, RequirementCollection> requirementCollectionsForSP(String serviceProvider) {
        if (!requirementCollections.containsKey(serviceProvider)) {
            synchronized (requirementCollections) {
                if (!requirementCollections.containsKey(serviceProvider)) {
                    requirementCollections.put(serviceProvider, Collections.synchronizedMap(new LinkedHashMap<>()));
                }
            }
        }
        return requirementCollections.get(serviceProvider);
    }


    private static void addToIndex(final Requirement requirement, final String serviceProviderId) {
        indexer.execute(() -> {
            try(IndexWriter indexWriter = getIndexWriter()) {
                indexWriter.addDocument(documentFor(requirement, serviceProviderId));
                log.debug("Indexed {}", requirement.getAbout().toString());
            } catch (IOException e) {
                log.warn("Error with Lucene index");
            }
        });
    }

    private static Document documentFor(final Requirement requirement, final String serviceProviderId) {
        Document document = new Document();

        document.add(new TextField("service_provider", serviceProviderId, Field.Store.YES));
//        document.add(new TextField("title", requirement.getTitle(), Field.Store.YES));
        document.add(new TextField("id", requirement.getIdentifier(), Field.Store.YES));
        final StringBuilder sb = new StringBuilder();
        sb.append(requirement.getIdentifier()).append(" ")
            .append(requirement.getShortTitle()).append(" ")
            .append(requirement.getTitle()).append(" ")
            .append(requirement.getDescription()).append(" ")
            .append(String.join(" ", requirement.getSubject().toArray(new String[0]))).append(" ");
        document.add(new TextField("body", sb.toString(), Field.Store.NO));

        return document;
    }

    private static IndexWriter getIndexWriter() {
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(indexAnalyser);
            return new IndexWriter(searchIndex, indexWriterConfig);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    // End of user code

    //The methods contextInitializeServletListener() and contextDestroyServletListener() no longer exits
    //Migrate any user-specific code blocks to the class co.oslc.refimpl.rm.gen.servlet.ServletListener
    //Any user-specific code should be found in *.lost files.

    public static ServiceProviderInfo[] getServiceProviderInfos(HttpServletRequest httpServletRequest)
    {
        ServiceProviderInfo[] serviceProviderInfos = {};
        
        // Start of user code "ServiceProviderInfo[] getServiceProviderInfos(...)"
        ServiceProviderInfo spInfo = new ServiceProviderInfo();
        spInfo.serviceProviderId = SP_DEFAULT;
        spInfo.name = "Default ServiceProvider";
        serviceProviderInfos = new ServiceProviderInfo[] {spInfo};
        // End of user code
        return serviceProviderInfos;
    }

    public List<Requirement> queryRequirements(HttpServletRequest httpServletRequest, final String serviceProviderId, String where, String prefix, boolean paging, int page, int limit)
    {
        List<Requirement> resources = null;
        
        
        // Start of user code queryRequirements
        final Map<String, Requirement> requirements = requirementsForSP(serviceProviderId);
        // page starts from 0
        resources = requirements.values()
                .stream()
                .skip((page) * limit)
                .limit(limit + 1)
                .collect(Collectors.toList());
        // End of user code
        return resources;
    }
    public List<Requirement> RequirementSelector(HttpServletRequest httpServletRequest, final String serviceProviderId, String terms)
    {
        List<Requirement> resources = null;
        
        
        // Start of user code RequirementSelector
        final Map<String, Requirement> requirements = requirementsForSP(serviceProviderId);
        try {
            Query termsQuery = new QueryParser("body", indexAnalyser).parse(terms);
            final BooleanQuery query = new BooleanQuery.Builder()
                    .add(new TermQuery(new Term("service_provider", serviceProviderId)),
                            BooleanClause.Occur.MUST)
                    .add(termsQuery, BooleanClause.Occur.SHOULD)
                    .build();

            IndexReader indexReader = DirectoryReader.open(searchIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 100);
            List<Requirement> selectedRequirements = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                final String id = searcher.doc(scoreDoc.doc).getField("id").stringValue();
                selectedRequirements.add(requirements.get(id));
            }
            return selectedRequirements;
        } catch (ParseException e) {
            log.debug("Wrong query: {}", terms);
        } catch (IOException e) {
            log.warn("Lucene index error");
        }
        // End of user code
        return resources;
    }
    public Requirement createRequirement(HttpServletRequest httpServletRequest, final Requirement aResource, final String serviceProviderId)
    {
        Requirement newResource = null;
        
        
        // Start of user code createRequirement
        final Map<String, Requirement> requirements = requirementsForSP(serviceProviderId);
        aResource.setAbout(resourcesFactory.constructURIForRequirement(serviceProviderId, aResource.getIdentifier()));
        aResource.setCreated(new Date());
        requirements.put(aResource.getIdentifier(), aResource);
        log.info("Created {}", aResource.getShortTitle());
        newResource = aResource;
        addToIndex(newResource, serviceProviderId);
        // End of user code
        return newResource;
    }

    public Requirement createRequirementFromDialog(HttpServletRequest httpServletRequest, final Requirement aResource, final String serviceProviderId)
    {
        Requirement newResource = null;
        
        
        // Start of user code createRequirementFromDialog
        // TODO Implement code to create a resource
        // End of user code
        return newResource;
    }


    public List<RequirementCollection> queryRequirementCollections(HttpServletRequest httpServletRequest, final String serviceProviderId, String where, String prefix, boolean paging, int page, int limit)
    {
        List<RequirementCollection> resources = null;
        
        
        // Start of user code queryRequirementCollections
        final Map<String, RequirementCollection> repository = requirementCollectionsForSP(serviceProviderId);
        // page starts from 0
        resources = repository.values()
                .stream()
                .skip((page) * limit)
                .limit(limit + 1)
                .collect(Collectors.toList());
        // End of user code
        return resources;
    }
    public List<RequirementCollection> RequirementCollectionSelector(HttpServletRequest httpServletRequest, final String serviceProviderId, String terms)
    {
        List<RequirementCollection> resources = null;
        
        
        // Start of user code RequirementCollectionSelector
        final Map<String, RequirementCollection> repository = requirementCollectionsForSP(serviceProviderId);
        resources = repository.values()
                .stream()
                .filter(requirement -> requirement.toString().contains(terms))
                .collect(Collectors.toList());
        // End of user code
        return resources;
    }
    public RequirementCollection createRequirementCollection(HttpServletRequest httpServletRequest, final RequirementCollection aResource, final String serviceProviderId)
    {
        RequirementCollection newResource = null;
        
        
        // Start of user code createRequirementCollection
        if(aResource == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        final Map<String, RequirementCollection> resources = requirementCollectionsForSP(serviceProviderId);
        aResource.setAbout(resourcesFactory.constructURIForRequirementCollection(serviceProviderId, aResource.getIdentifier()));
        aResource.setCreated(new Date());
        resources.put(aResource.getIdentifier(), aResource);
        log.info("Created {}", aResource.getShortTitle());
        newResource = aResource;
        // TODO Andrew@2019-09-24: add to index
//        addToIndex(newResource, serviceProviderId);
        // End of user code
        return newResource;
    }




    public Requirement getRequirement(HttpServletRequest httpServletRequest, final String serviceProviderId, final String resourceId)
    {
        Requirement aResource = null;
        
        
        // Start of user code getRequirement
        final Map<String, Requirement> requirements = requirementsForSP(serviceProviderId);
        aResource = requirements.get(resourceId);
        // End of user code
        return aResource;
    }
    public RequirementCollection getRequirementCollection(HttpServletRequest httpServletRequest, final String serviceProviderId, final String resourceId)
    {
        RequirementCollection aResource = null;
        
        
        // Start of user code getRequirementCollection
        // TODO Andrew@2019-09-24: add delete/update capability
        final Map<String, RequirementCollection> resources = requirementCollectionsForSP(serviceProviderId);
        aResource = resources.get(resourceId);
        // End of user code
        return aResource;
    }

    public Boolean deleteRequirement(HttpServletRequest httpServletRequest, final String serviceProviderId, final String resourceId)
    {
        Boolean deleted = false;
        
        // Start of user code deleteRequirement
        final Map<String, Requirement> requirements = requirementsForSP(serviceProviderId);
        deleted = requirements.remove(resourceId) != null;
        // End of user code
        return deleted;
    }
    public Boolean deleteRequirementCollection(HttpServletRequest httpServletRequest, final String serviceProviderId, final String resourceId)
    {
        Boolean deleted = false;
        
        // Start of user code deleteRequirementCollection
        final Map<String, RequirementCollection> repository = requirementCollectionsForSP(serviceProviderId);
        deleted = repository.remove(resourceId) != null;
        // End of user code
        return deleted;
    }

    public Requirement updateRequirement(HttpServletRequest httpServletRequest, final Requirement aResource, final String serviceProviderId, final String resourceId) {
        Requirement updatedResource = null;
        
        // Start of user code updateRequirement
        if(!resourcesFactory.constructURIForRequirement(serviceProviderId, resourceId).equals(aResource.getAbout())) {
            throw new WebApplicationException("Subject URI shall match the endpoint", Response.Status.BAD_REQUEST);
        }
        aResource.setModified(new Date());
        final Map<String, Requirement> repository = requirementsForSP(serviceProviderId);
        repository.put(resourceId, aResource);
        updatedResource = aResource;
        // End of user code
        return updatedResource;
    }
    public RequirementCollection updateRequirementCollection(HttpServletRequest httpServletRequest, final RequirementCollection aResource, final String serviceProviderId, final String resourceId) {
        RequirementCollection updatedResource = null;
        
        // Start of user code updateRequirementCollection
        if(!resourcesFactory.constructURIForRequirementCollection(serviceProviderId, resourceId).equals(aResource.getAbout())) {
            throw new WebApplicationException("Subject URI shall match the endpoint", Response.Status.BAD_REQUEST);
        }
        aResource.setModified(new Date());
        final Map<String, RequirementCollection> repository = requirementCollectionsForSP(serviceProviderId);
        repository.put(resourceId, aResource);
        updatedResource = aResource;
        // End of user code
        return updatedResource;
    }

    public String getETagFromRequirement(final Requirement aResource)
    {
        String eTag = null;
        // Start of user code getETagFromRequirement
        // TODO Implement code to return an ETag for a particular resource
        // End of user code
        return eTag;
    }
    public String getETagFromRequirementCollection(final RequirementCollection aResource)
    {
        String eTag = null;
        // Start of user code getETagFromRequirementCollection
        // TODO Implement code to return an ETag for a particular resource
        // End of user code
        return eTag;
    }

}
