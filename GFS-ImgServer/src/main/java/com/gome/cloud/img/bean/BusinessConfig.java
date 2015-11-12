package com.gome.cloud.img.bean;

/**
 * 业务配置
 * */
public class BusinessConfig {
	/** 业务 */
	private String businessName;
	
	/** 是否缩略图 */
	private int isThumbNail;
	
	/** 压缩 */
	private int compression;
	
	/** 水印 */
	private String waterMark;
	
	/** 图片水印 */
	private String waterMarkPic;
	
	/** 图片位置 */
	private String waterMarkPosition;
	
	/** 是否边框 */
	private int isBorder;
	
	/** 大小 */
	private int imgFramSize;
	
	/** 图片边框颜色 */
	private String imgBorderColor;
	
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public int getCompression() {
		return compression;
	}
	public void setCompression(int compression) {
		this.compression = compression;
	}
 
	public String getWaterMark() {
		return waterMark;
	}
	public void setWaterMark(String waterMark) {
		this.waterMark = waterMark;
	}
 
	public String getWaterMarkPosition() {
		return waterMarkPosition;
	}
	public void setWaterMarkPosition(String waterMarkPosition) {
		this.waterMarkPosition = waterMarkPosition;
	}
	public int getIsBorder() {
		return isBorder;
	}
	public void setIsBorder(int isBorder) {
		this.isBorder = isBorder;
	}
	public int getImgFramSize() {
		return imgFramSize;
	}
	public void setImgFramSize(int imgFramSize) {
		this.imgFramSize = imgFramSize;
	}
	public String getImgBorderColor() {
		return imgBorderColor;
	}
	public void setImgBorderColor(String imgBorderColor) {
		this.imgBorderColor = imgBorderColor;
	}
	public int getIsThumbNail() {
		return isThumbNail;
	}
	public void setIsThumbNail(int isThumbNail) {
		this.isThumbNail = isThumbNail;
	}
	public String getWaterMarkPic() {
		return waterMarkPic;
	}
	public void setWaterMarkPic(String waterMarkPic) {
		this.waterMarkPic = waterMarkPic;
	}
	
}
