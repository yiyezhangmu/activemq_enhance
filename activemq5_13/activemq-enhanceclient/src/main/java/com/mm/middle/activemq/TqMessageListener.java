package com.mm.middle.activemq;

import org.springframework.beans.factory.InitializingBean;

/**
 * 消息消费监听，必须实现的父类
 * @author yangjie
 *
 */
public abstract class TqMessageListener implements InitializingBean {
	protected String destination;
	protected int threadNum = 2;
	protected MessageConsumeBean messageConsumeBean;

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public MessageConsumeBean getMessageConsumeBean() {
		return messageConsumeBean;
	}

	public void setMessageConsumeBean(MessageConsumeBean messageConsumeBean) {
		this.messageConsumeBean = messageConsumeBean;
	}

	/**
	 * 返回true 或者抛出异常的话，消息的消费会被回滚
	 * @param message:
	 * @return:
	 * @throws Exception:
	 */
	public abstract boolean onMessage(Object message) throws Exception;

	public void afterPropertiesSet() throws Exception {
		messageConsumeBean.registerListener(this);
	}
}
