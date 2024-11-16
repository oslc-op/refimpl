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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="jakarta.ws.rs.core.UriBuilder"%>
<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JUtils"%>
<%@page import="org.eclipse.lyo.oslc4j.core.model.ServiceProvider"%>
<%@page import="org.eclipse.lyo.oslc4j.core.model.AbstractResource"%>
<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JConstants"%>
<%@page import="java.util.List" %>

<%@page import="org.eclipse.lyo.oslc.domains.rm.Requirement"%>
<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>
<%
  List<Requirement> resources = (List<Requirement>) request.getAttribute("resources");
  String queryUri = (String)request.getAttribute("queryUri");
  String nextPageUri = (String)request.getAttribute(OSLC4JConstants.OSLC4J_NEXT_PAGE);
  String catalogUrl = UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path("/catalog/singleton").build().toString();
%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Query capability RequirementQC</title>

  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>
  <link href="<c:url value="/static/css/adaptor.css"/>" rel="stylesheet">
  <script src="<c:url value="/static/js/ui-preview-helper.js"/>"></script>
  <script type="text/javascript">
    $(function () {setupUiPreviewOnPopover($("a.oslc-resource-link"));});
  </script>

</head>
<body>
<!-- Fixed navbar -->
  <nav class="navbar navbar-expand-lg sticky-top navbar-light bg-light">
    <div class="container">
      <a class="navbar-brand" href="<c:url value="/"/>"><%= application.getServletContextName() %></a>
      <ul class="navbar-nav mr-auto">
        <li class="nav-item"><a class="nav-link" href="<c:url value="<%= catalogUrl %>"/>">Service Provider Catalog</a></li>
        <li class="nav-item"><a class="nav-link" href="<c:url value="/swagger-ui/index.jsp"/>">Swagger UI</a></li>
      </ul>
    </div>
  </nav>
  <!-- Begin page content -->
  <div class="container">
    <div class="page-header">
      <h1>Query Capability &quot;RequirementQC&quot; results</h1>
      <div class="alert alert-secondary" role="alert">
          Showing&nbsp;${resources.size()} resources on this page
          <% if (nextPageUri != null) { %><p><a href="<%= nextPageUri %>">Next Page</a></p><% } %>
      </div>
    </div>
        <c:forEach items="${resources}" var="res">
          <div class="card mb-3">
            <div class="card-body">
              <a href="${fn:escapeXml(res.getAbout())}" class="oslc-resource-link">${fn:escapeXml(res.toString())}</a>
            </div>
          </div>
        </c:forEach>
      </div>
  <footer class="footer">
      <div class="container">
          <p class="text-muted">OSLC Adaptor was generated using <a href="http://eclipse.org/lyo">Eclipse Lyo</a>.</p>
      </div>
  </footer>
</body>
</html>
