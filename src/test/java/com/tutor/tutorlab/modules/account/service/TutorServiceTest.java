package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.*;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TutorServiceTest {

    private TutorSignUpRequest tutorSignUpRequest;

    @Autowired
    EntityManager em;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TuteeRepository tuteeRepository;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    TutorService tutorService;

    @Autowired
    CareerRepository careerRepository;
    @Autowired
    EducationRepository educationRepository;

    @BeforeEach
    void init() {

        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        List<CareerCreateRequest> careers = new ArrayList<>();
        careers.add(careerCreateRequest);

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        List<EducationCreateRequest> educations = new ArrayList<>();
        educations.add(educationCreateRequest);

        tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .careers(careers)
                .educations(educations)
                .specialist(false)
                .build();

    }

    @DisplayName("Tutor 등록")
    @WithAccount("yk")
    @Test
    void createTutor() {

        // Given
        User user = userRepository.findByName("yk");
        Assertions.assertEquals(RoleType.ROLE_TUTEE, user.getRole());
        Assertions.assertEquals(0, tutorRepository.count());

        // When
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        em.flush();

        // Then
        Assertions.assertEquals(RoleType.ROLE_TUTOR, user.getRole());
        Assertions.assertEquals(1, tutorRepository.count());

        Assertions.assertEquals(1, careerRepository.count());
        Assertions.assertNotNull(careerRepository.findByTutor(tutor));

        Assertions.assertEquals(1, educationRepository.count());
        Assertions.assertNotNull(educationRepository.findByTutor(tutor));
    }

    @DisplayName("Tutor 삭제")
    @WithAccount("yk")
    @Test
    void deleteTutor() {

        // Given
        User user = userRepository.findByName("yk");
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        em.flush();

        Assertions.assertEquals(RoleType.ROLE_TUTOR, user.getRole());
        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertEquals(1, tuteeRepository.count());
        Assertions.assertEquals(1, tutorRepository.count());
        Assertions.assertEquals(1, careerRepository.count());
        Assertions.assertEquals(1, educationRepository.count());

        // When
        tutorService.deleteTutor(user);
        em.flush();

        // Then
        Assertions.assertEquals(RoleType.ROLE_TUTEE, user.getRole());
        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertEquals(1, tuteeRepository.count());
        Assertions.assertEquals(0, tutorRepository.count());
        Assertions.assertEquals(0, careerRepository.count());
        Assertions.assertEquals(0, educationRepository.count());
    }
}