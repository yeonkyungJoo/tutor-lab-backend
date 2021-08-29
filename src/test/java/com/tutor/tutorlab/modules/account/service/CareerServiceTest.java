package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CareerServiceTest {

    @Autowired
    EntityManager em;

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
    void init() {

    }

    @Test
    @DisplayName("Career 등록")
    @WithAccount("yk")
    void createCareer() {

        // Given
        User user = userRepository.findByName("yk");
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

        // When
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        careerService.createCareer(user, careerCreateRequest);

        // Then
        Assertions.assertEquals(1, careerRepository.findByTutor(tutor).size());
    }

    @Transactional
    @Test
    @DisplayName("Career 삭제")
    @WithAccount("yk")
    void deleteCareer() {

        // Given
        User user = userRepository.findByName("yk");
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        Career career = careerService.createCareer(user, careerCreateRequest);
        // org.springframework.dao.InvalidDataAccessApiUsageException: The given id must not be null!;
        // nested exception is java.lang.IllegalArgumentException: The given id must not be null!
        em.flush();

        // When
        careerService.deleteCareer(career.getId());
        em.flush();

        // Then
        System.out.println(tutor.getCareers());             // []
        System.out.println(careerRepository.findAll());     // []
    }
}