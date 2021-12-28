package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@Transactional
@SpringBootTest
class TuteeServiceIntegrationTest extends AbstractTest {

    @WithAccount(NAME)
    @Test
    void Tutee_수정() {

        // Given
        // When
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tuteeService.updateTutee(user, tuteeUpdateRequest);

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNotNull(tutee);
        Assertions.assertEquals(RoleType.TUTEE, user.getRole());
        Assertions.assertEquals(tuteeUpdateRequest.getSubjects(), tutee.getSubjects());
    }

    @WithAccount(NAME)
    @Test
    void Tutee_탈퇴() {

        // Given
        // When
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tuteeService.deleteTutee(user);

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        assertNull(user);

        user = userRepository.findAllByUsername(USERNAME);
        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
        Tutee tutee = tuteeRepository.findByUser(user);
        assertNull(tutee);
    }
}