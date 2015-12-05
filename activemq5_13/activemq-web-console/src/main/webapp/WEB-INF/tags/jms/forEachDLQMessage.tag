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

<%@tag import="org.apache.activemq.command.ActiveMQMessage"%>
<%@tag import="org.apache.activemq.web.util.MessageTool"%>
<%@tag import="org.eclipse.jetty.websocket.api.MessageTooLargeException"%>
<%@tag import="org.apache.activemq.web.query.PageUtil"%>
<%@tag import="org.apache.activemq.web.query.PageInfo"%>
<%@tag import="org.apache.activemq.command.ActiveMQDestination"%>
<%@tag import="org.apache.activemq.command.ActiveMQObjectMessage"%>
<%@tag import="java.lang.reflect.Method"%>
<%@tag import="java.lang.reflect.Field"%>
<%@tag import="java.util.ArrayList"%>
<%@tag import="java.util.List"%>
<%@ attribute name="var" type="java.lang.String" required="true"%>
<%@ attribute name="queueBrowser" type="javax.jms.QueueBrowser"
	required="true"%>
<%@ tag import="java.util.Enumeration"%>
<%@ tag import="javax.jms.Message"%>
<%
	Enumeration iter = queueBrowser.getEnumeration();
	List<Message> messages = new ArrayList<Message>();
    		String keyWord=request.getParameter("keyWord");
	String originalDestination = null;
	while (iter.hasMoreElements()) {
		Message message = (Message) iter.nextElement();
		if (message != null) {

			if (message instanceof ActiveMQMessage) {
				ActiveMQMessage msg = (org.apache.activemq.command.ActiveMQMessage) message;

				ActiveMQDestination activeMQDestination = msg.getOriginalDestination();
		
				if (null != activeMQDestination) {
					originalDestination = activeMQDestination.getPhysicalName();
				}

			}

			if (("ActiveMQ.DLQ").equals(originalDestination)) {
				continue;
			}
			try
			{
				String value=	MessageTool.getBody(message).toString();
				
				if(null!=value&&!PageUtil.isEmpty(keyWord))
				{
					
					if(value.contains(keyWord))
					{
						continue;
					}
				}
				messages.add(message);
			}catch(NoClassDefFoundError e)
			{
				System.out.println(e.toString());
				String s =e.toString();
			}
			

		}
	}
	if (null != messages && messages.size() > 0) {
		PageInfo pageInfo = PageUtil.getPageInfoForList(messages, request);
		request.setAttribute("pageInfo", pageInfo);
		for (int i = pageInfo.getStart(); i < pageInfo.getEnd(); i++) {
			String value=	MessageTool.getBody( messages.get(i)).toString();

			request.setAttribute(var, messages.get(i));
			request.setAttribute("content", value);
%>
<jsp:doBody />
<%
	}
	}
%>


