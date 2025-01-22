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

<%@page import="org.eclipse.lyo.oslc4j.core.model.ServiceProvider"%>
<%@page import="org.eclipse.lyo.oslc.domains.qm.TestPlan"%>
<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JUtils"%>
<%@page import="jakarta.ws.rs.core.UriBuilder"%>

<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>

<%
  String selectionUri = (String) request.getAttribute("selectionUri");

%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <title>TestPlanSD</title>
    <script src="<c:url value="/static/js/delegated-ui.js"/>"></script>
  </head>
  <body style="padding: 10px;">
    <div id="selector-body">
      <p id="searchMessage">Find a specific resource through a free-text search.</p>

      <p id="loadingMessage" style="display: none;">Pondering your search. Please stand by ...</p>

      <div>
        <input type="search" style="width: 335px" id="searchTerms" placeholder="Enter search terms" autofocus>
        <button type="button" onclick="search( '<%= selectionUri %>' )">Search</button>
      </div>

      <div style="margin-top: 5px;">
        <select id="results" size="10" style="width: 400px" multiple="multiple"></select>
      </div>

      <div style="width: 400px; margin-top: 5px;">
        <button style="float: right;" type="button"
          onclick="javascript: cancel()">Cancel</button>
        <button style="float: right;" type="button"
          onclick="javascript: select();">OK</button>
      </div>
      <div style="clear: both;"></div>
    </div>

  </body>
</html>
