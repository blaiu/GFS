package com.gome.cloud.protocol;

/**
 * 
 * @author blaiu
 *
 */
public interface Procotol {

	/**
	 * 获取缺省端口,当用户没有配置的时候
	 * @return
	 */
	int getDefaultPort();
	
	/**
	 * 通过key得到内容
	 * @param key
	 * @return
	 */
	byte[] readBytes(String key);
	
	/**
	 * 数据保存，返回key
	 * @param data
	 * @return
	 */
	String writeBytes(byte[] data);
	
	/**
	 * 释放协议
	 */
	void destroy();
}
