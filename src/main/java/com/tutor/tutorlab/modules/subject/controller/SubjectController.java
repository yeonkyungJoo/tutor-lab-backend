package com.tutor.tutorlab.modules.subject.controller;

import com.tutor.tutorlab.modules.subject.controller.response.LearningKindResponse;
import com.tutor.tutorlab.modules.subject.controller.response.SubjectResponse;
import com.tutor.tutorlab.modules.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping(value = "/learningKinds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLearningKinds() {
        List<LearningKindResponse> learningKinds = subjectService.getLearningKindResponses();
        return ResponseEntity.ok(learningKinds);
    }

    @GetMapping(value = "/subjects", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSubjects() {
        List<SubjectResponse> subjects = subjectService.getSubjectResponses();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping(value = "/subjects/{learning_kind_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSubjects(@PathVariable(name = "learning_kind_id") Long learningKindId) {
        List<SubjectResponse> subjects = subjectService.getSubjectResponses(learningKindId);
        return ResponseEntity.ok(subjects);
    }

}
