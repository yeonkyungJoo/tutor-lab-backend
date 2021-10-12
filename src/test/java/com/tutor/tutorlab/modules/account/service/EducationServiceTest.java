package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class EducationServiceTest extends AbstractTest {

    @WithAccount(NAME)
    @Test
    void Education_등록() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        educationService.createEducation(user, educationCreateRequest);

        // Then
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(1, educationRepository.findByTutor(tutor).size());
    }

    @WithAccount(NAME)
    @Test
    void Education_수정() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        Education education = educationService.createEducation(user, educationCreateRequest);
        Long educationId = education.getId();

        // When
        educationService.updateEducation(user, educationId, educationUpdateRequest);

        // Then
        Education updatedEducation = educationRepository.findById(educationId).orElse(null);
        Assertions.assertNotNull(updatedEducation);
        Assertions.assertEquals(educationUpdateRequest.getMajor(), updatedEducation.getMajor());
    }

    @WithAccount(NAME)
    @Test
    void Education_삭제() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

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