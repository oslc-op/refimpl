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

<%@page import="java.util.List" %>
<%@page import="java.net.URI" %>
<%@page import="org.eclipse.lyo.oslc4j.core.model.ResourceShape"%>
<%@page import="org.eclipse.lyo.oslc4j.core.model.Property"%>
<%@page import="org.eclipse.lyo.oslc4j.core.model.OslcConstants"%>
<%@page import="jakarta.ws.rs.core.UriBuilder"%>
<%@page import="org.eclipse.lyo.oslc4j.core.OSLC4JUtils"%>

<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>

<%
  ResourceShape aResourceShape = (ResourceShape) request.getAttribute("aResourceShape");
  String catalogUrl = UriBuilder.fromUri(OSLC4JUtils.getServletURI()).path("/catalog/singleton").build().toString();
%>

<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title><%= aResourceShape.getName() %></title>

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
            <div class="row">
                <div class="col-md-12" id="page-index">
                    <h1><%=aResourceShape.getName()%></h1>
                    <p class="lead">
                      Describes:
                      <%if(aResourceShape.getDescribes().length == 1) {%>
                          <code><%=aResourceShape.getDescribes()[0]%></code>
                      <%} else {%>
                            <ul>
                                <%for(URI next : aResourceShape.getDescribes()) {
                            String[] split = next.toString().split("[#/]+");
                            String shortName = (split.length > 1) ? split[split.length -1] : next.toString();
                                %>
                                    <li><a href="<%=next%>"><%=shortName%></a></li>
                                <%}%>
                            </ul>
                      <%}%>
                    </p>
                    <%if(null != aResourceShape.getTitle()) {%>
                    <p>
                      <strong>Summary:</strong>
                      <%=aResourceShape.getTitle()%>
                    </p>
                    <%}%>
                    <%if(null != aResourceShape.getDescription()) {%>
                    <p>
                      <strong>Description:</strong>
                      <%=aResourceShape.getDescription()%>
                    </p>
                    <%}%>
                    <h2>Properties</h2>
                    <table class="table">
                        <tr>
                            <th bgcolor="#687684" valign="top">
                                <a rel="nofollow">
                                    <font color="#ffffff">Prefixed Name</font>
                                </a>
                            </th>
                            <th bgcolor="#687684" valign="top">
                                <a rel="nofollow">
                                    <font color="#ffffff">Occurs</font>
                                </a>
                            </th>
                            <th bgcolor="#687684" valign="top">
                                <a rel="nofollow">
                                    <font color="#ffffff">Read-only</font>
                                </a>
                            </th>
                            <th bgcolor="#687684" valign="top">
                                <a rel="nofollow">
                                    <font color="#ffffff">Value-type</font>
                                </a>
                            </th>
                            <th bgcolor="#687684" valign="top">
                                <a rel="nofollow">
                                    <font color="#ffffff">Representation</font>
                                </a>
                            </th>
                            <th bgcolor="#687684" valign="top">
                                <a rel="nofollow">
                                    <font color="#ffffff">Range</font>
                                </a>
                            </th>
                            <th bgcolor="#687684" valign="top">
                                <a rel="nofollow">
                                    <font color="#ffffff">Description</font>
                                </a>
                            </th>
                        </tr>
                        <%for(Property property : aResourceShape.getProperties()) {%>
                            <tr>
                                <td bgcolor="#ffffff" valign="top">
                                    <code><%=property.getName()%></code>
                                </td>
                                <td bgcolor="#ffffff" valign="top">
                                    <a href="<%=property.getOccurs()%>">
                                        <%= property.getOccurs().toString().replaceFirst(OslcConstants.OSLC_CORE_NAMESPACE, "")%></a>
                                </td>
                                <td bgcolor="#ffffff" valign="top">
                                    <%=property.isReadOnly()%>
                                </td>
                                <td bgcolor="#ffffff" valign="top">
                                    <a href="<%=property.getValueType()%>">
                                        <%= property.getValueType().toString().split("#")[1]%></a>
                                </td>
                                <td bgcolor="#ffffff" valign="top">
                                    <%= (property.getRepresentation() == null) ? "n/a" : "<a href=\"" + property.getRepresentation() + "\"> " + property.getRepresentation().toString().split("#")[1] + "</a>"%></td>
                                <td bgcolor="#ffffff" valign="top">
                                    <%for(URI range : property.getRange()) {
                                        String[] split = range.toString().split("[#/]+");
                                        String shortName = (split.length > 1) ? split[split.length -1] : range.toString();
                                    %>
                                        <a href="<%=range%>"><%=shortName%></a>
                                    <%}%>
                                </td>
                            </td>
                            <td bgcolor="#ffffff" valign="top">
                                <%=(property.getDescription() == null) ? "n/a" : property.getDescription() %>
                            </td>
                        </tr>
                    <%}%>
                </table>
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
    </body>
</html>
