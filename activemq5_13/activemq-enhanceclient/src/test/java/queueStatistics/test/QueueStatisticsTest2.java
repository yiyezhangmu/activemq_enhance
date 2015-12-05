package queueStatistics.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;


import org.junit.Test;



public class QueueStatisticsTest2 {
	private final static String  TRANSPORTCONNECTORS="tcp://127.0.0.1:61616";
	private final static String WEB_CONSOLE_URL="http://127.0.0.1:8161/admin";

	private Connection getConnection() throws JMSException {

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(TRANSPORTCONNECTORS);
		Connection connection;
		connection = connectionFactory.createConnection();
		connection.start();
		return connection;

	}

	private void getQueueStatisticsInfo() {

		URL url;
		URLConnection connection = null;
		try {
			url = new URL(WEB_CONSOLE_URL+"/queueStatistics.do?JMSDestination=TTTT");
			connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String info = "";
			while ((info = reader.readLine()) != null) {
				System.out.println(info);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != connection.getInputStream()) {
					connection.getInputStream().close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	@Test
	public void testSendMessage() throws JMSException, IOException {
		Connection connection = null;
		Session session = null;
		try {
			connection = getConnection();
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("TTTT");
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			ObjectMessage message = session.createObjectMessage();
			message.setObject("tttt");
			System.out.println("before sendMessage");
			getQueueStatisticsInfo();
			producer.send(message);
			session.commit();
			System.out.println("after sendMessage");
			getQueueStatisticsInfo();
		} catch (JMSException e) {

			e.printStackTrace();
		} finally {
			if (null != session) {
				session.close();
			}
			if (null != connection) {
				connection.close();
			}
		}

	}

	

	@Test
	public void testConsumptionMessage() throws JMSException, InterruptedException {
		System.out.println("before ConsumptionMessage");
		getQueueStatisticsInfo();
		Connection connection = getConnection();

		final Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("TTTT");

		MessageConsumer consumer = session.createConsumer(destination);
		// listener 方式
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message paramMessage) {
				ObjectMessage message = (ObjectMessage) paramMessage;

				try {
					message.getObject();
					session.commit();
					
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				

			}
		});

		  TimeUnit.MINUTES.sleep(1);   
		session.close();
		connection.close();
		System.out.println("after ConsumptionMessage");
		getQueueStatisticsInfo();
	}
	
	
	@Test
	public void testConsumptionFailMessage() throws JMSException, InterruptedException {
		System.out.println("before ConsumptionMessage");
		getQueueStatisticsInfo();
		Connection connection = getConnection();

		final Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("TTTT");

		MessageConsumer consumer = session.createConsumer(destination);
		// listener 方式
		consumer.setMessageListener(new MessageListener() {

			@Override
			public void onMessage(Message paramMessage) {
				ObjectMessage message = (ObjectMessage) paramMessage;

				try {
					message.getObject();
					session.rollback();
					
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				

			}
		});

		  TimeUnit.MINUTES.sleep(1);   
		session.close();
		connection.close();
		System.out.println("after ConsumptionMessage");
		getQueueStatisticsInfo();
	}
}
