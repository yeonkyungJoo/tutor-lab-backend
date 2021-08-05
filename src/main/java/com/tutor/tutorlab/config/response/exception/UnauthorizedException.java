package com.tutor.tutorlab.config.response.exception;

import com.tutor.tutorlab.config.response.ErrorCode;

public class UnauthorizedException extends GlobalException {

    public UnauthorizedException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
