package com.mm.middle.activemq;

import java.io.Serializable;
import java.util.List;

import org.apache.activemq.pool.PooledConnectionFactory;

/**
 * 发送mq 事务类型的消息消息
 * 
 * @author yangjie
 * 
 */
public interface MessageSendBean {
	public PooledConnectionFactory getPooledConnectionFactory();

	public boolean sendInTransactionBatch(String destination, List<? extends Serializable> list);

	public boolean sendInTransaction(String destination, Serializable object);

	public Object sendInTransaction(String destination, Serializable object, MqCallbackBeforeCommit callBase);

	public boolean sendNoTransaction(String destination, Serializable object);

	public boolean sendString(String destination, String object);
}
