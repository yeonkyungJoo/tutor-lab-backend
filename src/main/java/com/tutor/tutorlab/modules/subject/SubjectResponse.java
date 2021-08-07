package com.tutor.tutorlab.modules.subject;

import lombok.Value;

@Value
public class SubjectResponse {
    private final String parent;

    private final String enSubject;

    private final String krSubject;

    private final String learningKind;
}
