// Start of user code Copyright
/*******************************************************************************
 * Copyright (c) 2017 Jad El-khoury.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *
 *     Jad El-khoury        - initial implementation
 *     
 *******************************************************************************/
// End of user code

package co.oslc.refimpl.rm.gen;

// Start of user code Notice
//Note: The Lyo code generator is migrating the name of this class from 'RMResourcesFactory' to the new shorter name 'ResourcesFactory'.
//You are still using the old name. The generator will continue to use this old name until you actively trigger the change.
//To migrate to the new class name:
//1. Rename your class to ResourcesFactory 
//    * Please rename and do not simply create a copy of the file. The generator needs to detect the file deletion in order to activate the name change.
//2. Regenerate the code. 
//    * The generator will generate this class with the new name.
//    * Besides the class name, the code - including the user clode blocks - remain intact.
//    * All other class references to the new class name are updated.
//3. Delete this notice
// End of user code

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.UriBuilder;

import org.eclipse.lyo.oslc4j.core.model.Link;
import org.eclipse.lyo.oslc4j.core.OSLC4JUtils;
import org.eclipse.lyo.oslc.domains.Person;
import org.eclipse.lyo.oslc.domains.rm.Requirement;
import org.eclipse.lyo.oslc.domains.rm.RequirementCollection;

// Start of user code imports
// End of user code

// Start of user code pre_class_code
// End of user code

public class RMResourcesFactory {

    // Start of user code class_attributes
    // End of user code
    
    // Start of user code class_methods
    // End of user code

    //methods for Requirement resource
    
    public static Requirement createRequirement(final String serviceProviderId, final String resourceId)
    {
        return new Requirement(constructURIForRequirement(serviceProviderId, resourceId));
    }
    
    public static URI constructURIForRequirement(final String serviceProviderId, final String resourceId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("resourceId", resourceId);
        String instanceURI = "Requirement/{serviceProviderId}/{resourceId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForRequirement(final String serviceProviderId, final String resourceId , final String label)
    {
        return new Link(constructURIForRequirement(serviceProviderId, resourceId), label);
    }
    
    public static Link constructLinkForRequirement(final String serviceProviderId, final String resourceId)
    {
        return new Link(constructURIForRequirement(serviceProviderId, resourceId));
    }
    

    //methods for RequirementCollection resource
    
    public static RequirementCollection createRequirementCollection(final String serviceProviderId, final String resourceId)
    {
        return new RequirementCollection(constructURIForRequirementCollection(serviceProviderId, resourceId));
    }
    
    public static URI constructURIForRequirementCollection(final String serviceProviderId, final String resourceId)
    {
        String basePath = OSLC4JUtils.getServletURI();
        Map<String, Object> pathParameters = new HashMap<String, Object>();
        pathParameters.put("serviceProviderId", serviceProviderId);
        pathParameters.put("resourceId", resourceId);
        String instanceURI = "RequirementCollection/{serviceProviderId}/{resourceId}";
    
        final UriBuilder builder = UriBuilder.fromUri(basePath);
        return builder.path(instanceURI).buildFromMap(pathParameters);
    }
    
    public static Link constructLinkForRequirementCollection(final String serviceProviderId, final String resourceId , final String label)
    {
        return new Link(constructURIForRequirementCollection(serviceProviderId, resourceId), label);
    }
    
    public static Link constructLinkForRequirementCollection(final String serviceProviderId, final String resourceId)
    {
        return new Link(constructURIForRequirementCollection(serviceProviderId, resourceId));
    }
    

}
