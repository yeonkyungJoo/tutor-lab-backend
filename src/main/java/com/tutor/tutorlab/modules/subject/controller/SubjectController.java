package com.tutor.tutorlab.modules.subject.controller;

import com.tutor.tutorlab.modules.subject.controller.response.ParentResponse;
import com.tutor.tutorlab.modules.subject.controller.response.SubjectResponse;
import com.tutor.tutorlab.modules.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/subjects")
@RestController
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping(value = "/parents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getParents() {
        // TODO - CHECK
        ParentResponse parents = subjectService.getParents();
        return ResponseEntity.ok(parents);
    }

    @GetMapping(value = "/parents/{parent}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSubjects(@PathVariable("parent") String parent) {
        List<SubjectResponse> subjects = subjectService.getSubjects(parent);
        return ResponseEntity.ok(subjects);
    }

}
