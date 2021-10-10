package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@MockMvcTest
class LectureControllerTest extends AbstractTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LectureService lectureService;
    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TutorService tutorService;
    @Autowired
    TutorRepository tutorRepository;

    @WithAccount(NAME)
    @Test
    void newLecture() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        lectureService.createLecture(user, lectureCreateRequest);

        // Then
    }

    @Test
    void newLecture_invalidInput() {
    }

    @Test
    void editLecture() {
    }

    @Test
    void deleteLecture() {
    }

}