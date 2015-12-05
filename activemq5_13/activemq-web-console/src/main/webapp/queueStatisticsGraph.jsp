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
		<script type="text/javascript" src="/admin/js/highcharts/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="/admin/js/highcharts/highcharts.js" ></script>
		<script type="text/javascript">
$(function () {
	var time=$('#time').val();
	time=time.substring(0,time.length-1);
	var times=time.split(",");
	var produceNum=$('#produceNum').val();
	var produceNums=produceNum.split(",");
	var consumeNum=$('#consumeNum').val();
	var consumeNums=consumeNum.split(",");
	var consumeFaileNum=$('#consumeFaileNum').val();
	var consumeFaileNums=consumeFaileNum.split(",");
	var pNums=new Array();
	var cNums=new Array();
	var cFaileNums=new Array();
	for(var i=0;i<times.length;i++)
	{
		pNums[i]=parseInt(produceNums[i]);
		cNums[i]=parseInt(consumeNums[i]);
		cFaileNums[i]=parseInt(consumeFaileNums[i]);
	}

    $('#userDiv').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: '消息统计图'
        },
        subtitle: {
            text: '每小时消息统计图'
        },
        xAxis: {
            categories: times
        },
        yAxis: {
            title: {
                text: '数量 '
            }
        },
        tooltip: {
            enabled: false,
            formatter: function() {
                return '<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y +'条';
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        series: [{
            name: '消息生产',
            data: pNums
        }, {
            name: '消费消息',
            data: cNums
        }, {
            name: '消费失败',
            data: cFaileNums
        }]
    });
});	
</script>	
	</head>
	<body>
		<%@include file="decorators/header.jsp" %>
		<h2>消息统计 <form:tooltip text="${requestContext.queueStatisticsQuery.JMSDestination}"/></h2>
		<table id="messages" class="sortable autostripe">
<div id="assessmentTargetItem">    
        <div id="userDiv" class="grid_16 label-right" style="min-width:100%;height:400px">
        </div>
</div>
			<jms:forEachQueueStatisticsGraph queueStatisticsQuery="${requestContext.queueStatisticsQuery}" var="row">
				
			</jms:forEachQueueStatisticsGraph>
			<input type="hidden" id="time" value="${time}">
			<input type="hidden" id="produceNum" value="${produceNum}">
			<input type="hidden" id="consumeNum" value="${consumeNum}">
			<input type="hidden" id="consumeFaileNum" value="${consumeFaileNum}">
			</table>

		<%@include file="decorators/footerNew.jsp" %>
	</body>
</html>

	