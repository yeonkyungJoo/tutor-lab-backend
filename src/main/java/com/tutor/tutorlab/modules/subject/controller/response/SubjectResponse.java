package com.tutor.tutorlab.modules.subject.controller.response;

import lombok.Value;

@Value
public class SubjectResponse {
    private final String parent;

    private final String subject;

    private final String learningKind;
}
