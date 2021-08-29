package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class EducationServiceTest {

    @Autowired
    EntityManager em;

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

    @Transactional
    @DisplayName("Education 등록")
    @WithAccount("yk")
    @Test
    void createEducation() {

        // Given
        User user = userRepository.findByName("yk");
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

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
        em.flush();

        // Then
        Assertions.assertEquals(1, educationRepository.findByTutor(tutor).size());
    }

    @Transactional
    @DisplayName("Education 삭제")
    @WithAccount("yk")
    @Test
    void deleteEducation() {

        // Given
        User user = userRepository.findByName("yk");
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        Education education = educationService.createEducation(user, educationCreateRequest);
        em.flush();

        // When
        educationService.deleteEducation(education.getId());
        em.flush();

        // Then
        System.out.println(tutor.getEducations());
        System.out.println(educationRepository.findAll());
    }
}