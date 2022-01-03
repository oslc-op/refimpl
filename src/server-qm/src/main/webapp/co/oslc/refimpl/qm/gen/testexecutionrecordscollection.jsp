<%--To avoid the overriding of any manual code changes upon subsequent code generations, disable "Generate JSP Files" option in the Adaptor model.--%>
<!DOCTYPE html>
<%--
 Copyright (c) 2020 Contributors to the Eclipse Foundation
 
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

<%@page import="javax.ws.rs.core.UriBuilder"%>
<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JUtils"%>
<%@page import="org.eclipse.lyo.oslc4j.core.model.ServiceProvider"%>
<%@page import="org.eclipse.lyo.oslc4j.core.model.AbstractResource"%>
<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JConstants"%>
<%@page import="java.util.List" %>

<%@page import="org.eclipse.lyo.oslc.domains.qm.TestExecutionRecord"%>
<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>
<%
  List<TestExecutionRecord> resources = (List<TestExecutionRecord>) request.getAttribute("resources");
  String queryUri = (String)request.getAttribute("queryUri");
  String nextPageUri = (String)request.getAttribute(OSLC4JConstants.OSLC4J_NEXT_PAGE);
  String catalogUrl = UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path("/catalog/singleton").build().toString();
%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Query capability TestExecutionRecordQC</title>

  <link href="<c:url value="/static/css/bootstrap-4.0.0-beta.min.css"/>" rel="stylesheet">
  <link href="<c:url value="/static/css/adaptor.css"/>" rel="stylesheet">

  <script src="<c:url value="/static/js/jquery-3.2.1.min.js"/>"></script>
  <script src="<c:url value="/static/js/popper-1.11.0.min.js"/>"></script>
  <script src="<c:url value="/static/js/bootstrap-4.0.0-beta.min.js"/>"></script>
  <script src="<c:url value="/static/js/ui-preview-helper.js"/>"></script>
  <script type="text/javascript">
    $(function () {setupUiPreviewOnPopover($("a.oslc-resource-link"));});
  </script>

</head>
<body>
<!-- Fixed navbar -->
  <nav class="navbar navbar-expand-lg sticky-top navbar-light bg-light">
    <div class="container">
      <ul class="navbar-nav mr-auto">
        <li class="nav-item"><a class="nav-link" href="<c:url value="/"/>"><%= application.getServletContextName() %></a></li>
        <li class="nav-item"><a class="nav-link" href="<c:url value="<%= catalogUrl %>"/>">Service Provider Catalog</a></li>
        <li class="nav-item"><a class="nav-link" href="<c:url value="/swagger-ui/index.jsp"/>">Swagger UI</a></li>
      </ul>
    </div>
  </nav>
  <!-- Begin page content -->
  <div class="container">
    <div class="page-header">
      <h1>Query Capability &quot;TestExecutionRecordQC&quot; results</h1>
      <div class="alert alert-secondary" role="alert">
          Number of elements:&nbsp;
          <%= resources.size()%>
          <% if (nextPageUri != null) { %><p><a href="<%= nextPageUri %>">Next Page</a></p><% } %>
      </div>
    </div>
        <% for (TestExecutionRecord aResource : resources) { %>
        <p><a href="<%= aResource.getAbout() %>" class="oslc-resource-link"><%=aResource.toString()%></a><br /></p>
        <% } %>
      </div>
  <footer class="footer">
      <div class="container">
          <p class="text-muted">OSLC Adaptor was generated using <a href="http://eclipse.org/lyo">Eclipse Lyo</a>.</p>
      </div>
  </footer>
</body>
</html>
