/**
 * 
 */
package com.gome.cloud.datanode;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * DataNodeWatcher
 * @author blaiu
 *
 */
public class DataNodeWatcher implements Watcher {

	private DataNodeListener nodeListener;
	
	public DataNodeWatcher(DataNodeListener nodeListener) {
		this.nodeListener = nodeListener;
	}
	
	@Override
	public void process(WatchedEvent event) {
		nodeListener.dataNodeChange();
	}

}
