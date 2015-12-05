package org.apache.activemq.web.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.activemq.broker.statistics.QueryStatistics;
import org.apache.activemq.broker.statistics.QueryStatisticsDO;
import org.apache.activemq.web.BrokerFacade;
import org.apache.activemq.web.DestinationFacade;
import org.springframework.beans.factory.DisposableBean;

public class QueueStatisticsQuery extends DestinationFacade implements DisposableBean {

	private List<QueryStatisticsDO> queryStatisticsList;

	public QueueStatisticsQuery(BrokerFacade brokerFacade) {
		super(brokerFacade);
		setJMSDestinationType("query");
	}

	public void setQueryStatisticsList(List<QueryStatisticsDO> queryStatisticsList) {
		this.queryStatisticsList = queryStatisticsList;
	}

	public List<QueryStatisticsDO> getQueryStatisticsList() {
		ConcurrentHashMap<Integer, QueryStatisticsDO> queueStatisticsMap = QueryStatistics.map.get(getJMSDestination());
		if (null != queueStatisticsMap && queueStatisticsMap.size() > 0) {
			queryStatisticsList = new ArrayList<QueryStatisticsDO>();
			int[] keys = getQueueStatisticsMapKeys(queueStatisticsMap);
			for (int j = keys.length - 1; j >= 0; j--) {
				queryStatisticsList.add(queueStatisticsMap.get(keys[j]));
			}
		}
		return queryStatisticsList;
	}

	/**
	 * »ñÈ¡ÉýÐòµÄkey
	 * 
	 * @param queueStatisticsMap
	 * @return
	 */
	private int[] getQueueStatisticsMapKeys(ConcurrentHashMap<Integer, QueryStatisticsDO> queueStatisticsMap) {
		int[] keys = null;
		if (null != queueStatisticsMap && queueStatisticsMap.size() > 0) {
			Iterator<Integer> keySet = queueStatisticsMap.keySet().iterator();
			keys = new int[queueStatisticsMap.size()];
			int i = 0;
			while (keySet.hasNext()) {
				keys[i] = keySet.next();
				i++;
			}
			Arrays.sort(keys);
		}
		return keys;
	}

	@Override
	public void destroy() throws Exception {
		queryStatisticsList = null;

	}

}
