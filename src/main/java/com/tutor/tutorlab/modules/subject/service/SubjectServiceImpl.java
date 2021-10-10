package com.tutor.tutorlab.modules.subject.service;

import com.tutor.tutorlab.modules.lecture.embeddable.LearningKind;
import com.tutor.tutorlab.modules.subject.controller.response.LearningKindResponse;
import com.tutor.tutorlab.modules.subject.controller.response.SubjectResponse;
import com.tutor.tutorlab.modules.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Override
    public List<LearningKindResponse> getLearningKindResponses() {
        return subjectRepository.findLearningKinds().stream()
                .map(LearningKindResponse::new).collect(toList());
    }

    @Override
    public List<SubjectResponse> getSubjectResponses() {
        return subjectRepository.findAll().stream()
                .map(SubjectResponse::new).collect(toList());
    }

    @Override
    public List<SubjectResponse> getSubjectResponses(Long learningKindId) {
        return subjectRepository.findAllByLearningKindId(learningKindId).stream()
                .map(SubjectResponse::new).collect(toList());
    }
}
