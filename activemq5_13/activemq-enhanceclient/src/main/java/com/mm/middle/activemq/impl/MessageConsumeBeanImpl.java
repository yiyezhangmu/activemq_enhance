package com.mm.middle.activemq.impl;

import java.util.HashSet;
import java.util.Set;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.pool.PooledConnection;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.InitializingBean;

import com.mm.middle.activemq.MessageConsumeBean;
import com.mm.middle.activemq.TqMessageListener;

public class MessageConsumeBeanImpl implements MessageConsumeBean, InitializingBean {
	private PooledConnectionFactory pooledConnectionFactory;
	private Set<String> destinations = new HashSet<String>();// 重复检测

	public PooledConnectionFactory getPooledConnectionFactory() {
		return pooledConnectionFactory;
	}

	public void setPooledConnectionFactory(PooledConnectionFactory pooledConnectionFactory) {
		this.pooledConnectionFactory = pooledConnectionFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public void registerListener(TqMessageListener listener) {
		if (destinations.contains(listener.getDestination())) {
			throw new RuntimeException(" 重复监听 同一 queue：" + listener.getDestination());
		}
		destinations.add(listener.getDestination());
		for (int i = 0; i < listener.getThreadNum(); i++) {
			Thread brokerThread = new Thread(new ConsumerThread(listener, pooledConnectionFactory));
			brokerThread.setDaemon(true);
			brokerThread.start();
		}
	}

	public static class ConsumerThread implements Runnable, ExceptionListener {
		private TqMessageListener listener;
		private PooledConnectionFactory pool;

		private Session session;

		public ConsumerThread(TqMessageListener listener, PooledConnectionFactory pool) {
			super();
			this.listener = listener;
			this.pool = pool;
		}

		@Override
		public void run() {
			try {
				this.initSession(true);
				MessageConsumer consumer = session.createConsumer(session.createQueue(listener.getDestination()));
				while (true) {
					try {
						Message message = consumer.receive();
						if (message instanceof ObjectMessage) {

							boolean result = listener.onMessage(((ObjectMessage) message).getObject());
							if (result) {
								session.commit();
							} else {
								session.rollback();
							}

						} else if (message instanceof TextMessage) {
							boolean result = listener.onMessage(((TextMessage) message).getText());
							if (result) {
								session.commit();
							} else {
								session.rollback();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						this.initSession(true);

					}
				}

			} catch (Throwable e) {
				e.printStackTrace();
				try {
					Thread.sleep(10000);
					this.run();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void onException(JMSException exception) {
		
		}

		private void initSession(boolean transaction) {
			int trynum = 0;
			if (session != null) {
				try {
					session.rollback();
					session.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				session = null;

			}
			while (trynum < 10) {
				try {
					PooledConnection connection = (PooledConnection) pool.createConnection();
					connection.start();
					session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
