package com.mm.test.activemq;

import com.mm.middle.activemq.*;

public class TestTransMessageListener extends TqMessageListener {

	@Override
	public boolean onMessage(Object message) throws Exception {
		System.out.println("custom mq thread name :"
				+ Thread.currentThread().getName());

		if (message instanceof TestMqDO) {
			TestMqDO o = (TestMqDO) message;
			System.out.println("custom mq:::" + o.toString());
			// if (o.getAge() % 2 == 0) {
			// throw new RuntimeException("");//抛出异常回滚
			// }
		} else if (message instanceof String) {
			System.out.println("string message::::" + message);
		}
		return true;
	}

}
