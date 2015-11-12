package com.gome.cloud.img.exception;
/**
 * 上传文件消息
 * id=1：msg=key
 * id=0:
 * msg:
 * 1:授权码无效
 * 2:文件未识别
 * 3:上传图片过大
 * 4:获取异常，请重试
 * 5:上传失败，请重试
 * 6:重复名称
 * 7:crc验证失败
 * */
public enum MessageEnum {
	SUCCESS(1,""),
	AUTHCODEINVALID(0,"1"),			//授权码无效
	FILERECOGNIZED(0,"2"),			//文件未识别
	IMGTOOLARGER(0, "3"),   		//上传图片过大
	EXCEPTIONERROR(0,"4"), 			//获取异常，请重试
	FAIL(0,"5"),    				//上传失败，请重试
	RENAME(0,"6"),					//文件名已存在
	CRCERROR(0,"7");			//CRC验证失败
	
	private int id; 				//成功标识：1成功，0失败
	private String msg;  
	
	
	private MessageEnum(int id, String msg){
		this.id = id;
		this.msg = msg;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String toStrng(){
		 return String.format("\"id\":\"%s\",\"msg\":\"%s\"", id, msg);
	}
	public String toJson() {
        return String.format("{\"id\":\"%s\",\"msg\":\"%s\"}", id, msg);
    }
}
