package org.apache.activemq.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.web.BrokerFacade;
import org.apache.activemq.web.DestinationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class DLQDeleteMessage extends DestinationFacade implements Controller {
    private String messageId;
    private static final Logger log = LoggerFactory.getLogger(DeleteMessage.class);

    public DLQDeleteMessage(BrokerFacade brokerFacade) {
        super(brokerFacade);
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (messageId != null) {
            QueueViewMBean queueView = getQueueView();
            if (queueView != null) {
                log.info("Removing message " + getJMSDestination() + "(" + messageId + ")");
                queueView.removeMessage(messageId);
            } else {
            	log.warn("No queue named: " + getPhysicalDestinationName());
            }
        }
        return redirectToDLQManageView();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    protected ModelAndView redirectToDLQManageView() {
		return new ModelAndView("redirect:dlqManage.jsp?JMSDestination=" + getJMSDestination());
	}
}

