package com.tutor.tutorlab.modules.subject.service;

import com.tutor.tutorlab.modules.subject.controller.response.ParentResponse;
import com.tutor.tutorlab.modules.subject.vo.Subject;
import com.tutor.tutorlab.modules.subject.mapstruct.SubjectMapstruct;
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
    private final SubjectMapstruct subjectMapstruct;

    @Override
    public ParentResponse getParents() {
        return ParentResponse.of(subjectRepository.findParentAll());
    }

    @Override
    public List<SubjectResponse> getSubjects(String parent) {
        List<Subject> subjects = subjectRepository.findSubjectAllByParent(parent);
        return subjects.stream()
                       .map(subject -> subjectMapstruct.subjectToSubjectResponse(subject))
                       .collect(toList());
    }
}
