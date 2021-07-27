package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MockMvcTest
class TuteeControllerTest {

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    // @Transactional
    public void init() throws Exception {

    }

    @Test
    @DisplayName("Tutee 수정 / 추가 정보 입력")
    @WithAccount("yk")
    public void editTutee() throws Exception {

        // Given
        User user = userRepository.findByName("yk");

        // When

        // Then
    }

    // TODO
    @Test
    @DisplayName("Tutee 수정 - Invalid Input")
    public void editTutee_withInvalidInput() throws Exception {

        // Given

        // When

        // Then
    }

    // TODO
    @Test
    @DisplayName("Tutee 수정 - 인증된 사용자 X")
    public void editTutee_withoutAuthenticatedUser() throws Exception {

        // Given

        // When

        // Then
    }

    @Test
    @DisplayName("Tutee 삭제")
    @WithAccount("yk")
    public void removeTutee() throws Exception {

        // Given

        // When

        // Then
    }

    // TODO
    @Test
    @DisplayName("Tutee 삭제 - 튜티가 아닌 경우")
    public void removeTutee_notTutee() throws Exception {

        // Given

        // When

        // Then
    }
}