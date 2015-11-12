package com.gome.cloud.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.gome.cloud.exception.GFSException;
import com.gome.cloud.protocol.ProtocolUtil;

/**
 * @author blaiu
 *
 */
public class Connection {

	private String host;
	
	private int port;
	
	private Socket socket;
	
	private InputStream inputStream;
	
	private OutputStream outputStream;
	
	private int timeout = ProtocolUtil.DEFAULT_TIMEOUT;
	
	public Connection(final String host, final int port, int timeout) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}
	
	
	public void connect() {
		if (!isConnected()) {
			try {
				socket = new Socket();
				socket.setTcpNoDelay(true);
				socket.connect(new InetSocketAddress(host, port), timeout);
				outputStream = socket.getOutputStream();
				inputStream = new BufferedInputStream(socket.getInputStream());
			} catch (IOException e) {
				throw new GFSException(e, "host:"+ host +" port:" + port + e.getMessage());
			}
		}
	}
	
	public boolean isConnected () {
		return socket != null 
				&& socket.isBound() 
				&& !socket.isClosed()
				&& socket.isConnected()
				&& !socket.isInputShutdown()
				&& !socket.isOutputShutdown();
	}
	
	public void disConnection () {
		if (isConnected()) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if (socket.isClosed()) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void flush () {
		try {
			outputStream.flush();
		} catch (IOException e) {
			throw new GFSException(e);
		}
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	
}
