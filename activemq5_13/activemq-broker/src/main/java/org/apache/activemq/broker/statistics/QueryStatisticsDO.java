package org.apache.activemq.broker.statistics;

import java.util.concurrent.atomic.AtomicLong;

public class QueryStatisticsDO {

	private String queryName;
	private int occurTime;// 小时 eg: 2015112513
	private AtomicLong produceNum = new AtomicLong(0L);// 创建消息的数量
	private AtomicLong consumeNum = new AtomicLong(0L);
	private AtomicLong consumeFaileNum = new AtomicLong(0L);

	public QueryStatisticsDO(String queryName, int occurTime) {
		super();
		this.queryName = queryName;
		this.occurTime = occurTime;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public int getOccurTime() {
		return occurTime;
	}

	public void setOccurTime(int occurTime) {
		this.occurTime = occurTime;
	}

	public AtomicLong getProduceNum() {
		return produceNum;
	}

	public void setProduceNum(AtomicLong produceNum) {
		this.produceNum = produceNum;
	}

	public AtomicLong getConsumeFaileNum() {
		return consumeFaileNum;
	}

	public void setConsumeFaileNum(AtomicLong consumeFaileNum) {
		this.consumeFaileNum = consumeFaileNum;
	}

	public AtomicLong getConsumeNum() {
		return consumeNum;
	}

	public void setConsumeNum(AtomicLong consumeNum) {
		this.consumeNum = consumeNum;
	}

}
