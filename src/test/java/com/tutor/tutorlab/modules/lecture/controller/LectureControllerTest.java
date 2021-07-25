package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LectureControllerTest extends AbstractTest {
    private final String BASE_URL = "/lectures";

    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("doqndnffo@gmail.com")
                .password("1")
                .name("우성환")
                .gender("M")
                .phoneNumber("1")
                .email("test")
                .bio("test")
                .zone("test")
                .role("test")
                .provider("dfs")
                .providerId("fsd")
                .build();

        Lecture lecture = Lecture.builder()
                .user(user)
                .title("test")
                .subTitle("test")
                .content("test")
                .totalTime(10)
                .pertimeCost(1000L)
                .totalCost(10000L)
                .difficulty(DifficultyType.ADVANCED)
                .isGroup(true)
                .groupNumber(2)
                .system(SystemType.ONLINE)
                .build();

        userRepository.save(user);
        lectureRepository.save(lecture);
    }

    @Test
    void 강의_한건조회() throws Exception{
        int id = 1;

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
