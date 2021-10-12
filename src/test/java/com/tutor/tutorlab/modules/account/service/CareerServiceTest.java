package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class CareerServiceTest extends AbstractTest {

    @WithAccount(NAME)
    @Test
    void Career_등록() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        careerService.createCareer(user, careerCreateRequest);

        // Then
        Tutor tutor = tutorRepository.findByUser(user);
        assertEquals(1, careerRepository.findByTutor(tutor).size());

    }

    @WithAccount(NAME)
    @Test
    void Career_수정() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        Career career = careerService.createCareer(user, careerCreateRequest);
        Long careerId = career.getId();

        // When
        careerService.updateCareer(user, careerId, careerUpdateRequest);

        // Then
        Career updatedCareer = careerRepository.findById(careerId).orElse(null);
        assertAll(
                () -> assertNotNull(updatedCareer),
                () -> assertEquals(careerUpdateRequest.getCompanyName(), updatedCareer.getCompanyName())
        );
    }

    @WithAccount(NAME)
    @Test
    void Career_삭제() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        tutorService.createTutor(user, tutorSignUpRequest);

        Career career = careerService.createCareer(user, careerCreateRequest);
        Long careerId = career.getId();

        // When
        careerService.deleteCareer(user, careerId);

        // Then
        Career deletedCareer = careerRepository.findById(careerId).orElse(null);
        Assertions.assertNull(deletedCareer);

        Tutor tutor = tutorRepository.findByUser(user);
        assertEquals(0, tutor.getCareers().size());
    }
}