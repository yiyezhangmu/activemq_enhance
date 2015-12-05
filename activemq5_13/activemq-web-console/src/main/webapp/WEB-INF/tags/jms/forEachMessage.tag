<%--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at
   
    http://www.apache.org/licenses/LICENSE-2.0
   
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
--%>
<%@tag import="org.apache.activemq.web.query.PageUtil"%>
<%@tag import="org.apache.activemq.web.query.PageInfo"%>
<%@tag import="java.util.ArrayList"%>
<%@tag import="java.util.List"%>
<%@ attribute name="var" type="java.lang.String" required="true"%>
<%@ attribute name="queueBrowser" type="javax.jms.QueueBrowser"
	required="true"%>
<%@ tag import="java.util.Enumeration"%>
<%@ tag import="javax.jms.Message"%>
<%
	Enumeration iter = queueBrowser.getEnumeration();
	List<Message> list = new ArrayList<Message>();
	while (iter.hasMoreElements()) {
		Message message = (Message) iter.nextElement();
		if (message != null) {
			list.add(message);
		}
	}

	if (null != list && list.size() > 0) {
		PageInfo pageInfo = PageUtil.getPageInfoForList(list, request);
		request.setAttribute("pageInfo", pageInfo);
		for (int i = pageInfo.getStart(); i < pageInfo.getEnd(); i++) {

			request.setAttribute(var, list.get(i));
%>
<jsp:doBody />
<%
	}
	}
%>

