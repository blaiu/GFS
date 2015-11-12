package com.gome.cloud.img.bean;
/**
 * 文件删除/或变更
 * */
public class FileDelRecord {
	private int gomeFsDelRecordId;
	private String businessName;
	private String sourceURI;
	private String gomefsURI;
	private String flag;   					//1删除；2变更业务
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getSourceURI() {
		return sourceURI;
	}
	public void setSourceURI(String sourceURI) {
		this.sourceURI = sourceURI;
	}
	public String getGomefsURI() {
		return gomefsURI;
	}
	public void setGomefsURI(String gomefsURI) {
		this.gomefsURI = gomefsURI;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public int getGomeFsDelRecordId() {
		return gomeFsDelRecordId;
	}
	public void setGomeFsDelRecordId(int gomeFsDelRecordId) {
		this.gomeFsDelRecordId = gomeFsDelRecordId;
	}
	
}
