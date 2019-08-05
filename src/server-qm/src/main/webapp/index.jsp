<!DOCTYPE html>
<%--Start of user code "Copyright"
--%>
<%--
 Copyright (c) 2017 KTH Royal Institute of Technology.

 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 and Eclipse Distribution License v. 1.0 which accompanies this distribution.

 The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 and the Eclipse Distribution License is available at
 http://www.eclipse.org/org/documents/edl-v10.php.

 Contributors:

  Andrew Berezovskyi - initial implementation

 This file is generated by org.eclipse.lyo.oslc4j.codegenerator
--%>
<%--End of user code--%>

<%--
Start of user code "indexBody"
--%>
<%--TODO: Replace/adjust this default content as necessary.
All manual changes in this "protected" user code area will NOT be overwritten upon subsequent code generations.
To revert to the default generated content, delete all content in this file, and then re-generate.
--%>
<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JUtils"%>
<%@page import="javax.ws.rs.core.UriBuilder"%>
<%@page import="java.net.URI"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Latest compiled and minified CSS -->
    <title>Adaptor home</title>

    <link href="<c:url value="/static/css/bootstrap-4.0.0-beta.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/static/css/adaptor.css"/>" rel="stylesheet">

    <script src="<c:url value="/static/js/jquery-3.2.1.min.js"/>"></script>
    <script src="<c:url value="/static/js/bootstrap-4.0.0-beta.min.js"/>"></script>
</head>
<body>
<div class="container">
    <div class="jumbotron">
        <h1 class="display-3"><%= application.getServletContextName() %></h1>
        <p class="lead">This is a homepage of the <em>QM</em> that was generated using
            Eclipse Lyo Toolchain Designer.</p>
        <hr class="my-4">
        <p class="lead">
            <a class="btn btn-primary btn-lg" href="<%= UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path("/catalog/singleton").build() %>" role="button">Service Provider
                Catalog</a>
        </p>
        <p>Start from the Service Provider Catalog, to navigate your adaptor's services and resources, using the available Query capabilities, Selection and Creation Dialogs. 
            Note that these are end-user HTML pages, which is very useful for debugging your adaptor.</p>
        
    </div>
    <div class="alert alert-primary" role="alert">
        <h4 class="alert-heading">Sample front page!</h4>
        <hr>
        <p>This initially generated sample front page can be modified, by changing the file <code>../src/server-qm/src/main/webapp/index.jsp</code></p>
        <ul>
        <li>Any changes made will not be overriden upon future re-generations.</li>
        <li>If you need to revert to the originally generated content, delete the file and/or its complete content.</li>
        </ul>
        <p><strong>You enabled Swagger Documentation?</strong>
            If you have enabled Swagger Documentation in the generated code, make sure you have configured your project according to these 
            <a href="<%= "https://wiki.eclipse.org/Lyo/creating_OSLC4J_project#OpenApi.2FSwagger_Support_.28Optional.29" %>">Lyo Instructions for Swagger</a>
        </p>
    </div>
</div>
</body>
</html>
<%--
End of user code
--%>
