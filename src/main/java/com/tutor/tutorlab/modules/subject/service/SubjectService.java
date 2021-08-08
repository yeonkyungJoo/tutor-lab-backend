package com.tutor.tutorlab.modules.subject.service;

import com.tutor.tutorlab.modules.subject.ParentResponse;
import com.tutor.tutorlab.modules.subject.SubjectResponse;

import java.util.List;

public interface SubjectService {
    ParentResponse getParents();

    List<SubjectResponse> getSubjects(String parent);
}
