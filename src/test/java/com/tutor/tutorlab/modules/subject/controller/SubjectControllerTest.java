package com.tutor.tutorlab.modules.subject.controller;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.subject.vo.Subject;
import com.tutor.tutorlab.modules.subject.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SubjectControllerTest extends AbstractTest {

    private final String BASE_URL = "/subjects";

    @Autowired
    private SubjectRepository subjectRepository;

    @BeforeEach
    void subjectSetUp() {
        List<String> parents = Arrays.asList("개발", "프로그래밍언어", "프레임워크", "etc");
        List<MockSubject> subjects = Arrays.asList(
                MockSubject.of("웹개발"),
                MockSubject.of("백엔드"),
                MockSubject.of("프론트엔드"),
                MockSubject.of("게임개발"),
                MockSubject.of("모바일개발"),
                MockSubject.of("정보/보안"));

        parents.forEach(parent -> {
            subjects.forEach(subject -> {
                Subject entity = Subject.builder()
                        .parent(parent)
                        .subject(subject.getSubject())
                        .learningKind("coding")
                        .build();
                subjectRepository.save(entity);
            });
        });
    }

    @Test
    void parent_목록조회() throws Exception {
        mockMvc.perform(get(BASE_URL + "/parents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result.parents").exists())
                .andExpect(jsonPath("$.result.parents").isArray())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.responseTime").isString());
    }

    @Test
    void subject_목록조회() throws Exception {
        String parent = "개발";
        mockMvc.perform(get(BASE_URL + "/parents/{parent}", parent)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].parent").isString())
                .andExpect(jsonPath("$.result[0].subject").isString())
                .andExpect(jsonPath("$.result[0].learningKind").isString())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.responseTime").isString())
        ;
    }

    @RequiredArgsConstructor(staticName = "of")
    @Value
    static class MockSubject {
        private final String subject;
    }

}