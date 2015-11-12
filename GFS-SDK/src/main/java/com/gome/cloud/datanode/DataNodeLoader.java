/**
 * 
 */
package com.gome.cloud.datanode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gome.cloud.common.Configure;
import com.gome.cloud.exception.GFSException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * @author blaiu
 *
 */
public class DataNodeLoader {

	private static Logger logger = LoggerFactory.getLogger(DataNodeLoader.class);
	
	private final static String DN_PATH = "/gomefs-root/tfnode";
	private final static int ZK_SESSION_TIMEOUT_MS = 10000;
	
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	/** DataNode cache */
	private List<DataNode> dataNodeCache = Lists.newArrayList();
	
	/** zk */
	private ZooKeeper zookeeper;
	
	/** zk watcher */
	private Watcher watcher;
	
	/** config */
	private Configure configure;
	
	public DataNodeLoader(Configure configure, Watcher watcher) throws Exception {
		this.configure = configure;
		this.watcher = watcher;
		connection();
	}
	
	/**
	 * 连接ZK
	 * @throws Exception
	 */
	private void connection() throws Exception {
		Assert.assertNotNull(configure.getServers());
		final String servers = Joiner.on(",").join(configure.getServers());
		zookeeper = new ZooKeeper(servers, ZK_SESSION_TIMEOUT_MS, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				countDownLatch.countDown();
			}
		});
		
		/**
		 * 如果设置了ZK连接等待时间, 在超过等待时间还没有连接上的时候抛出异常
		 */
		if (configure.getMaxZkConnWaitTimeoutMs() <= 0) {
			countDownLatch.await();
		} else {
			boolean result = countDownLatch.await(configure.getMaxZkConnWaitTimeoutMs(), TimeUnit.MILLISECONDS);
			if (!result) {
				countDownLatch.countDown();
				throw new GFSException("Unable to connect to zk server {} before the specified waiting time ({}ms) elapses");
			}
		}
		logger.info("Successfully connected to ZK.");
	}
	
	/**
	 * 获取ZK 中的 DataNode数据
	 * @return
	 */
	public List<DataNode> getDataNodes() {
		List<String> paths = null;
		try {
			paths = zookeeper.getChildren(DN_PATH, watcher);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			logger.warn(e.getMessage());
			return dataNodeCache;
		}
		List<DataNode> dataNodes = new ArrayList<DataNode>(); 
		for (String path : paths) {
			try {
				byte[] dataByte = zookeeper.getData(DN_PATH + "/" + path, null, null);
				DataNode dataNode = DataNode.create(dataByte);
				dataNode.setId(Integer.valueOf(path));
				dataNodes.add(dataNode);
			} catch (Exception e) {
				logger.error("Bad path:{} from zk", DN_PATH + "/" + path, e);
				continue;
			}
		}
		
		/**
		 * 有主的话 保存 缓存节点信息
		 */
		for (DataNode dn : dataNodes) {
			if (dn.getId() % Configure.REPLICATION == 1) {
				dataNodeCache = dataNodes;
				return dataNodeCache;
			}
		}
		
		if (null == dataNodeCache || dataNodeCache.isEmpty()) {
			dataNodeCache = dataNodes;
			return dataNodeCache;
		} else {
			return dataNodeCache;
		}
	}
	
	public synchronized void shutdown() {
		if (null != zookeeper) {
			try {
				zookeeper.close();
				zookeeper = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
