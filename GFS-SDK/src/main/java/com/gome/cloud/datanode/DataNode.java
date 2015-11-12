/**
 * 
 */
package com.gome.cloud.datanode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gome.cloud.common.Configure;
import com.gome.cloud.exception.GFSException;

/**
 * @author blaiu
 *
 */
public class DataNode {

	/** ID */
	private int id;
	
	/** IP */
	private String ip;
	
	/** PORT */
	private int port;
	
	/** WEIGHT */
	private long weight;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getWeight() {
		return weight;
	}

	public void setWeight(long weight) {
		this.weight = weight;
	}
	
	public boolean isMaster() {
		return id % Configure.REPLICATION == 1;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("");
		builder.append("{");
		builder.append(" ID:" + id);
		builder.append(" IP:" + ip);
		builder.append(" PORT:" + port);
		builder.append(" WEIGHT:" + weight);
		builder.append("}");
		return builder.toString();
	}
	
	private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),(-?\\d+)\\|(\\d+)$");
	
	public static DataNode create(byte[] bytes) {
		String data = new String(bytes);
		Matcher matcher = PATTERN.matcher(data);
		if (matcher.find()) {
			DataNode dn = new DataNode();
			dn.setIp(matcher.group(1));
			dn.setPort(Integer.valueOf(matcher.group(2)));
			dn.setWeight(Long.valueOf(matcher.group(3)));
			return dn;
		} else {
			throw new GFSException("Bad datanode info:" + data);
		}
	}
	
}
