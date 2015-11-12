/**
 * 
 */
package com.gome.cloud.msg;

import java.io.IOException;
import java.io.InputStream;

import com.gome.cloud.exception.GFSException;
import com.gome.cloud.protocol.ProtocolUtil;
import com.google.common.io.ByteStreams;

/**
 * @author blaiu
 *
 */
public class Response extends Message {

	private byte[] bodyByte;
	
	public Response(InputStream is) throws IOException {
		ByteStreams.readFully(is, head);
        body = is;
	}

	public void process() throws IOException {
        contentLenght = ProtocolUtil.processHead(head);
        if (contentLenght == -2) {
            contentLenght = ProtocolUtil.byte2int(head[4], head[5], head[6], head[7]);
            byte[] buff = new byte[contentLenght];
            try {
                ByteStreams.readFully(getInput(), buff);
            } catch (IOException e) {

            }
            throw new GFSException("Data server reply a error code for this operating.Error message:" + new String(buff));
        }
        if (contentLenght == -1) {
            throw new GFSException("Bad MAGIC byte.The operation failed due to an unknown error.");
        }
        if (contentLenght > -1) {
            this.body = ByteStreams.limit(body, contentLenght);
        }
        bodyByte = new byte[contentLenght];
        ByteStreams.readFully(getInput(), bodyByte);
    }

    public int getContentLenght() {
        return contentLenght;
    }
	
	@Override
	public InputStream getInput() throws IOException {
		return new InputStream() {
            @Override
            public int read() throws IOException {
                int b = body.read();
                if (b != -1) {
                    crc32.update(b);
                }
                return b;

            }

            @Override
            public long skip(long n) throws IOException {
                return body.skip(n);
            }

            @Override
            public int available() throws IOException {
                return body.available();
            }

            @Override
            public int read(byte[] b) throws IOException {
                int len = body.read(b);
                if (len != -1) {
                    crc32.update(b, 0, len);
                }
                return len;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                len = body.read(b, off, len);
                if (len != -1) {
                    crc32.update(b, off, len);
                }
                return len;
            }

            @Override
            public void close() throws IOException {
                release();
            }

            @Override
            public synchronized void mark(int readlimit) {
                body.mark(readlimit);
            }

            @Override
            public synchronized void reset() throws IOException {
                body.reset();
                crc32.reset();
            }

            @Override
            public boolean markSupported() {
                return body.markSupported();
            }
        };
	}

	@Override
	public void release() {
		
	}

	public byte[] getBodyByte() {
		return bodyByte;
	}

	public void setBodyByte(byte[] bodyByte) {
		this.bodyByte = bodyByte;
	}
	
	
	
}
