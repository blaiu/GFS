/**
 * 
 */
package com.gome.cloud.common;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author blaiu
 *
 */
public class Configure {
	
	/** SDK 版本 */
	public final static String VERSION = "GomeFS-JAVA-SDK/1.0";
	
	/** 副本个数 */
	public static int REPLICATION = 3;
	
	/** 重试次数 */
	public final static int MAXRETRY = 10; 
	
	/** 最大活动连接数  负数不限制 */
	private int maxActive = 100;
	
	/** 当 连接数达到 maxActive时，0:抛出异常 1：阻塞 2：新建连接*/
	private int whenExhaustedAction = 1;
	
	/** 发生阻塞时等待时间 */
	private int maxWaitTimeMs = 2000;
	
	/** 最大空闲连接 */
	private int maxIdle = 80;
	
	/** 最小空闲连接 */
	private int minIdle = 0;
	
	/** 后台每隔多少秒进行连接池清理 */
	private int timeBetweenEvictionRunsMs = 10000;
	
	/** 每次清理个数 */
	private int numTestsPerEvictionRun = 5;
	
	/** 空闲链接时间大于此值， 连接将会关闭 */
	private int minEvictableIdleTimeMs = 1000;
	
	/** socket 连接超时时间 */
	private int socketTimeoutMs = 50000;
	
	/** zk的连接地址和端口 */
	private List<String> servers = Lists.newArrayList();
	
	/** zk 连接超时时间 */
	private int maxZkConnWaitTimeoutMs = -1;
	
	/** 监控地址 */
	private String monitorAddress = null;
	
	/** 是否开启压缩 */
	private boolean compression = false;

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getWhenExhaustedAction() {
		return whenExhaustedAction;
	}

	public void setWhenExhaustedAction(int whenExhaustedAction) {
		if(whenExhaustedAction < 0 || whenExhaustedAction > 2) {
			throw new IllegalArgumentException("whenExhaustedAction is less than 2 and more than 0");
		}
		this.whenExhaustedAction = whenExhaustedAction;
	}

	public int getMaxWaitTimeMs() {
		return maxWaitTimeMs;
	}

	public void setMaxWaitTimeMs(int maxWaitTimeMs) {
		this.maxWaitTimeMs = maxWaitTimeMs;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getTimeBetweenEvictionRunsMs() {
		return timeBetweenEvictionRunsMs;
	}

	public void setTimeBetweenEvictionRunsMs(int timeBetweenEvictionRunsMs) {
		this.timeBetweenEvictionRunsMs = timeBetweenEvictionRunsMs;
	}

	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public int getMinEvictableIdleTimeMs() {
		return minEvictableIdleTimeMs;
	}

	public void setMinEvictableIdleTimeMs(int minEvictableIdleTimeMs) {
		this.minEvictableIdleTimeMs = minEvictableIdleTimeMs;
	}

	public int getSocketTimeoutMs() {
		return socketTimeoutMs;
	}

	public void setSocketTimeoutMs(int socketTimeoutMs) {
		this.socketTimeoutMs = socketTimeoutMs;
	}

	public List<String> getServers() {
		return servers;
	}

	public void setServers(List<String> servers) {
		this.servers = servers;
	}
	
	public void setServers(String... servers) {
		this.setServers(Lists.newArrayList(servers));
	}

	public int getMaxZkConnWaitTimeoutMs() {
		return maxZkConnWaitTimeoutMs;
	}

	public void setMaxZkConnWaitTimeoutMs(int maxZkConnWaitTimeoutMs) {
		this.maxZkConnWaitTimeoutMs = maxZkConnWaitTimeoutMs;
	}

	public String getMonitorAddress() {
		return monitorAddress;
	}

	public void setMonitorAddress(String monitorAddress) {
		this.monitorAddress = monitorAddress;
	}

	public boolean isCompression() {
		return compression;
	}

	public void setCompression(boolean compression) {
		this.compression = compression;
	}
	
	
	
}
