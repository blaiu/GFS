/**
 * 
 */
package com.gome.cloud.msg;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import com.gome.cloud.protocol.ProtocolUtil;
import com.gome.cloud.protocol.ProtocolUtil.Command;

/**
 * @author blaiu
 *
 */
public abstract class Message {

	protected byte[] head = new byte[20];
    protected InputStream body;
    
    protected int contentLenght;
    protected CRC32 crc32 = new CRC32();
    
    public abstract InputStream getInput() throws IOException;

    public abstract void release();

	public byte[] getHead() {
		return head;
	}

	public void setHead(byte[] head) {
		this.head = head;
	}

	public InputStream getBody() {
		return body;
	}

	public void setBody(InputStream body) {
		this.body = body;
	}

	public int getContentLenght() {
		return contentLenght;
	}

	public void setContentLenght(int contentLenght) {
		this.contentLenght = contentLenght;
	}

	public CRC32 getCrc32() {
		return crc32;
	}

	public void setCrc32(CRC32 crc32) {
		this.crc32 = crc32;
	}
	
	public Command getCommand() {
		return ProtocolUtil.getCommand(head);
	}
    
    
	
}
