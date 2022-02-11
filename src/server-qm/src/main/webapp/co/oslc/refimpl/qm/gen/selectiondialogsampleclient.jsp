<%--To avoid the overriding of any manual code changes upon subsequent code generations, disable "Generate JSP Files" option in the Adaptor model.--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<%@page import="javax.ws.rs.core.UriBuilder"%>
<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JUtils"%>
<%@page import="org.eclipse.lyo.oslc4j.core.model.ServiceProvider" %>
<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>
<%
  String selectionDialogUri = request.getParameter("selectionUri");
  selectionDialogUri += "#oslc-core-postMessage-1.0";
  String catalogUrl = UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path("/catalog/singleton").build().toString();
%>
<html>
<head>
  <title>Selection Dialog sample client</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js" integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k" crossorigin="anonymous"></script>
  <link href="<c:url value="/static/css/adaptor.css"/>" rel="stylesheet">

  <script src="<c:url value="/static/js/delegated-ui-helper.js"/>"></script>
  <script src="<c:url value="/static/js/ui-preview-helper.js"/>"></script>
</head>
<body>

  <nav class="navbar navbar-expand-lg sticky-top navbar-light bg-light">
    <div class="container">
      <a class="navbar-brand" href="<c:url value="/"/>"><%= application.getServletContextName() %></a>
      <ul class="navbar-nav mr-auto">
        <li class="nav-item"><a class="nav-link" href="<c:url value="<%= catalogUrl %>"/>">Service Provider Catalog</a></li>
        <li class="nav-item"><a class="nav-link" href="<c:url value="/swagger-ui/index.jsp"/>">Swagger UI</a></li>
      </ul>
    </div>
  </nav>

<div class="container">

  <div class="page-header">
    <h1>Selection Dialog sample client</h1>
    <p>Use this sample code (html, javascript) to build your own interactions with OSLC Delegated UIs.</p>
  </div>

  <div class="row">
    <div class="col-md-6 col-md-offset-3">
      <div class="panel panel-primary">
        <div class="panel-heading">Selection Dialog iframe for:</div>
        <div class="panel-footer">
          <p><em style="word-wrap:break-word;"><%= selectionDialogUri %></em></p>
        </div>
        <div class="panel-body">
          <iframe src="<%= selectionDialogUri %>" id="delegatedUI"></iframe>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-md-6 col-md-offset-3">
      <div class="panel panel-success">
        <div class="panel-heading">Selection Dialog results</div>
        <div class="panel-body" id="results">
            <ul></ul>
        </div>
      </div>
    </div>
  </div>

</div>

<footer class="footer">
  <div class="container">
    <p class="text-muted">
      OSLC Adaptor was generated using <a href="http://eclipse.org/lyo">Eclipse Lyo</a>.
    </p>
  </div>
</footer>

<script type="text/javascript">
    $(function () {
        registerSelectionDUIResponseListener(document.getElementById("delegatedUI"), resetPreviousSelections, presentOslcSelection);

        function presentOslcSelection(resourceUrl, resourceLabel) {
            var displayContainer = document.getElementById("results");
            var hyperlinkElement = document.createElement('a');  
            var link = document.createTextNode(resourceLabel); 
            hyperlinkElement.appendChild(link);  
            hyperlinkElement.title = resourceLabel;
            hyperlinkElement.href = resourceUrl;
            hyperlinkElement.className = 'oslc-resource-link';
            var li = document.createElement("LI");
            li.appendChild(hyperlinkElement); 
            document.querySelector('#results ul').appendChild(li);
            setupUiPreviewOnPopover($("a.oslc-resource-link"));
        }

        function resetPreviousSelections() {
            var resultsUl = document.querySelector('#results ul')
            while(resultsUl.hasChildNodes()) {
                resultsUl.removeChild(resultsUl.lastChild)
            }
        }
    });
</script>
</body>
</html>
