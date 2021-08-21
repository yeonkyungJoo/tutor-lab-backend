package com.tutor.tutorlab.config.response.exception;

import com.tutor.tutorlab.config.response.ErrorCode;

public class AlreadyExistException extends GlobalException {

    public AlreadyExistException(String message) {
        super(ErrorCode.ALREADY_EXIST, message);
    }

    public AlreadyExistException() {
        super(ErrorCode.ALREADY_EXIST);
    }
}
