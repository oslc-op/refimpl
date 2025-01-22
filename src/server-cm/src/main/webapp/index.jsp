<%--To avoid the overriding of any manual code changes upon subsequent code generations, disable "Generate JSP Files" option in the Adaptor model.--%>
<!DOCTYPE html>
<%--
 Copyright (c) 2022 Contributors to the Eclipse Foundation
 
 See the NOTICE file(s) distributed with this work for additional
 information regarding copyright ownership.
 
 This program and the accompanying materials are made available under the
 terms of the Eclipse Distribution License 1.0 which is available at
 http://www.eclipse.org/org/documents/edl-v10.php.
 
 SPDX-License-Identifier: BSD-3-Simple

 This file is generated by Lyo Designer (https://www.eclipse.org/lyo/)
--%>

<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JUtils"%>
<%@page import="jakarta.ws.rs.core.UriBuilder"%>
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

  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>
  <link href="<c:url value="/static/css/adaptor.css"/>" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="jumbotron">
        <h1 class="display-3"><%= application.getServletContextName() %></h1>
        <p class="lead">This is a homepage of the <em>CM</em> that was generated using
            Eclipse Lyo Toolchain Designer.</p>
        <hr class="my-4">
        <p class="lead">
            <a class="btn btn-primary btn-lg" href="<%= UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path("/catalog/singleton").build() %>" role="button">Service Provider
                Catalog</a>
        </p>
        <p>Start from the Service Provider Catalog, to navigate your adaptor's services and resources, using the available Query capabilities, Selection and Creation Dialogs. 
            Note that these are end-user HTML pages, which is very useful for debugging your adaptor.</p>
        <p class="lead">
            <a class="btn btn-primary btn-lg" href="<c:url value="/swagger-ui/index.jsp"/>" role="button">Interactive Swagger UI</a>
        </p>
        <p>Use Swagger UI To interact with the adaptor services dedicated for RDF.
        </p>
        <p>You can also copy <a href="<%= UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path("/openapi.yaml").build() %>">this OpenAPI specification document (yaml file) of this adaptor</a> to a <a href="<%= "https://editor.swagger.io" %>">Swagger Editor</a> to generate client SDK code for a number of languages and platforms.
        </p>
    </div>
</div>
</body>
</html>
