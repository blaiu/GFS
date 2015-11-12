/**
 * 
 */
package com.gome.cloud.monitor;

/**
 * @author blaiu
 *
 */
public class Monitor {

	Value value = new Value();
	
	public void addReadByte(long v) {
        value.readByte.addAndGet(v);
        value.fileRead.addAndGet(1);
    }

    public void addWriteByte(long v) {
        value.writeByte.addAndGet(v);
        value.fileWrite.addAndGet(1);
    }

    public void addErrorRead() {
        value.errorRead.addAndGet(1);
    }

    public void addErrorWrite() {
        value.errorWrite.addAndGet(1);
    }

    public void addReadTime(long v) {
        value.readTime.addAndGet(v);
    }

    public void addWriteTime(long v) {
        value.writeTime.addAndGet(v);
    }

    public void addFinalReadError() {
        value.finalReadError.addAndGet(1);
    }

    public void addFinalWriteError() {
        value.finalWriteError.addAndGet(1);
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("\"FilesRead\":")
        	.append(value.fileRead)
        	.append(",\"ReadByte\":")
        	.append(value.readByte);
        sb.append(",\"TotalReadTime\":")
        	.append((value.readTime.get()))
        	.append(",\"ErrorRead\":")
            .append(value.errorRead);
        sb.append(",\"FinalReadError\":")
        	.append(value.finalReadError);
        sb.append(",\"FilesWrite\":")
        	.append(value.fileWrite)
        	.append(",\"WriteByte\":")
        	.append(value.writeByte);
        sb.append(",\"TotalWriteTime\":")
        	.append((value.writeTime.get()))
        	.append(",\"ErrorWrite\":")
            .append(value.errorWrite);
        sb.append(",\"FinalWriteError\":")
        	.append(value.finalWriteError);
        return sb.toString();
	}
    
    
    
}
