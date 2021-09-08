package com.tutor.tutorlab.config.exception;

import com.tutor.tutorlab.config.response.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class EntityNotFoundException extends GlobalException {

    public EntityNotFoundException(String message) {
        super(ErrorCode.ENTITY_NOT_FOUND, message);
    }

    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }

    public EntityNotFoundException(EntityType entityType) {
        super(ErrorCode.ENTITY_NOT_FOUND, entityType.getMessage());
    }

    @Getter
    @AllArgsConstructor
    public enum EntityType {
        TUTEE("존재하지 않는 튜티입니다."),
        TUTOR("존재하지 않는 튜터입니다."),
        LECTURE("존재하지 않는 강의입니다."),
        REVIEW("존재하지 않는 리뷰입니다."),
        CHATROOM("존재하지 않는 채팅방입니다.");

        private String message;
    }
}
