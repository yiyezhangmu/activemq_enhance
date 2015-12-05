package org.apache.activemq.web.controller;


import java.util.Enumeration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.web.BrokerFacade;

import org.apache.activemq.web.QueueBrowseQuery;
import org.apache.activemq.web.SessionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class BatchMoveMessage extends QueueBrowseQuery implements Controller {
	public BatchMoveMessage(BrokerFacade brokerFacade, SessionPool sessionPool) throws JMSException {
		super(brokerFacade, sessionPool);
	}

	private String messageIds;
	private static final Logger log = LoggerFactory.getLogger(MoveMessage.class);

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (messageIds != null) {
			String[] ids = messageIds.split(",");
			Message[] message = getMessage(ids);
			for (int i = 0; i < ids.length; i++) {
				QueueViewMBean queueView = getQueueView();
				String destination = getOriginalDestination(message[i]);
				if (queueView != null) {
					log.info("Moving message " + getJMSDestination() + "(" + ids[i] + ")" + " to " + destination);
					queueView.moveMessageTo(ids[i], destination);
				} else {
					log.warn("No queue named: " + getPhysicalDestinationName());
				}
			}

		}
		return redirectToDLQManageView();
	}

	protected ModelAndView redirectToDLQManageView() {
		return new ModelAndView("redirect:dlqManage.jsp?JMSDestination=" + getJMSDestination());
	}

	public String getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(String messageIds) {
		this.messageIds = messageIds;
	}

	private Message[] getMessage(String[] ids) throws JMSException {
		Message[] messages = new Message[ids.length];
		QueueBrowser tempBrowser = getBrowser();
		for (int i = 0; i < ids.length; i++) {
			Enumeration iter = tempBrowser.getEnumeration();
			while (iter.hasMoreElements()) {
				Message item = (Message) iter.nextElement();
				if (ids[i].equals(item.getJMSMessageID())) {
					messages[i] = item;
					break;
				}
			}

		}
		tempBrowser.close();
		return messages;
	}

	public Message getMessage(String id) throws JMSException {
		Message message = null;
		if (id != null) {
			QueueBrowser tempBrowser = getBrowser();
			Enumeration iter = tempBrowser.getEnumeration();
			while (iter.hasMoreElements()) {
				Message item = (Message) iter.nextElement();
				if (id.equals(item.getJMSMessageID())) {
					message = item;
					break;
				}
			}
			tempBrowser.close();
		}

		return message;
	}

	private String getOriginalDestination(Message message) {
		String originalDestination = null;
		try {

			if (message instanceof ActiveMQObjectMessage) {
				ActiveMQObjectMessage msg = (org.apache.activemq.command.ActiveMQObjectMessage) message;
				ActiveMQDestination activeMQDestination = msg.getOriginalDestination();
				originalDestination = activeMQDestination.getPhysicalName();

			}
		} catch (Exception e) {
			log.error("getOriginalDestination is error", e);
		}
		return originalDestination;
	}

}
