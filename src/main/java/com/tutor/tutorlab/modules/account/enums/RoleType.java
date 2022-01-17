package com.tutor.tutorlab.modules.account.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

    ADMIN("ROLE_ADMIN", "관리자"),
    TUTOR("ROLE_TUTOR", "튜터"),
    TUTEE("ROLE_TUTEE", "튜티");

    private String type;
    private String name;
}
