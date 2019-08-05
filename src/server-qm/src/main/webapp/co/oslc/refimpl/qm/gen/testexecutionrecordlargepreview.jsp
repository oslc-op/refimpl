<!DOCTYPE html>
<%--Start of user code "Copyright"
--%>
<%--
 Copyright (c) 2011, 2012, 2017 IBM Corporation and others.

 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 and Eclipse Distribution License v. 1.0 which accompanies this distribution.

 The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 and the Eclipse Distribution License is available at
 http://www.eclipse.org/org/documents/edl-v10.php.

 Contributors:

  Sam Padgett     - initial API and implementation
  Michael Fiedler - adapted for OSLC4J
  Jad El-khoury   - initial implementation of code generator (422448)
  Frédéric Loiret - Switch the template to Bootstrap (519699)

 This file is generated by org.eclipse.lyo.oslc4j.codegenerator
--%>
<%--End of user code--%>

<%--Start of user code "body"
--%>
<%--TODO: Replace/adjust this default content as necessary.
All manual changes in this "protected" user code area will NOT be overwritten upon subsequent code generations.
To revert to the default generated content, delete all content in this file, and then re-generate.
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="org.eclipse.lyo.oslc4j.core.model.Link" %>
<%@page import="org.eclipse.lyo.oslc4j.core.model.ServiceProvider"%>
<%@page import="java.net.URI"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.eclipse.lyo.oslc.domains.qm.TestExecutionRecord"%>

<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>

<%
  TestExecutionRecord aTestExecutionRecord = (TestExecutionRecord) request.getAttribute("aTestExecutionRecord");
%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title><%= aTestExecutionRecord.toString() %></title>

  <link href="<c:url value="/static/css/bootstrap-4.0.0-beta.min.css"/>" rel="stylesheet">
  <link href="<c:url value="/static/css/adaptor.css"/>" rel="stylesheet">

  <script src="<c:url value="/static/js/jquery-3.2.1.min.js"/>"></script>
  <script src="<c:url value="/static/js/popper-1.11.0.min.js"/>"></script>
  <script src="<c:url value="/static/js/bootstrap-4.0.0-beta.min.js"/>"></script>
</head>

<body>

<!-- Begin page content -->
<div>
        <div>
          <dl class="dl-horizontal">
            <dt>contributor</dt>
            <dd>
            <ul>
            <%
            for(Link next : aTestExecutionRecord.getContributor()) {
                if (next.getValue() == null) {
                    out.write("<li>" + "<em>null</em>" + "</li>");
                }
                else {
                    %>
                    <li>
                    <jsp:include page="/co/oslc/refimpl/qm/gen/persontohtml.jsp">
                        <jsp:param name="resourceUri" value="<%=next.getValue()%>"/> 
                        </jsp:include>
                    </li>
                    <%
                }
            }
            %>
            </ul>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>created</dt>
            <dd>
            <%
            if (aTestExecutionRecord.getCreated() == null) {
                out.write("<em>null</em>");
            }
            else {
                out.write(aTestExecutionRecord.getCreated().toString());
            }
            %>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>creator</dt>
            <dd>
            <ul>
            <%
            for(Link next : aTestExecutionRecord.getCreator()) {
                if (next.getValue() == null) {
                    out.write("<li>" + "<em>null</em>" + "</li>");
                }
                else {
                    %>
                    <li>
                    <jsp:include page="/co/oslc/refimpl/qm/gen/persontohtml.jsp">
                        <jsp:param name="resourceUri" value="<%=next.getValue()%>"/> 
                        </jsp:include>
                    </li>
                    <%
                }
            }
            %>
            </ul>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>identifier</dt>
            <dd>
            <%
            if (aTestExecutionRecord.getIdentifier() == null) {
                out.write("<em>null</em>");
            }
            else {
                out.write(aTestExecutionRecord.getIdentifier().toString());
            }
            %>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>modified</dt>
            <dd>
            <%
            if (aTestExecutionRecord.getModified() == null) {
                out.write("<em>null</em>");
            }
            else {
                out.write(aTestExecutionRecord.getModified().toString());
            }
            %>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>type</dt>
            <dd>
            <ul>
            <%
            for(Link next : aTestExecutionRecord.getType()) {
                if (next.getValue() == null) {
                    out.write("<li>" + "<em>null</em>" + "</li>");
                }
                else {
                    out.write("<li>" + "<a href=\"" + next.getValue().toString() + "\" class=\"oslc-resource-link\">" + next.getValue().toString() + "</a>" + "</li>");
                }
            }
            %>
            </ul>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>instanceShape</dt>
            <dd>
            <ul>
            <%
            for(Link next : aTestExecutionRecord.getInstanceShape()) {
                if (next.getValue() == null) {
                    out.write("<li>" + "<em>null</em>" + "</li>");
                }
                else {
                    out.write("<li>" + "<a href=\"" + next.getValue().toString() + "\" class=\"oslc-resource-link\">" + next.getValue().toString() + "</a>" + "</li>");
                }
            }
            %>
            </ul>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>serviceProvider</dt>
            <dd>
            <ul>
            <%
            for(Link next : aTestExecutionRecord.getServiceProvider()) {
                if (next.getValue() == null) {
                    out.write("<li>" + "<em>null</em>" + "</li>");
                }
                else {
                    out.write("<li>" + "<a href=\"" + next.getValue().toString() + "\" class=\"oslc-resource-link\">" + next.getValue().toString() + "</a>" + "</li>");
                }
            }
            %>
            </ul>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>title</dt>
            <dd>
            <%
            if (aTestExecutionRecord.getTitle() == null) {
                out.write("<em>null</em>");
            }
            else {
                out.write(aTestExecutionRecord.getTitle().toString());
            }
            %>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>blockedByChangeRequest</dt>
            <dd>
            <ul>
            <%
            for(Link next : aTestExecutionRecord.getBlockedByChangeRequest()) {
                if (next.getValue() == null) {
                    out.write("<li>" + "<em>null</em>" + "</li>");
                }
                else {
                    %>
                    <li>
                    <jsp:include page="/co/oslc/refimpl/qm/gen/changerequesttohtml.jsp">
                        <jsp:param name="resourceUri" value="<%=next.getValue()%>"/> 
                        </jsp:include>
                    </li>
                    <%
                }
            }
            %>
            </ul>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>relatedChangeRequest</dt>
            <dd>
            <ul>
            <%
            for(Link next : aTestExecutionRecord.getRelatedChangeRequest()) {
                if (next.getValue() == null) {
                    out.write("<li>" + "<em>null</em>" + "</li>");
                }
                else {
                    %>
                    <li>
                    <jsp:include page="/co/oslc/refimpl/qm/gen/changerequesttohtml.jsp">
                        <jsp:param name="resourceUri" value="<%=next.getValue()%>"/> 
                        </jsp:include>
                    </li>
                    <%
                }
            }
            %>
            </ul>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>reportsOnTestPlan</dt>
            <dd>
            <%
            if ((aTestExecutionRecord.getReportsOnTestPlan() == null) || (aTestExecutionRecord.getReportsOnTestPlan().getValue() == null)) {
                out.write("<em>null</em>");
            }
            else {
                %>
                <jsp:include page="/co/oslc/refimpl/qm/gen/testplantohtml.jsp">
                    <jsp:param name="resourceUri" value="<%=aTestExecutionRecord.getReportsOnTestPlan().getValue()%>"/> 
                    </jsp:include>
                <%
            }
            %>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>runsOnTestEnvironment</dt>
            <dd>
            <%
            if ((aTestExecutionRecord.getRunsOnTestEnvironment() == null) || (aTestExecutionRecord.getRunsOnTestEnvironment().getValue() == null)) {
                out.write("<em>null</em>");
            }
            else {
                out.write("<a href=\"" + aTestExecutionRecord.getRunsOnTestEnvironment().getValue().toString() + "\" class=\"oslc-resource-link\">" + aTestExecutionRecord.getRunsOnTestEnvironment().getValue().toString() + "</a>");
            }
            %>
            
            </dd>
          </dl>
          <dl class="dl-horizontal">
            <dt>runsTestCase</dt>
            <dd>
            <%
            if ((aTestExecutionRecord.getRunsTestCase() == null) || (aTestExecutionRecord.getRunsTestCase().getValue() == null)) {
                out.write("<em>null</em>");
            }
            else {
                %>
                <jsp:include page="/co/oslc/refimpl/qm/gen/testcasetohtml.jsp">
                    <jsp:param name="resourceUri" value="<%=aTestExecutionRecord.getRunsTestCase().getValue()%>"/> 
                    </jsp:include>
                <%
            }
            %>
            
            </dd>
          </dl>
        </div>
      </div>
</body>
</html>
<%--End of user code--%>
