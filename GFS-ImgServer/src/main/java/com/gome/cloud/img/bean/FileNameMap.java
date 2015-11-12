package com.gome.cloud.img.bean;

/**
 * 业务与文件系统key映射
 * */
public class FileNameMap {
	private int fileNameMapId;
	private String courseFileKey;
	private String businessName;
	private String gomefsKey;
	public String getCourseFileKey() {
		return courseFileKey;
	}
	public void setCourseFileKey(String courseFileKey) {
		this.courseFileKey = courseFileKey;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getGomefsKey() {
		return gomefsKey;
	}
	public void setGomefsKey(String gomefsKey) {
		this.gomefsKey = gomefsKey;
	}
	public int getFileNameMapId() {
		return fileNameMapId;
	}
	public void setFileNameMapId(int fileNameMapId) {
		this.fileNameMapId = fileNameMapId;
	}

}
