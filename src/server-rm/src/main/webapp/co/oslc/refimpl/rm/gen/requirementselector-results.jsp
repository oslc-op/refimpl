<%--
 Copyright (c) 2025 Contributors to the Eclipse Foundation
 
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
<%@page import="org.eclipse.lyo.oslc.domains.rm.Requirement"%>

<%@ page contentType="text/html" language="java" pageEncoding="UTF-8" %>

<c:forEach var="requirement" items="${resources}">
  <ul>
    <li>
      <a href="${requirement.about}"
        onclick="sendOslcSelectionPostMessage(this, event)"
        >${requirement.title}</a>
    </li>
  </ul>
</c:forEach>
