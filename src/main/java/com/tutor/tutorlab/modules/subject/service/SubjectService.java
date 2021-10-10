package com.tutor.tutorlab.modules.subject.service;

import com.tutor.tutorlab.modules.subject.controller.response.LearningKindResponse;
import com.tutor.tutorlab.modules.subject.controller.response.SubjectResponse;

import java.util.List;

public interface SubjectService {

    List<LearningKindResponse> getLearningKindResponses();

    List<SubjectResponse> getSubjectResponses();

    List<SubjectResponse> getSubjectResponses(Long learningKindId);
}
