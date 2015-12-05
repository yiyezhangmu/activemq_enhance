package com.mm.middle.activemq.impl;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;

import com.mm.middle.activemq.MessageSendBean;
import com.mm.middle.activemq.MqCallbackBeforeCommit;

/**
 * 发送mq 事务类型的消息消息
 * 
 * @author yangjie
 * 
 */
public class MessageSendBeanImpl implements MessageSendBean {
	private PooledConnectionFactory pooledConnectionFactory;

	public PooledConnectionFactory getPooledConnectionFactory() {
		return pooledConnectionFactory;
	}

	public void setPooledConnectionFactory(PooledConnectionFactory pooledConnectionFactory) {
		this.pooledConnectionFactory = pooledConnectionFactory;
	}

	public void init() {
		//
	}

	private Session getSession(boolean transaction) {
		PooledConnection connection = null;
		try {
			connection = (PooledConnection) pooledConnectionFactory.createConnection();
			Session session = connection.createSession(transaction, Session.AUTO_ACKNOWLEDGE);
			return session;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean sendInTransactionBatch(String destination, List<? extends Serializable> list) {
		Session session = this.getSession(true);
		if (session == null) {
			return false;
		}
		try {
			MessageProducer producer = session.createProducer(session.createQueue(destination));
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			for (Serializable object : list) {
				ObjectMessage message = session.createObjectMessage(object);
				producer.send(message);
			}
			session.commit();
			return true;
		} catch (Exception e) {
			try {
				session.rollback();
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean sendInTransaction(String destination, Serializable object) {
		List<Serializable> list = new ArrayList<Serializable>();
		list.add(object);
		return this.sendInTransactionBatch(destination, list);
	}

	@Override
	public Object sendInTransaction(String destination, Serializable object, MqCallbackBeforeCommit callBase) {

		Session session = this.getSession(true);
		if (session == null) {
			return null;
		}
		Object result = null;

		try {
			MessageProducer producer = session.createProducer(session.createQueue(destination));
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			ObjectMessage message = session.createObjectMessage(object);
			producer.send(message);

			result = callBase.doMethod();
			session.commit();
		} catch (Exception e) {
			try {
				session.rollback();
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			} // 如果未commit 就close，那么close 前会自动rollback
		}
		return result;
	}

	@Override
	public boolean sendNoTransaction(String destination, Serializable object) {
		Session session = this.getSession(false);
		try {
			MessageProducer producer = session.createProducer(session.createQueue(destination));
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);

			ObjectMessage message = session.createObjectMessage(object);
			producer.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public boolean sendString(String destination, String object) {
		Session session = this.getSession(false);
		try {
			MessageProducer producer = session.createProducer(session.createQueue(destination));
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);

			TextMessage message = session.createTextMessage(object);
			producer.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				session.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
