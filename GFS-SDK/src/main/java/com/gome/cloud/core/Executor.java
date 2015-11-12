/**
 * 
 */
package com.gome.cloud.core;

import com.gome.cloud.msg.Request;
import com.gome.cloud.msg.Response;

/**
 * @author blaiu
 *
 */
public interface Executor {

	public Response execute(Request req); 
	
	public Response execute(int id, Request req);
	
	public void shutdown();
	
}
