<%@page import="org.apache.activemq.web.util.MessageTool"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
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
<html>
	<head>
		<c:set var="pageTitle" value="Manage ${requestContext.queueBrowser.JMSDestination}"/>
		<%@include file="decorators/head.jsp" %>
	</head>
	<body>
		<%@include file="decorators/header.jsp" %>
		<h2>
			Manage <form:tooltip text="${requestContext.queueBrowser.JMSDestination}"/>
		</h2>
		过滤关键字:<input id="keyWordText" type="text" name="keyWord" onblur="updateData()"> 
		<input type="button" onclick="checkAll()" value="全选">
		<input type="button" onclick="cancelAll()" value="取消全选">
		<input type="button" onclick="recovery()" value="恢复">
				<div>
			<jms:forEachDLQMessage queueBrowser="${requestContext.queueBrowser.browser}" var="row">
			</jms:forEachDLQMessage>
			<c:choose>
				<c:when test="${empty prePage}">
				</c:when>
				<c:otherwise>
					<a href="<c:url value="/dlqManage.jsp">
							 <c:param name="keyWord" value="${keyWord}" />
							 <c:param name="JMSDestination" value="${requestContext.queueBrowser.JMSDestination}" />
							 <c:param name="page" value="${prePage}" />
							 <c:param name="pageSize" value="${pageSize}" />
						 </c:url>">上一页</a>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${empty nextPage}">
				</c:when>
				<c:otherwise>
				<a href="<c:url value="/dlqManage.jsp">
							 <c:param name="keyWord" value="${keyWord}" />
							 <c:param name="JMSDestination" value="${requestContext.queueBrowser.JMSDestination}" />
							 <c:param name="page" value="${nextPage}" />
							 <c:param name="pageSize" value="${pageSize}" />
						 </c:url>">下一页</a>
				</c:otherwise>
			</c:choose>
				<c:choose>
		<c:when test="${empty pageInfo}">
		</c:when>
		<c:otherwise>
			当前为第${pageInfo.currPage}页 共${pageInfo.totalPage}页${pageInfo.totalCount}条数据
		</c:otherwise>
	</c:choose>
			
		</div>
		<table id="messages" class="sortable autostripe">
		<thead>
			<tr>
				<th></th>
				<th>消息ID</th>
				<th>原始地址</th>
				<th>持久化</th>
				<th>再投递</th>
				<th>创建时间</th>
				<th>内容</th>
				<th>操作</th>
			</tr>
		</thead>
		<form id="myform" method="post" action="batchMoveMessage.action">
		<input type="hidden" name="JMSDestination" value="${requestContext.queueBrowser.JMSDestination}">
		<input type="hidden" name="JMSDestinationType" value="queue">
		<input type="hidden" name="secret" value="${sessionScope["secret"]}">
		<input id="messageIds" type="hidden" name="messageIds" value="">			
		</form>
			<tbody>
				<jms:forEachDLQMessage queueBrowser="${requestContext.queueBrowser.browser}" var="row">
				<tr>
					<td><input type='checkbox' name='messageIDForCheckBox' value="${row.JMSMessageID}"></td>
					<td><a href="<c:url value="message.jsp">
									 <c:param name="id" value="${row.JMSMessageID}" />
									 <c:param name="JMSDestination" value="${requestContext.queueBrowser.JMSDestination}"/></c:url>"
						title="${row.properties}">${row.JMSMessageID}</a></td>
					<td><c:out value="${row.originalDestination}"/></td>
					<td><jms:persistentNew message="${row}"/></td>
					<td><c:out value="${row.JMSRedelivered}"/></td>
					<td><jms:formatTimestamp timestamp="${row.JMSTimestamp}"/></td>					
					<td><c:out value="${content} "/></td>
					<td>
						<a href="<c:url value="dlqDeleteMessage.action">
									 <c:param name="JMSDestination" value="${requestContext.queueBrowser.JMSDestination}"/>
									 <c:param name="messageId" value="${row.JMSMessageID}"/>
									 <c:param name="secret" value='${sessionScope["secret"]}'/></c:url>"
						   onclick="return confirm('Are you sure you want to delete this message?')"
						>删除</a>					
					</td>
				</tr>
				</jms:forEachDLQMessage>
			</tbody>
		</table>
		<div>
			<c:choose>
				<c:when test="${empty prePage}">
				</c:when>
				<c:otherwise>
					<a href="<c:url value="/dlqManage.jsp">
							 <c:param name="keyWord" value="${keyWord}" />
							 <c:param name="JMSDestination" value="${requestContext.queueBrowser.JMSDestination}" />
							 <c:param name="page" value="${prePage}" />
							 <c:param name="pageSize" value="${pageSize}" />
						 </c:url>">上一页</a>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${empty nextPage}">
				</c:when>
				<c:otherwise>
				<a href="<c:url value="/dlqManage.jsp">
							 <c:param name="keyWord" value="${keyWord}" />
							 <c:param name="JMSDestination" value="${requestContext.queueBrowser.JMSDestination}" />
							 <c:param name="page" value="${nextPage}" />
							 <c:param name="pageSize" value="${pageSize}" />
						 </c:url>">下一页</a>
				</c:otherwise>
			</c:choose>
				<c:choose>
		<c:when test="${empty pageInfo}">
		</c:when>
		<c:otherwise>
			当前为第${pageInfo.currPage}页 共${pageInfo.totalPage}页${pageInfo.totalCount}条数据
		</c:otherwise>
	</c:choose>
		</div>
		<%@include file="decorators/footer.jsp" %>
	</body>
	<script type="text/javascript">
		function checkAll()
		{
			var ids=document.getElementsByName("messageIDForCheckBox");
			for (var i = 0; i < ids.length; i++)         
			{               
				ids[i].checked = true;        
			} 
		}
		function cancelAll()
		{
			var ids=document.getElementsByName("messageIDForCheckBox");
			for (var i = 0; i < ids.length; i++)         
			{               
				ids[i].checked = false;        
			} 
		}
		function recovery()
		{
			var ids=document.getElementsByName("messageIDForCheckBox");
			var messageIds=new Array();
			var id_index=0;
			for (var i = 0; i < ids.length; i++)         
			{        
				if(ids[i].checked == true)
				{
					messageIds[id_index]=ids[i].value;
					id_index++;
				}       
			}
			var mesIds=document.getElementById("messageIds");
			mesIds.value=messageIds;
			console.log(messageIds);
			if(messageIds.length==0)
			{
				alert("请选择数据进行恢复");
				return;
			}
			document.getElementById('myform').submit();
		}
		function updateData()
		{
			var keyWordText=document.getElementById("keyWordText").value;
			var url=window.location.href;						
			if(url.indexOf("keyWord")>0)
				{
					var leng=url.length;
					var url1=url.substring(0,url.indexOf("keyWord"));
					var url2=url.substring(url.indexOf("keyWord"),leng);
					if(url2.indexOf("&")>0)
					{
						var url3=url2.substring(url2.indexOf("&"),url2.length);
						url=url1+"keyWord="+keyWordText;
						url=url+url3
					}
					else{
						url=url1+"keyWord="+keyWordText;
					}
				}
			else
				{
				url=url+"&keyWord="+keyWordText;
				}
			location=url 
		}
		
	</script>
</html>

