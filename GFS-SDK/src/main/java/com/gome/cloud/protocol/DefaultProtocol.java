/**
 * 
 */
package com.gome.cloud.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.gome.cloud.common.Configure;
import com.gome.cloud.core.Executor;
import com.gome.cloud.exception.GFSException;
import com.gome.cloud.msg.Request;
import com.gome.cloud.msg.Response;

/**
 * @author blaiu
 *
 */
public class DefaultProtocol extends AbstractProtocol {

	 private Configure configure;

	 public DefaultProtocol(Executor executor, Configure configure) {
		 super(executor);
		 this.configure = configure;
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
	
		String crc = key.substring(key.lastIndexOf("/") + 1);
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

	@Override
	public int getDefaultPort() {
		return 0;
	}

	@Override
	public byte[] readBytes(String key) {
		KeyUtil.verification(key);
        String fileId = KeyUtil.getFileId(key);
        boolean compress = KeyUtil.isCompression(key);
        byte[] head = ProtocolUtil.createHead(ProtocolUtil.Command.GET, ProtocolUtil.writeInt(fileId.length()));
        ByteArrayInputStream body = new ByteArrayInputStream(fileId.getBytes());
        int dsId = KeyUtil.getDsId(key);
        byte[] data = getBytes(dsId, head, body, KeyUtil.getCrc(key));
        if (compress) {
            return CompressUtil.decompress(data);
        } else {
            return data;
        }
	}

	@Override
	public String writeBytes(byte[] data) {
		if (this.configure.isCompression()) {
            byte[] compressData = CompressUtil.compress(data);
            String key = write(compressData.length, new ByteArrayInputStream(compressData));
            String cheaksum = KeyUtil.createChecksum(key, true);
            return String.format("gomefs/t%s%sY", key, cheaksum);
        } else {
            String key = write(data.length, new ByteArrayInputStream(data));
            String cheaksum = KeyUtil.createChecksum(key, false);
            return String.format("gomefs/t%s%sN", key, cheaksum);
        }
	}
	
	public String write(long length, InputStream is) {
        if (length > 32 * 1024 * 1024) {
            throw new GFSException("Your proposed upload exceeds the maximum allowed size.");
        }
        byte[] head = ProtocolUtil.createHead(ProtocolUtil.Command.PUT, ProtocolUtil.writeInt(length));
        return write(head, is);
    }

	@Override
	public void destroy() {
		executor.shutdown();
	}

}
