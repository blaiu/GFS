package com.gome.cloud.protocol;

import java.io.IOException;
import java.io.InputStream;

import com.gome.cloud.core.Executor;
import com.gome.cloud.exception.GFSException;
import com.gome.cloud.msg.Request;
import com.gome.cloud.msg.Response;

/**
 * abstract ProtocolSupport
 * @author blaiu
 *
 */
public abstract class AbstractProtocol implements Procotol {
	
	protected Executor executor;
	
	public AbstractProtocol(Executor executor) {
		this.executor = executor;
	}
	
	/**
	 * 写文件
	 * @param head
	 * @param in
	 * @return
	 */
	public String write(byte[] head, InputStream in) {
		Request req = new Request(head, in);
		req.setContentLenght(ProtocolUtil.processHead(head));
		Response resp = executor.execute(req);
		String key = new String(resp.getBodyByte());
	
		String crc = key.substring(key.lastIndexOf("/" + 1));
		if (req.getCrc32().getValue() != Long.parseLong(crc, 16)) {
			throw new GFSException("The CRC32 did not match what client send. client[" + Long.toHexString(req.getCrc32().getValue()) + "],server[" + crc + "]");
		}
		StringBuilder sb = new StringBuilder("");
		sb.append(req.getDsId())
		.append("/")
		.append(key);
		return sb.toString();
	} 
	
	/**
	 * 读文件
	 * @param dnId
	 * @param head
	 * @param body
	 * @return
	 */
	public InputStream get(int dnId, byte[] head, InputStream body) {
		Request req = new Request(head, body);
		req.setContentLenght(ProtocolUtil.processHead(head));
		Response resp = executor.execute(dnId, req);
		try {
			return resp.getInput();
		} catch (IOException e) {
			throw new GFSException(e, e.getMessage());
		}
	}
	
	/**
	 * 返回文件字节
	 * @param dnId
	 * @param head
	 * @param body
	 * @param crc
	 * @return
	 */
	public byte[] getBytes(int dnId, byte[] head, InputStream body, String crc) {
		Request req = new Request(head, body);
		req.setCrcExpect(crc);
		req.setContentLenght(ProtocolUtil.processHead(head));
		Response resp = executor.execute(dnId, req);
		return resp.getBodyByte();
	}
	
	/**
	 * 删除文件
	 * @param dnId
	 * @param head
	 * @param in
	 */
	public void remove(int dnId, byte[] head, InputStream in) {
//		Request req = new Request(head, in);
//		Response resp = executor.execute(dnId, req);
//		resp.release();
	}

	
	
}
