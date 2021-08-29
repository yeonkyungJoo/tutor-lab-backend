package com.tutor.tutorlab.config.exception;

import com.tutor.tutorlab.config.response.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private ErrorCode errorCode;
    // private String message;

    public GlobalException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GlobalException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
