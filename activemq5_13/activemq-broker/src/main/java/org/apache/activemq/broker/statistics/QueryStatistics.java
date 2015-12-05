package org.apache.activemq.broker.statistics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;

public class QueryStatistics {
	public static boolean openStatistice = true;

	/** 格式 key:queueNam, value 每小时的数据列表 */
	public static ConcurrentHashMap<String, ConcurrentHashMap<Integer, QueryStatisticsDO>> map = new ConcurrentHashMap<String, ConcurrentHashMap<Integer, QueryStatisticsDO>>();

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
	public static void addProduceOneQueue(
			ActiveMQDestination activeMQDestination) {
		String queueName = getQueueName(activeMQDestination);
		if (null != queueName) {
			QueryStatisticsDO data = getQueryStatisticsDO(queueName);
			data.getProduceNum().incrementAndGet();// 同步增加
		}
	}

	public static void addConsumeOneQueue(
			ActiveMQDestination activeMQDestinatione) {
		String queueName = getQueueName(activeMQDestinatione);
		if (null != queueName) {
			QueryStatisticsDO data = getQueryStatisticsDO(queueName);
			data.getConsumeNum().incrementAndGet();// 同步增加
		}
	}

	public static void addConsumeOneQueueFaile(
			ActiveMQDestination activeMQDestinatione) {
		String queueName = getQueueName(activeMQDestinatione);
		if (null != queueName) {
			QueryStatisticsDO data = getQueryStatisticsDO(queueName);
			data.getConsumeFaileNum().incrementAndGet();// 同步增加
		}
	}

	private static String getQueueName(ActiveMQDestination activeMQDestination) {
		if (activeMQDestination != null
				&& activeMQDestination instanceof ActiveMQQueue) {
			try {
				return (((ActiveMQQueue) activeMQDestination).getQueueName());
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private static QueryStatisticsDO getQueryStatisticsDO(String queueName) {
		ConcurrentHashMap<Integer, QueryStatisticsDO> m = map.get(queueName);
		if (m == null) {
			m = new ConcurrentHashMap<Integer, QueryStatisticsDO>();
			map.put(queueName, m);
		}
		int nowHour = getNowHour();
		
		QueryStatisticsDO data = m.get(nowHour);
		if (data == null) {
			data = new QueryStatisticsDO(queueName, nowHour);
			m.put(nowHour, data);
		}
		return data;
	}
	
	private static int getNowHour() {
		Date date = new Date();
		String now = sdf.format(date);
		return Integer.parseInt(now);
	}
	
	public static void main(String[] args) {
		System.out.println(getNowHour());
	}
	
	
}
