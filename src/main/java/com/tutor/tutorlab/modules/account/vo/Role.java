package com.tutor.tutorlab.modules.account.vo;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_USER("ROLE_USER", "사용자"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_TUTOR("ROLE_TUTOR", "튜터"),
    ROLE_TUTEE("ROLE_TUTEE", "튜티");

    private final String role;
    private final String role_ko;
}
