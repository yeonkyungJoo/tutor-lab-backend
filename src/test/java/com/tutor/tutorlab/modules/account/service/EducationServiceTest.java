package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class EducationServiceTest {

    @Autowired
    EducationRepository educationRepository;
    @Autowired
    EducationService educationService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    TutorService tutorService;

    @WithAccount("yk")
    @Test
    void Education_등록() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        educationService.createEducation(user, educationCreateRequest);

        // Then
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(1, educationRepository.findByTutor(tutor).size());
    }

    @WithAccount("yk")
    @Test
    void Education_수정() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        Education education = educationService.createEducation(user, educationCreateRequest);
        Long educationId = education.getId();

        // When
        EducationUpdateRequest educationUpdateRequest = EducationUpdateRequest.builder()
                .schoolName("school2")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        educationService.updateEducation(user, educationId, educationUpdateRequest);

        // Then
        Education updatedEducation = educationRepository.findById(educationId).orElse(null);
        Assertions.assertNotNull(updatedEducation);
        Assertions.assertEquals("school2", updatedEducation.getSchoolName());
    }

    @WithAccount("yk")
    @Test
    void Education_삭제() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        Education education = educationService.createEducation(user, educationCreateRequest);
        Long educationId = education.getId();

        // When
        educationService.deleteEducation(user, educationId);

        // Then
        Education deletedEducation = educationRepository.findById(educationId).orElse(null);
        Assertions.assertNull(deletedEducation);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(0, tutor.getEducations().size());
    }
}