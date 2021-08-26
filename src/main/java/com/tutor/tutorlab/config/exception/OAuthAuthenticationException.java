package com.tutor.tutorlab.config.exception;

import com.tutor.tutorlab.config.response.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class OAuthAuthenticationException extends AuthenticationException {

    private ErrorCode errorCode;

    public OAuthAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
        this.errorCode = ErrorCode.UNAUTHENTICATED;
    }

    public OAuthAuthenticationException(String msg) {
        super(msg);
        this.errorCode = ErrorCode.UNAUTHENTICATED;
    }
}
