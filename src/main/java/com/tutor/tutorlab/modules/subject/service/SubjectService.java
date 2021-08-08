package com.tutor.tutorlab.modules.subject.service;

import com.tutor.tutorlab.modules.subject.controller.response.ParentResponse;
import com.tutor.tutorlab.modules.subject.controller.response.SubjectResponse;

import java.util.List;

public interface SubjectService {
    ParentResponse getParents();

    List<SubjectResponse> getSubjects(String parent);
}
