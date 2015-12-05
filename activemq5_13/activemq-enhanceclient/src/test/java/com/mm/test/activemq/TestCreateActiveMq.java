package com.mm.test.activemq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mm.middle.activemq.*;

public class TestCreateActiveMq {

	// 请添加 -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=com.tianque.userauth.test.activemq
	
	
	
	public static void main(String[] args) {
		ApplicationContext context;
		try {
			context = new ClassPathXmlApplicationContext(
					"classpath:/application-activemq.xml");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("请确认activemq 是否启动");
			return;
		}
		int i = 1051;

		for (; i < 1053; i++) {

			final MessageSendBean messageSendBean = (MessageSendBean) context
					.getBean("messageSendBean");

			final int ss = i;
			Thread t = new Thread() {
				String name = "trhead" + ss;

				public void run() {
					TestMqDO bb = new TestMqDO("bbbbbbb" + ss, 20);

					Object result2 = messageSendBean.sendInTransaction(
							"call-info", bb, new MqCallbackBeforeCommit() {

								@Override
								public Object doMethod() {
									long start = System.currentTimeMillis();
									System.out.println(name
											+ ":time1 start ==============="
											+ start);
									try {
										Thread.sleep(100);
										// 300000
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									System.out.println(name
											+ ":time1 spend  ==============="
											+ (System.currentTimeMillis() - start));

									// 在这里做一些其他操作，如数据库。
									String result = "db insert success";
									return result;// 返回值可以直接被返回到，messageSendBean.sendInTransaction
								}

							});

					System.out.println(name + ":transulatc result:" + result2);

				}
			};
			t.start();
			System.out.println("start thread " + i);
			try {
				Thread.sleep(1L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// boolean result1 = messageSendBean.sendInTransaction("TEST.TRANS",
			// new TestMqDO("dadfa" + ss, 20));

		}
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
