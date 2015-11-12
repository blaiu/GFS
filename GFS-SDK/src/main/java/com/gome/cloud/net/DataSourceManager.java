/**
 * 
 */
package com.gome.cloud.net;

import java.util.Hashtable;
import java.util.Map;

import com.gome.cloud.common.Configure;
import com.gome.cloud.datanode.DataNode;

/**
 * 数据源管理
 * @author blaiu
 *
 */
public class DataSourceManager {

	private final Map<Integer, ConnectPool> source = new Hashtable<Integer, ConnectPool>();
//	private final Map<Integer, ConnectPool> source = new ConcurrentHashMap<Integer, ConnectPool>();
	
	private Configure configure;
	
	public DataSourceManager (Configure configure) {
		this.configure = configure;
	}
	
	/**
	 * 获得连接池
	 * @param node
	 * @return
	 */
	public ConnectPool get(DataNode node) {
		ConnectPool pool = source.get(node.getId());
		if (null != pool) {
			return pool;
		} else {
			synchronized (this) {
				pool = source.get(node.getId());
				if (null == pool) {
					return register(node);
				} else {
					return pool;
				}
			}
		}
	}
	
	/**
	 * 获取连接池
	 * @param id
	 * @return
	 */
	public ConnectPool get (int id) {
		return source.get(id);
	}
	
	/**
	 * 关闭并删除连接池
	 * @param id
	 */
	public void remove (int id) {
		ConnectPool pool = source.get(id);
		if (null != pool) {
			pool.close();
		}
	}
	
	/**
	 * 注册连接，防止连接池
	 * @param node
	 * @return
	 */
	private ConnectPool register(DataNode node) {
		ConnectFactory factory = new ConnectFactory(node.getIp(), node.getPort(), configure.getSocketTimeoutMs());
		ConnectPool pool = new ConnectPool(factory, configure);
		source.put(node.getId(), pool);
		return pool;
	}
	
	/**
	 * 统计连接总数
	 * @return
	 */
	public int totalConnets () {
		int total = 0;
		for (ConnectPool pool : source.values()) {
			total += (pool.getNumActive() + pool.getNumIdle());
		}
		return total;
	}
	
	/**
	 * 获取连接池的大小
	 * @return
	 */
	public int getSize () {
		return source.size();
	}

	public void setConfigure(Configure configure) {
		this.configure = configure;
	}
	
	
}
