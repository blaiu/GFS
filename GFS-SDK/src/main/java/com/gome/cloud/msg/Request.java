/**
 * 
 */
package com.gome.cloud.msg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;

/**
 * @author blaiu
 *
 */
public class Request extends Message {

	private int dsId;
	
	private String crcExpect;
	
	public Request(byte[] head, InputStream body) {
		this.head = head;
		this.body = body;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public InputStream getInput() throws IOException {
		return ByteStreams.join(new InputSupplier<ByteArrayInputStream> () {
			@Override
			public ByteArrayInputStream getInput() throws IOException {
				return new ByteArrayInputStream(head);
			}
		}, new InputSupplier<InputStream>() {
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
				};
			}
		}).getInput();
	}

	public boolean markSupported() {
        return this.body.markSupported();
    }

    public void mark(int readlimit) {
        this.body.mark(readlimit);
    }

    public void reset() throws IOException {
        this.body.reset();
        this.crc32.reset();
    }
	
	@Override
	public void release() {
		if (null != body) {
			try {
				body.close();
			} catch (IOException e) {
			}
		}
	}

	public int getDsId() {
		return dsId;
	}

	public void setDsId(int dsId) {
		this.dsId = dsId;
	}

	public String getCrcExpect() {
		return crcExpect;
	}

	public void setCrcExpect(String crcExpect) {
		this.crcExpect = crcExpect;
	}

	
	
}
