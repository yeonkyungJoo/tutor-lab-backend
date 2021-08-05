package com.tutor.tutorlab.config.response.exception;

import com.tutor.tutorlab.config.response.ErrorCode;

public class EntityNotFoundException extends GlobalException {

    public EntityNotFoundException(String message) {
        super(ErrorCode.ENTITY_NOT_FOUND, message);
    }

    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }
}
