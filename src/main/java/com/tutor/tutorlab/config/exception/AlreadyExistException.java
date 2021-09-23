package com.tutor.tutorlab.config.exception;

import com.tutor.tutorlab.config.response.ErrorCode;

public class AlreadyExistException extends GlobalException {

    public static final String ID = "동일한 ID가 존재합니다.";

    public AlreadyExistException(String message) {
        super(ErrorCode.ALREADY_EXIST, message);
    }

    public AlreadyExistException() {
        super(ErrorCode.ALREADY_EXIST);
    }
}
