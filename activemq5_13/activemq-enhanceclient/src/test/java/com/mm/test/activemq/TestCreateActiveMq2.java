package com.mm.test.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mm.middle.activemq.*;

public class TestCreateActiveMq2 {

	public static void main(String[] args) {
		ApplicationContext context;
		try {
			context = new ClassPathXmlApplicationContext("classpath:/application-activemq.xml");
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("请确认activemq 是否启动");
			return;
		}
		MessageSendBean messageSendBean = (MessageSendBean) context.getBean("messageSendBean");
		PooledConnectionFactory poolfactory = messageSendBean.getPooledConnectionFactory();
		poolfactory.getProperties();
		org.apache.activemq.ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) poolfactory
				.getConnectionFactory();
		factory.getBrokerURL();

		// boolean result = messageSendBean.sendInTransaction("TEST.TRANS", cc);
		//
		// boolean result0 = messageSendBean.sendInTransaction("TEST.TRANS",
		// bb);
		//
		//
		boolean result1 = messageSendBean.sendString("TEST.TRANS", new String("nnnnnnnnn1111"));
		boolean result2 = messageSendBean.sendString("TEST.TRANS", new String("nnnnnnnnnn2222"));
		boolean result3 = messageSendBean.sendString("TEST.TRANS", "jmmmmmmmmmmmmmmmmmmmmmmmmmm");
		System.out.println("result1:" + result1);
		System.out.println("result2:" + result2);

		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
