package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TuteeServiceTest {

    @Autowired
    TuteeService tuteeService;
    @Autowired
    TuteeRepository tuteeRepository;

    @Autowired
    UserRepository userRepository;

//    @Transactional
//    @Test
//    void getTutees() {
//    }
//
//    @Transactional
//    @Test
//    void getTutee() {
//    }

    @WithAccount("yk")
    @Transactional
    @Test
    void Tutee_수정() {

        // Given
        // When
        User user = userRepository.findByName("yk");
        TuteeUpdateRequest tuteeUpdateRequest = TuteeUpdateRequest.builder()
                .subjects("python")
                .build();
        tuteeService.updateTutee(user, tuteeUpdateRequest);

        // Then
        user = userRepository.findByName("yk");
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);
        Assertions.assertEquals(RoleType.TUTEE, user.getRole());
        Assertions.assertEquals("python", tutee.getSubjects());
    }

    @WithAccount("yk")
    @Transactional
    @Test
    void Tutee_탈퇴() {

        // Given
        // When
        User user = userRepository.findByName("yk");
        tuteeService.deleteTutee(user);

        // Then
        user = userRepository.findByName("yk");
        assertNull(user);
        user = userRepository.findAllByName("yk");
        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNull(tutee);
    }
}