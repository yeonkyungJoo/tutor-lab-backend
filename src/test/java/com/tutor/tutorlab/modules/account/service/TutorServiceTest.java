package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class TutorServiceTest {

    @Autowired
    TutorService tutorService;
    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    UserRepository userRepository;

//    @Test
//    void getTutors() {
//    }
//
//    @Test
//    void getTutor() {
//    }

    // @AfterEach
    void afterEach() {

        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);

        tutorService.deleteTutor(user);
    }

    @WithAccount("yk")
    @Test
    void Tutor_등록() {

        // Given
        // When
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertNotNull(tutor);
        Assertions.assertEquals(RoleType.TUTOR, user.getRole());
    }

    @Test
    void Tutor_수정() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        TutorUpdateRequest tutorUpdateRequest = TutorUpdateRequest.builder()
                .subjects("python")
                .specialist(true)
                .build();
        tutorService.updateTutor(user, tutorUpdateRequest);

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertNotNull(tutor);
        Assertions.assertEquals(RoleType.TUTOR, user.getRole());
        Assertions.assertEquals("python", tutor.getSubjects());
        Assertions.assertTrue(tutor.isSpecialist());
    }

    @Test
    void Tutor_탈퇴() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        tutorService.deleteTutor(user);

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertNull(tutor);
        Assertions.assertEquals(RoleType.TUTEE, user.getRole());
    }

/*
    @Test
    void getCareers() {
    }

    @Test
    void getEducations() {
    }

    @Test
    void getLectures() {
    }

    @Test
    void getEnrollmentsOfLecture() {
    }

    @Test
    void getTuteesOfLecture() {
    }

    @Test
    void getReviewsOfLecture() {
    }
*/
}