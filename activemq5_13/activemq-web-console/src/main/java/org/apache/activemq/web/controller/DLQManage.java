package org.apache.activemq.web.controller;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.web.BrokerFacade;
import org.apache.activemq.web.DestinationFacade;
import org.apache.activemq.web.SessionPool;
import org.springframework.beans.factory.DisposableBean;

public class DLQManage extends DestinationFacade implements DisposableBean {
	private String defaultJmsDestination = "ActiveMQ.DLQ";
	private String keyWord;

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	private SessionPool sessionPool;
	private String selector;
	private Session session;
	private Queue queue;
	private QueueBrowser browser;

	public DLQManage(BrokerFacade brokerFacade) {
		super(brokerFacade);
		setJMSDestinationType("query");
	}

	public void destroy() throws Exception {
		if (this.browser != null) {
			this.browser.close();
		}
		this.sessionPool.returnSession(this.session);
		this.session = null;
	}

	public QueueBrowser getBrowser() throws JMSException {
		if (this.browser == null) {
			this.browser = createBrowser();
		}
		return this.browser;
	}

	public void setBrowser(QueueBrowser browser) {
		this.browser = browser;
	}

	public Queue getQueue() throws JMSException {
		if (this.queue == null) {
			this.queue = this.session.createQueue(defaultJmsDestination);
		}
		return this.queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public String getSelector() {
		return this.selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public Session getSession() {
		return this.session;
	}

	public boolean isQueue() {
		return true;
	}

	protected QueueBrowser createBrowser() throws JMSException {
		return getSession().createBrowser(getQueue(), getSelector());
	}

}
