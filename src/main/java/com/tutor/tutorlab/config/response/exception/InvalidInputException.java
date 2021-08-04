package com.tutor.tutorlab.config.response.exception;

import com.tutor.tutorlab.config.response.ErrorCode;

public class InvalidInputException extends GlobalException {

    public InvalidInputException(String message) {
        super(ErrorCode.INVALID_INPUT, message);
    }

    public InvalidInputException() {
        super(ErrorCode.INVALID_INPUT);
    }
}
