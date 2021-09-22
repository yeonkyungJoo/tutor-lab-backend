package com.tutor.tutorlab.modules.account.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {

    ADMIN("ADMIN", "관리자"),
    TUTOR("TUTOR", "튜터"),
    TUTEE("TUTEE", "튜티");

    private String type;
    private String name;
}
