package com.gome.cloud.img.exception;


public class UploadException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private MessageEnum messageEnum;
  

	public UploadException(String message) {
        super(message);
    }

    public UploadException(Throwable e) {
        super(e);
    }
	public MessageEnum getMessageEnum() {
		return messageEnum;
	}

	public UploadException(MessageEnum messageEnum) {
        super(messageEnum.toString());
        this.messageEnum = messageEnum;
    }
    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
