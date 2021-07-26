package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@MockMvcTest
class TutorControllerTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    // @Transactional
    public void init() throws Exception {

    }

    @Test
    @DisplayName("Tutor 등록")
    @WithAccount("yk")
    public void newTutor() throws Exception {

        // Given
        User user = userRepository.findByName("yk");

        // When
        // TutorSignUpRequest tutorSignUpRequest =
        // Then
    }

    @Test
    @DisplayName("Tutor 등록 - Invalid Input")
    public void newTutor_withInvalidInput() throws Exception {

        // Given

        // When

        // Then
    }

    @Test
    @DisplayName("Tutor 등록 - 인증된 사용자 X")
    public void newTutor_withoutAuthenticatedUser() throws Exception {

        // Given

        // When

        // Then
    }

    @Test
    @DisplayName("Tutor 수정")
    public void editTutor() throws Exception {

        // Given

        // When

        // Then
    }

    @Test
    @DisplayName("Tutor 삭제")
    public void removeTutor() throws Exception {

        // Given

        // When

        // Then
    }
}