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
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<html>
	<head>
		<c:set var="pageTitle" value="QueueStatistics ${requestContext.queueStatisticsQuery.JMSDestination}"/>
		<%@include file="decorators/head.jsp" %>
	</head>
	<body>
		<%@include file="decorators/header.jsp" %>
		<h2>消息统计 <form:tooltip text="${requestContext.queueStatisticsQuery.JMSDestination}"/></h2>
		<jms:forEachQueueStatistics queueStatisticsQuery="${requestContext.queueStatisticsQuery}" var="row">
		</jms:forEachQueueStatistics>
		当前为第${pageInfo.currPage}页 共${pageInfo.totalPage}页${pageInfo.totalCount}条数据
		<table id="messages" class="sortable autostripe">
		<thead>
			<tr>
				<th>时间</th>
				<th>消息生产数量</th>
				<th>消息成功消费数量</th>
				<th>消息消费失败数量</th>
			</tr>
		</thead>
		<tbody>
			<jms:forEachQueueStatistics queueStatisticsQuery="${requestContext.queueStatisticsQuery}" var="row">
				<tr>
					<td><c:out value="${row.occurTime}"/></td>
					<td><c:out value="${row.produceNum}"/></td>
					<td><c:out value="${row.consumeNum}"/></td>
					<td><c:out value="${row.consumeFaileNum}"/></td>
				</tr>
			</jms:forEachQueueStatistics>
		</tbody>
		</table>
		<div>
	<c:choose>
		<c:when test="${empty prePage}">
		</c:when>
		<c:otherwise>
			<a href="<c:url value="/queueStatistics.jsp">
					 <c:param name="JMSDestination" value="${requestContext.queueStatisticsQuery.JMSDestination}" />
					 <c:param name="page" value="${prePage}" />
					 <c:param name="pageSize" value="${pageSize}" />
				 </c:url>">上一页</a>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${empty nextPage}">
		</c:when>
		<c:otherwise>
		<a href="<c:url value="/queueStatistics.jsp">
					 <c:param name="JMSDestination" value="${requestContext.queueStatisticsQuery.JMSDestination}" />
					 <c:param name="page" value="${nextPage}" />
					 <c:param name="pageSize" value="${pageSize}" />
				 </c:url>">下一页</a>
		</c:otherwise>
	</c:choose>
	当前为第${pageInfo.currPage}页 共${pageInfo.totalPage}页${pageInfo.totalCount}条数据
</div>
		<%@include file="decorators/footerNew.jsp" %>
	</body>
</html>

