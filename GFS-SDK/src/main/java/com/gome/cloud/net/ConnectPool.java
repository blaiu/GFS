package com.gome.cloud.net;

import org.apache.commons.pool.BaseObjectPool;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.gome.cloud.common.Configure;
import com.gome.cloud.exception.GFSException;

/**
 * 
 * @author blaiu
 *
 */
public class ConnectPool extends BaseObjectPool<Connection>{

	private ObjectPool<Connection> pool;
	
	private ConnectFactory factory;
	
	public ConnectPool(ConnectFactory factory, Configure configure) {
		this.factory = factory;
		initPool(configure);
	}
	
	/**
	 * 初始化连接参数
	 * @param configure
	 */
	public void initPool(Configure configure) {
		GenericObjectPool<Connection> pool = new GenericObjectPool<Connection>(factory);
		pool.setMaxActive(configure.getMaxActive());							//最大活动连接数
		pool.setWhenExhaustedAction((byte)configure.getWhenExhaustedAction());	//当 连接数达到 maxActive时，0:抛出异常 1：阻塞 2：新建连接
		pool.setMaxWait(configure.getMaxWaitTimeMs());							//发生阻塞是最大等待时间
		pool.setMaxIdle(configure.getMaxIdle());								//最大空闲连接
		pool.setMinIdle(configure.getMinIdle());								//最小空闲连接
		pool.setTestOnBorrow(true);												//借出对象时是否进行有效性检查
		pool.setTestOnReturn(false);											//返还对象时是否进行有效性检查
		pool.setTimeBetweenEvictionRunsMillis(configure.getTimeBetweenEvictionRunsMs());	//后台每隔多长时间对连接进行清理
		pool.setNumTestsPerEvictionRun(configure.getNumTestsPerEvictionRun());				//每次清理个数
		pool.setMinEvictableIdleTimeMillis(configure.getMinEvictableIdleTimeMs());			//空闲连接大于此值时 此连接关闭
		this.pool = pool;
	}
	
	/**
	 * 租借连接
	 */
	@Override
	public Connection borrowObject() {
		try {
			return pool.borrowObject();
		} catch (Exception e) {
			throw new GFSException(e, e.getMessage());
		}
	}

	/**
	 * 归还连接
	 */
	@Override
	public void returnObject(Connection connection) {
		if (null != connection) {
			try {
				pool.returnObject(connection);
			} catch (Exception e) {
				throw new GFSException(e, e.getMessage());
			}
		}
	}

	/**
	 * 验证连接
	 */
	@Override
	public void invalidateObject(Connection connection) {
		if (null != connection) {
			try {
				pool.invalidateObject(connection);
			} catch (Exception e) {
				throw new GFSException(e, e.getMessage());
			}
		}
	}
	
	/**
	 * 关闭连接池
	 */
	public void close () {
		try {
			pool.close();
		} catch (Exception e) {
			throw new GFSException(e, e.getMessage());
		}
	}
	
	/**
	 * 获取连接池中可用的连接数
	 * @return
	 */
	public int getActiveNum () {
		return pool.getNumActive();
	}

}
