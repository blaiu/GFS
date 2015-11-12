/**
 * 
 */
package com.gome.cloud.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gome.cloud.common.Configure;
import com.gome.cloud.datanode.DataNode;
import com.gome.cloud.datanode.DataNodeLoadBalance;
import com.gome.cloud.exception.GFSException;
import com.gome.cloud.monitor.Monitor;
import com.gome.cloud.msg.Request;
import com.gome.cloud.msg.Response;
import com.gome.cloud.net.ConnectPool;
import com.gome.cloud.net.Connection;
import com.gome.cloud.net.DataSourceManager;
import com.google.common.io.ByteStreams;

/**
 * @author blaiu
 *
 */
public class ExecutorImpl implements Executor {

	private DataNodeLoadBalance balance;
	
	private DataSourceManager manager;
	
	private Monitor monitor;
	
	public ExecutorImpl(DataNodeLoadBalance balance, DataSourceManager manager, Monitor monitor) {
		this.balance = balance;
		this.manager = manager;
		this.monitor = monitor;
	}
	
	/**
	 * 写操作
	 */
	@Override
	public Response execute (Request req) {
		req.mark(0);
		DataNode node = balance.leastConnect();
		try {
			Response resp = execute(node, req);
			monitor.addWriteByte(req.getContentLenght());
			return resp;
		} catch (GFSException e) {
			monitor.addErrorWrite();
			if (req.markSupported()) {
				int i = 0;
				while ((i++) < Configure.MAXRETRY) {
					try {
						req.reset();
						DataNode node1 = balance.leastConnect();
						Response resp = execute(node1, req);
						monitor.addWriteByte(req.getContentLenght());
						return resp;
					} catch (GFSException ex) {
						monitor.addErrorWrite();
						continue;
					} catch (IOException ex) {
						break;
					}
				}
			}
			monitor.addFinalWriteError();
			throw e;
		}
	}

	@Override
	public Response execute (int id, Request req) {
		DataNode node = balance.select(id);
		try {
			Response resp = execute(node, req);
			monitor.addReadByte(resp.getContentLenght());
			return resp;
		} catch (GFSException e) {
			monitor.addErrorRead();
			for (int i = 0; i < Configure.REPLICATION; i++) {
				DataNode node1 = balance.forceSelect(id + i);
				if (null != node1) {
					try {
						req.reset();
						Response resp = execute(node, req);
						monitor.addReadByte(resp.getContentLenght());
						return resp;
					} catch (Exception ex) {
						monitor.addErrorRead();
						continue;
					}
				} else {
					continue;
				} 
			} 
			monitor.addFinalReadError();
			throw e;
		}
	}
	
	private Response execute (DataNode dn, Request req) {
		req.setDsId(dn.getId());
		ConnectPool pool = manager.get(dn);
		Connection conn = pool.borrowObject();
		OutputStream os = conn.getOutputStream();
		long start = System.currentTimeMillis();
		try {
			ByteStreams.copy(req.getInput(), os);
			os.flush();
			
			InputStream is = conn.getInputStream();
			Response resp = new Response(is);
			resp.process();
			
			if (req.getCrcExpect() != null && !req.getCrcExpect().equalsIgnoreCase(Long.toHexString(resp.getCrc32().getValue()))) {
                throw new GFSException("The data returned by the server CRC validation failure.Client[" + req.getCrcExpect() + "],Server[" + Long.toHexString(resp.getCrc32().getValue()) + "]");
            }
			pool.returnObject(conn);
			return resp;
		} catch (IOException e) {
			pool.invalidateObject(conn);
			throw new GFSException(e, e.getMessage());
		} finally {
			switch (req.getCommand()) {
			case GET:
				monitor.addReadTime(System.currentTimeMillis() - start);
				break;
			case PUT:
				monitor.addWriteTime(System.currentTimeMillis() - start);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void shutdown() {
		balance.getHolder().shutdown();
		for (DataNode node : balance.getHolder().getDataNodes()) {
			manager.remove(node.getId());
		}
	}

}
