package com.gome.cloud.img.exception;

public enum ErrorEnum {

    BadRequest("BadRequest", "Bad Request", 400),
    InternalError("InternalError","We encountered an internal error. Please try again.", 500), 
    NoSuchKey("NoSuchURL","The specified URL does not exist.", 404),
    InvalidArgument("InvalidArgument", "InvalidArgument", 400),
    MethodNotAllowed("MethodNotAllowed","The specified method is not allowed against this resource.",405),
    BadUpload("BadUpload", " Upload file is not valid ", 400);
    
    
    private String errorCode;
    private String description;
    private int httpCode;

    private ErrorEnum(String errorCode, String description, int httpCode) {
        this.errorCode = errorCode;
        this.description = description;
        this.httpCode = httpCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }

    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public String toString() {
        return errorCode;
    }

    public String toJson() {
        return String.format("{\"Code\":\"%s\",\"Msg\":\"%s\"}", errorCode, description);
    }
    
    public String toMessage() {
        return String.format("{\"Msg\":\"%s\"}", description);
    }
}
