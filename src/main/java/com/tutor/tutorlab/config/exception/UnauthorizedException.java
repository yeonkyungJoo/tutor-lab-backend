package com.tutor.tutorlab.config.exception;

import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.modules.account.enums.RoleType;

public class UnauthorizedException extends GlobalException {

    public UnauthorizedException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(RoleType roleType) {
        super(ErrorCode.UNAUTHORIZED, "해당 사용자는 " + roleType.getName() + "가 아닙니다.");
    }

}
