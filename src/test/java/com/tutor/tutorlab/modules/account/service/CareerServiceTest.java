package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class CareerServiceTest {

    @Autowired
    CareerRepository careerRepository;
    @Autowired
    CareerService careerService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    TutorService tutorService;

    @BeforeEach
    void beforeEach() {

        User user = userRepository.findByName("yk");
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

    }

    @AfterEach
    void afterEach() {

        User user = userRepository.findByName("yk");
        Tutor tutor = tutorRepository.findByUser(user);

        tutorService.deleteTutor(user);
//        List<Career> careers = careerRepository.findByTutor(tutor);
//        careerRepository.deleteAll(careers);
//        tutorRepository.delete(tutor);
    }

    @Test
    void getCareer() {
    }

    @Transactional
    @WithAccount("yk")
    @Test
    void Career_등록() {

        // Given
        // When
        User user = userRepository.findByName("yk");
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        careerService.createCareer(user, careerCreateRequest);

        // Then
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(1, careerRepository.findByTutor(tutor).size());

    }

    @Transactional
    @WithAccount("yk")
    @Test
    void Career_수정() {

        // Given
        User user = userRepository.findByName("yk");
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        Career career = careerService.createCareer(user, careerCreateRequest);
        Long careerId = career.getId();

        // When
        CareerUpdateRequest careerUpdateRequest = CareerUpdateRequest.builder()
                .companyName("tutorlab2")
                .duty("engineer")
                .startDate("2007-12-03")
                .present(true)
                .build();
        careerService.updateCareer(user, careerId, careerUpdateRequest);

        // Then
        Career updatedCareer = careerRepository.findById(careerId).orElse(null);
        Assertions.assertNotNull(updatedCareer);
        Assertions.assertEquals("tutorlab2", updatedCareer.getCompanyName());
    }

    @Transactional
    @WithAccount("yk")
    @Test
    void Career_삭제() {

        // Given
        User user = userRepository.findByName("yk");
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        Career career = careerService.createCareer(user, careerCreateRequest);
        Long careerId = career.getId();

        // When
        careerService.deleteCareer(user, careerId);

        // Then
        Career deletedCareer = careerRepository.findById(careerId).orElse(null);
        Assertions.assertNull(deletedCareer);
        Tutor tutor = tutorRepository.findByUser(user);
        Assertions.assertEquals(0, tutor.getCareers().size());
    }
}