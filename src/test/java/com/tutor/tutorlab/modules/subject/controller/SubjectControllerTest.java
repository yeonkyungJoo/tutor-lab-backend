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

}