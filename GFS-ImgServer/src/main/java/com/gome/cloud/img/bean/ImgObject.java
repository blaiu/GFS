package com.gome.cloud.img.bean;

import org.springframework.http.HttpHeaders;

public class ImgObject {

	private boolean isOriginal = true; // 是否原图

	private String newUrl;
	private String reqUrl;
	private String gomefsKey;
	private String bussinessName;
	private int isThumbNail;
	// private WaterMarkEnum waterMark = WaterMarkEnum.WATERMARK_NONE;
	private String waterMark;
	private String waterMarkPic;
	private int isPressed;
	private int isBorder;
	private String borderColor = "";
	private int borderSize;
	private int radius;
	private float cutRate;
	private int isCut;
	private String waterMarkPos = "";
	private int quality;
	private int hopeWidth;
	private int hopeHeight;
	private HttpHeaders headers = new HttpHeaders();
	private byte[] zoomData;
	private String sourceKey;
	private int isWebp;
	private boolean isGomefsKey;
	private boolean isHtml;
	private String labelStr; // 标签字符串
	private String labelPosStr; // 标签位置字符串

	// private int label_30 = 0; //30天标签
	// private int label_180 = 0; //180天标签
	// private String label_30_pos ;
	// private String label_180_pos ;
	public String toNullString(){
		return "0||0||0||0||0||0||0||0||0||0||0||0||0||0||0";
	}
	public String toParaString() {

		StringBuilder waterAndLabel = new StringBuilder(); // 水印标签名称
		StringBuilder waterAndLabelPos = new StringBuilder(); // 水印标签位置

		// 处理水印名称及位置，默认位置九宫格 5
		if (waterMark != null && !waterMark.equals("") && !waterMark.equals("0")) {
			waterAndLabel.append(waterMarkPic);
			if (waterMarkPos != null && !waterMarkPos.equals("") && !waterMarkPos.equals("0")) {
				waterAndLabelPos.append(waterMarkPos);
			} else {
				waterAndLabelPos.append("5");
			}
		}

		// 处理标签名称及位置
		if (labelStr != null && !labelStr.equals("")) {
			if (waterAndLabel.toString().equals("")) {
				waterAndLabel.append(labelStr);
				waterAndLabelPos.append(labelPosStr);

			} else {
				waterAndLabel.append("-").append(labelStr);
				waterAndLabelPos.append("-").append(labelPosStr);
			}
		}
		// 如果都为空默认0
		if (waterAndLabel.toString().equals("")) {
			waterAndLabel.append("0");
			waterAndLabelPos.append("0");
		}

		return String.valueOf(bussinessName) + "||"
				+ String.valueOf(isThumbNail) + "||"
				+ String.valueOf(isPressed) + "||" + String.valueOf(quality)
				+ "||" + String.valueOf(isBorder) + "||" + borderColor + "||"
				+ borderSize + "||" + String.valueOf(radius) + "||" + isCut
				+ "||" + cutRate + "||" + waterAndLabel.toString() + "||"
				+ waterAndLabelPos.toString() + "||"
				+ String.valueOf(hopeWidth) + "||" + String.valueOf(hopeHeight)
				+ "||" + isWebp;
	}

	public boolean isOriginal() {
		return isOriginal;
	}

	public void setOriginal(boolean isOriginal) {
		this.isOriginal = isOriginal;
	}

	public String getNewUrl() {
		return newUrl;
	}

	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getGomefsKey() {
		return gomefsKey;
	}

	public void setGomefsKey(String gomefsKey) {
		this.gomefsKey = gomefsKey;
	}

	public String getBussinessName() {
		return bussinessName;
	}

	public void setBussinessName(String bussinessName) {
		this.bussinessName = bussinessName;
	}

	public int getIsThumbNail() {
		return isThumbNail;
	}

	public void setIsThumbNail(int isThumbNail) {
		this.isThumbNail = isThumbNail;
	}

	// public WaterMarkEnum getWaterMark() {
	// return waterMark;
	// }
	//
	// public void setWaterMark(WaterMarkEnum waterMark) {
	// this.waterMark = waterMark;
	// }

	public int getIsPressed() {
		return isPressed;
	}

	public void setIsPressed(int isPressed) {
		this.isPressed = isPressed;
	}

	public int getIsBorder() {
		return isBorder;
	}

	public void setIsBorder(int isBorder) {
		this.isBorder = isBorder;
		if (isBorder == 1) {
			this.isOriginal = false; // 设置为非原图
		}
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public int getBorderSize() {
		return borderSize;
	}

	public void setBorderSize(int borderSize) {
		this.borderSize = borderSize;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
		this.isOriginal = false; // 设置为非原图
	}

	public float getCutRate() {
		return cutRate;
	}

	public void setCutRate(float cutRate) {
		this.cutRate = cutRate;
		this.isOriginal = false; // 设置为非原图
	}

	public int getIsCut() {
		return isCut;
	}

	public void setIsCut(int isCut) {
		this.isCut = isCut;
	}

	public String getWaterMarkPos() {
		return waterMarkPos;
	}

	public void setWaterMarkPos(String waterMarkPos) {
		this.waterMarkPos = waterMarkPos;
		this.isOriginal = false; // 设置为非原图
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
		this.isOriginal = false; // 设置为非原图
	}

	public int getHopeWidth() {
		return hopeWidth;
	}

	public void setHopeWidth(int hopeWidth) {
		this.hopeWidth = hopeWidth;
		this.isThumbNail = 1;
		this.isOriginal = false; // 设置为非原图
	}

	public int getHopeHeight() {
		return hopeHeight;
	}

	public void setHopeHeight(int hopeHeight) {
		this.hopeHeight = hopeHeight;
		this.isThumbNail = 1;
		this.isOriginal = false; // 设置为非原图
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	public byte[] getZoomData() {
		return zoomData;
	}

	public void setZoomData(byte[] zoomData) {
		this.zoomData = zoomData;
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	public boolean getIsGomefsKey() {
		return isGomefsKey;
	}

	public void setIsGomefsKey(boolean isGomefsKey) {
		this.isGomefsKey = isGomefsKey;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	//
	// public int getLabel_30() {
	// return label_30;
	// }
	//
	// public void setLabel_30(int label_30) {
	// this.label_30 = label_30;
	// this.isOriginal = false; //设置为非原图
	// }
	//
	// public int getLabel_180() {
	// return label_180;
	// }
	//
	// public void setLabel_180(int label_180) {
	// this.label_180 = label_180;
	// this.isOriginal = false; //设置为非原图
	// }
	//
	// public String getLabel_30_pos() {
	// return label_30_pos;
	// }
	//
	// public void setLabel_30_pos(String label_30_pos) {
	// this.label_30_pos = label_30_pos;
	// }
	//
	// public String getLabel_180_pos() {
	// return label_180_pos;
	// }
	//
	// public void setLabel_180_pos(String label_180_pos) {
	// this.label_180_pos = label_180_pos;
	// }

	public void setWaterMark(String waterMark) {
		this.waterMark = waterMark;
	}

	public void setGomefsKey(boolean isGomefsKey) {
		this.isGomefsKey = isGomefsKey;
	}

	public String getLabelStr() {
		return labelStr;
	}

	public void setLabelStr(String labelStr) {
		this.labelStr = labelStr;
	}

	public String getLabelPosStr() {
		return labelPosStr;
	}

	public void setLabelPosStr(String labelPosStr) {
		this.labelPosStr = labelPosStr;
	}

	public String getWaterMark() {
		return waterMark;
	}

	public int getIsWebp() {
		return isWebp;
	}

	public void setIsWebp(int isWebp) {
		this.isWebp = isWebp;
	}

	public String getWaterMarkPic() {
		return waterMarkPic;
	}

	public void setWaterMarkPic(String waterMarkPic) {
		this.waterMarkPic = waterMarkPic;
	}

}
