package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled
@Transactional
@SpringBootTest
class TutorServiceIntegrationTest extends AbstractTest {

    @WithAccount(NAME)
    @Test
    void Tutor_등록() {

        // Given
        // When
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        assertNotNull(tutor);
        assertEquals(RoleType.TUTOR, user.getRole());
    }

    @WithAccount(NAME)
    @Test
    void Tutor_수정() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        tutorService.updateTutor(user, tutorUpdateRequest);

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTOR, user.getRole());

        Tutor tutor = tutorRepository.findByUser(user);
        assertNotNull(tutor);
    }

    @WithAccount(NAME)
    @Test
    void Tutor_탈퇴() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        tutorService.deleteTutor(user);

        // Then
        user = userRepository.findByUsername(USERNAME).orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());

        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertNull(tutor);
    }

}