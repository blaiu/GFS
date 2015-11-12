package com.gome.cloud.img.exception;


public class ImageServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private ErrorEnum error;
    
    public ImageServerException(ErrorEnum errorEnum) {
        super(errorEnum.toString());
        this.error = errorEnum;
    }

    public ImageServerException(ErrorEnum errorEnum, Throwable e) {
        super(errorEnum.toString(), e);
        this.error = errorEnum;
    }

    public ImageServerException(ErrorEnum errorEnum, String message) {
        super(String.format("%s:%s", errorEnum, message));
        this.error = errorEnum;
    }

    public ImageServerException(ErrorEnum errorEnum, Exception e, String message) {
        super(String.format("%s:%s", errorEnum, message), e);
        this.error = errorEnum;
    }

    
    public ErrorEnum getError() {
		return error;
	}


	public ImageServerException(String message) {
        super(message);
    }

    public ImageServerException(Throwable e) {
        super(e);
    }
}
