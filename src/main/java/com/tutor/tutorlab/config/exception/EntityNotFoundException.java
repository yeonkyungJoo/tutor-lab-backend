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

        USER("존재하지 않는 사용자입니다."),
        NOTIFICATION("존재하지 않는 알림입니다."),
        TUTEE("존재하지 않는 튜티입니다."),
        TUTOR("존재하지 않는 튜터입니다."),
        CAREER("존재하지 않는 데이터입니다."),
        EDUCATION("존재하지 않는 데이터입니다."),
        LECTURE("존재하지 않는 강의입니다."),
        LECTURE_PRICE("존재하지 않는 데이터입니다."),
        PICK("존재하지 않는 내역입니다."),
        ENROLLMENT("수강 내역이 존재하지 않습니다."),
        CANCELLATION("취소 내역이 존재하지 않습니다."),
        REVIEW("존재하지 않는 리뷰입니다."),
        CHATROOM("존재하지 않는 채팅방입니다.");

        private String message;
    }
}
