package com.tutor.tutorlab.config.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    ALREADY_EXIST(400, "Already Exist"),
    ENTITY_NOT_FOUND(400, "Entity Not Found"),
    INVALID_INPUT(400, "Invalid Input"),
    UNAUTHORIZED(401, "Unauthorized"),
    UNAUTHENTICATED(401, "Unauthenticated");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
