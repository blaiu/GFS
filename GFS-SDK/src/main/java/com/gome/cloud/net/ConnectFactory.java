package com.gome.cloud.net;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * 
 * @author blaiu
 *
 */
public class ConnectFactory extends BasePoolableObjectFactory<Connection>{

	private String host;
	
	private int port;
	
	private int timeout;
	
	public ConnectFactory(String host, int port, int timeout) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}
	
	/**
	 * 创建连接
	 */
	@Override
	public Connection makeObject() throws Exception {
		Connection connection = new Connection(host, port, timeout);
		connection.connect();
		return connection;
	}

	/**
	 * 断开连接
	 */
	@Override
	public void destroyObject(Connection connection) throws Exception {
		connection.disConnection();
	}

	/**
	 * 验证连接是否可用
	 */
	@Override
	public boolean validateObject(Connection connection) {
		return connection.isConnected();
	}

	
	
}
