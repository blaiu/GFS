/**
 * 
 */
package com.gome.cloud.exception;

/**
 * 异常
 * @author blaiu
 *
 */
public class GFSException extends RuntimeException {

	private static final long serialVersionUID = 6469068311314903387L;

	public GFSException(String message) {
		super(message);
	}
	
	public GFSException(Throwable e) {
		super(e);
	}
	
	public GFSException(Throwable cause, String message) {
		super(message, cause);
	}
}
