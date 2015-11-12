/**
 * 
 */
package com.gome.cloud.datanode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.gome.cloud.common.Configure;
import com.gome.cloud.exception.GFSException;
import com.gome.cloud.net.DataSourceManager;

/**
 * @author blaiu
 *
 */
public class DataNodeLoadBalance {

	protected DataNodeHolder holder;
	
	protected volatile DataNodeInfo dataNodeInfo;
	
	private AtomicLong value = new AtomicLong(1);
	
	private DataSourceManager sourceManager;
	
	private Random random = new Random();
	
	public DataNodeLoadBalance(DataSourceManager sourceManager, DataNodeHolder holder) {
		this.sourceManager = sourceManager;
		this.holder = holder;
	}
	
	private void getDataNodeInfo () {
		if (null == dataNodeInfo || dataNodeInfo.dns != holder.getDataNodes()) {
			synchronized (this) {
				if (null == dataNodeInfo || dataNodeInfo.dns != holder.getDataNodes()) {
					dataNodeInfo = new DataNodeInfo(holder.getDataNodes());
				}
			}
		}
	}
	
	/**
	 * 轮询选择 
	 * @return
	 */
	public DataNode round () {
		getDataNodeInfo();
		int size = dataNodeInfo.masters.size();
		if (0 == size) {
			throw new GFSException("No master data servers are available.");
		}
		
		int key = (int)(value.getAndIncrement() % size);
		return dataNodeInfo.masters.get(key);
	} 
	
	/**
	 * 轮询选择
	 * @return
	 */
	public DataNode leastConnect () {
		DataNode dataNode = round();	//主节点
		int minConn = sourceManager.get(dataNode).getNumActive();
		for (DataNode dn : dataNodeInfo.masters) {
			int minNumActive = sourceManager.get(dn).getNumActive();
			if (minNumActive < minConn) {
				minConn = minNumActive;
				dataNode = dn;
			}
		}
		return dataNode;
	}
	
	/**
	 * 通过组ID选择一个datanode
	 * @param groupId
	 * @return
	 */
	public DataNode select (int groupId) {
		getDataNodeInfo();
		
		Map<Integer, DataNode> dnsMap = dataNodeInfo.dnsMap;
		
		int rnum = 1 + random.nextInt(Configure.REPLICATION - 1);
		
		//随机取从节点
		DataNode node = dnsMap.get(groupId + rnum);
		
		if (null == node) {
			if (rnum > (Configure.REPLICATION - 1) / 2) {
				//小大选择
				for (int i=1; i < Configure.REPLICATION; i++) {
					node = dnsMap.get(groupId + i);
					if (null != node) {
						return node;
					}
				}
			} else {
				//大小选择
				for (int i=Configure.REPLICATION - 1; i > 0; i--) {
					node = dnsMap.get(groupId + i);
					if (null != node) {
						return node;
					}
				}
			}
		} else {
			return node;
		}
		
		//主
		node = dnsMap.get(groupId);
		if (null == node) {
			throw new GFSException("TfnodeRead:Group Unavailable id: " + groupId);
		}
		return node;
	}
	
	/**
	 * 指定获取某个id的dataNode
	 * @param id
	 * @return
	 */
	public DataNode forceSelect (int id) {
		return dataNodeInfo.dnsMap.get(id);
	}
	
	public DataNodeHolder getHolder() {
		return holder;
	}

	class DataNodeInfo {
		private List<DataNode> dns;
		
		/** 节点 */
		private Map<Integer, DataNode> dnsMap;
		
		/** 主节点 */
		private List<DataNode> masters;
		
		public DataNodeInfo(List<DataNode> dns) {
			this.dns = dns;
			masters = new ArrayList<DataNode>();
			dnsMap = new HashMap<Integer, DataNode>();
			
			for (DataNode dn : dns) {
				dnsMap.put(dn.getId(), dn);
				if (dn.isMaster() && dn.getWeight() > 0) {
					masters.add(dn);
				}
			}
		}
	}
	
	
	
}
