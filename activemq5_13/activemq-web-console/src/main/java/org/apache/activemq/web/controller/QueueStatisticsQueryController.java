package org.apache.activemq.web.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.broker.statistics.QueryStatisticsDO;
import org.apache.activemq.web.query.QueueStatisticsQuery;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class QueueStatisticsQueryController implements Controller {

	private String JMSDestination;
	private QueueStatisticsQuery queueStatisticsQuery;

	public String getJMSDestination() {
		return JMSDestination;
	}

	public QueueStatisticsQuery getQueueStatisticsQuery() {
		return queueStatisticsQuery;
	}

	public void setQueueStatisticsQuery(QueueStatisticsQuery queueStatisticsQuery) {
		this.queueStatisticsQuery = queueStatisticsQuery;
	}

	public void setJMSDestination(String jMSDestination) {
		JMSDestination = jMSDestination;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ServletContext s = request.getServletContext();
		queueStatisticsQuery.setJMSDestination(JMSDestination);
		List<QueryStatisticsDO> list = queueStatisticsQuery.getQueryStatisticsList();
		if (null != list && list.size() > 0) {
	
				response.getOutputStream().print("time--" + list.get(0).getOccurTime() + " QueryName--"
						+ list.get(0).getQueryName() + " ConsumeFaileNum--"
						+ list.get(0).getConsumeFaileNum() + " ConsumeNum--" + list.get(0).getConsumeNum()
						+ " ProduceNum--" + list.get(0).getProduceNum());
		
		} else {
			response.getOutputStream().print(JMSDestination + "is empty");
		}

		return null;
	}

}
