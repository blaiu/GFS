/**
 * 
 */
package com.gome.cloud.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.gome.cloud.common.Configure;
import com.gome.cloud.datanode.DataNodeHolder;
import com.gome.cloud.datanode.DataNodeLoadBalance;
import com.gome.cloud.exception.GFSException;
import com.gome.cloud.monitor.Monitor;
import com.gome.cloud.net.DataSourceManager;
import com.gome.cloud.protocol.DefaultProtocol;

/**
 * @author blaiu
 *
 */
public class GFS {

	private final static String defalutConfigName = "gfs-sdk-config.properties";
	
	private Configure configure = new Configure();
	private DefaultProtocol protocol;
	
	private static volatile GFS gfs;
	
	public static GFS getGFS () {
		if (null == gfs) {
			synchronized (GFS.class) {
				if (null == gfs) {
					gfs = new GFS();
				}
			}
		}
		return gfs;
	}
	
	public static ClassLoader findClassLoader() {
		return GFS.class.getClassLoader();
	}
	
	private GFS() {
		initConfig();
		init();
	}
	
	/**
	 * 加载配置文件
	 * @param configName
	 * @return
	 * @throws IOException 
	 */
	public Properties loadConfig(String configName) {
		if (StringUtils.isEmpty(configName)) {
			configName = defalutConfigName;
		}
		InputStream is = findClassLoader().getResourceAsStream(configName);
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new GFSException(e.getCause(), "gfs-sdk-config.properties is not exist ");
		}
		finally {
			try {
				if (null != is) {
					is.close();
				} 
			} catch (IOException e) {
			}
		}
		return properties;
	}
	
	private void initConfig() {
		Properties prop = loadConfig(defalutConfigName);
		String zkAddr = prop.getProperty("zkAddr");
		if (StringUtils.isEmpty(zkAddr)) {
			throw new GFSException("zookeeper address not found");
		}
		configure.setServers(zkAddr);
	}
	
	private void init() {
		DataSourceManager sourceManager = new DataSourceManager(configure);
		DataNodeHolder nodeHolder = new DataNodeHolder(configure);
		DataNodeLoadBalance balance = new DataNodeLoadBalance(sourceManager, nodeHolder);
		Monitor monitor = new Monitor();
		Executor executor = new ExecutorImpl(balance, sourceManager, monitor);
		protocol = new DefaultProtocol(executor, configure);
	}

	public byte[] readBytes(String key) {
        return protocol.readBytes(key);
    }

    public String writeBytes(byte[] data) {
        return protocol.writeBytes(data);
    }
	
}
