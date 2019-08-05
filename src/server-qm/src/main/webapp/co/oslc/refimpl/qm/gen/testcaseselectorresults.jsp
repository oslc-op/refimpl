<%--Start of user code "Copyright"
--%>
<%--
 Copyright (c) 2011, 2012 IBM Corporation and others.

 All rights reserved. This program and the accompanying materials
 are made available under the terms of the Eclipse Public License v1.0
 and Eclipse Distribution License v. 1.0 which accompanies this distribution.

 The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 and the Eclipse Distribution License is available at
 http://www.eclipse.org/org/documents/edl-v10.php.

 Contributors:

  Sam Padgett		 - initial API and implementation
  Michael Fiedler	 - adapted for OSLC4J
  Jad El-khoury        - initial implementation of code generator (https://bugs.eclipse.org/bugs/show_bug.cgi?id=422448)

 This file is generated by org.eclipse.lyo.oslc4j.codegenerator
--%>
<%--End of user code--%>

<%--Start of user code "body"
--%>
<%--TODO: Replace/adjust this default content as necessary.
All manual changes in this "protected" user code area will NOT be overwritten upon subsequent code generations.
To revert to the default generated content, delete all content in this file, and then re-generate.
--%>
<%@ page import="java.net.*" %>
<%@ page import="java.util.*" %>
<%@page import="org.eclipse.lyo.oslc4j.core.model.ServiceProvider"%>
<%@page import="org.eclipse.lyo.oslc4j.core.model.AbstractResource"%>
<%@page import="org.eclipse.lyo.oslc.domains.qm.TestCase"%>

<%@ page contentType="application/json" language="java" pageEncoding="UTF-8" %>

{
<%
  String selectionUri = (String) request.getAttribute("selectionUri");
  List<TestCase> resources = (List<TestCase>) request.getAttribute("resources");
  String terms = (String) request.getAttribute("terms");
%>
"oslc:results": [
<% int i = 0; for (TestCase r : resources) { %>
  <% if (i > 0) { %>,<% } %>
  {
    "oslc:label" : "<%= r.toString() %>",
    "rdf:resource" : "<%= r.getAbout() %>"
  }
<% i++; } %>
]
}
<%--End of user code--%>
