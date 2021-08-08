package com.tutor.tutorlab.modules.subject.controller;

import com.tutor.tutorlab.modules.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/subjects")
@RestController
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping(value = "/parents", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getParents() throws Exception {
        return subjectService.getParents();
    }

    @GetMapping(value = "/parents/{parent}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getSubjects(@PathVariable("parent") String parent) throws Exception {
        return subjectService.getSubjects(parent);
    }

}
