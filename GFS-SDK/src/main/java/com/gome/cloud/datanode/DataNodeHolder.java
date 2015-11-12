/**
 * 
 */
package com.gome.cloud.datanode;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.gome.cloud.common.Configure;
import com.gome.cloud.exception.GFSException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;

/**
 * @author blaiu
 *
 */
public class DataNodeHolder implements DataNodeListener {

	private DataNodeLoader dataNodeLoader;
	
	private LoadingCache<String, List<DataNode>> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(120, TimeUnit.SECONDS)
			.maximumSize(1)
			.build(new CacheLoader<String, List<DataNode>>() {
				@Override
				public List<DataNode> load(String key) throws Exception {
					return ImmutableList.copyOf(dataNodeLoader.getDataNodes());
				}
			});
	
	
	@Override
	public void dataNodeChange() {
		cache.put("dataNodes", ImmutableList.copyOf(dataNodeLoader.getDataNodes()));
	}
	
	public DataNodeHolder(Configure configure) {
		try {
			dataNodeLoader = new DataNodeLoader (configure, new DataNodeWatcher(this));
		} catch (Exception e) {
			throw new GFSException(e, e.getMessage());
		}
	}
	
	public List<DataNode> getDataNodes () {
		try {
			return cache.get("dataNodes");
		} catch (ExecutionException e) {
			throw new GFSException(e, e.getMessage());
		}
	}
	
	public void shutdown () {
		this.dataNodeLoader.shutdown();
		this.cache.invalidateAll();
	}

}
