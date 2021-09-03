package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
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
class UserServiceTest {

    private TutorSignUpRequest tutorSignUpRequest;

    @Autowired
    EntityManager em;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
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

        CareerCreateRequest careerCreateRequest1 = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();

        CareerCreateRequest careerCreateRequest2 = CareerCreateRequest.builder()
                .companyName("tutorlab2")
                .duty("engineer")
                .startDate("2007-12-05")
                .endDate("2007-12-06")
                .present(false)
                .build();
        List<CareerCreateRequest> careers = new ArrayList<>();
        careers.add(careerCreateRequest1);
        careers.add(careerCreateRequest2);

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

    @DisplayName("EntityListener preUpdate 테스트")
    @Test
    @WithAccount("yk")
    void updateUser() {

        User user = userRepository.findByName("yk");
        System.out.println(user.getUpdatedAt());    // null

        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .phoneNumber("010-1234-5678")
                .email("yk@email.com")
                .nickname("nickname")
                //.bio(null)
                .zone("서울시 서초구")
                .build();
        userService.updateUser(user, userUpdateRequest);
        em.flush();

        userRepository.findAll().stream()
                .forEach(u -> System.out.println(u.getUpdatedAt())); // 2021-08-26T23:26:14.841354400
    }

    @DisplayName("회원 탈퇴")
    @Test
    @WithAccount("yk")
    void deleteUser() {

        // Given
        User user = userRepository.findByName("yk");
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        em.flush();

        Assertions.assertEquals(RoleType.ROLE_TUTOR, user.getRole());
        Assertions.assertEquals(1, userRepository.count());
        Assertions.assertEquals(1, tuteeRepository.count());
        Assertions.assertEquals(1, tutorRepository.count());
        Assertions.assertEquals(2, careerRepository.count());
        Assertions.assertEquals(1, educationRepository.count());

        // When
        userService.deleteUser(user);
        em.flush();

        // Then
        Assertions.assertEquals(1, userRepository.count());
        User findUser = userRepository.findAll().get(0);
        Assertions.assertEquals(RoleType.ROLE_TUTEE, findUser.getRole());
        Assertions.assertTrue(findUser.isDeleted());
        Assertions.assertNotNull(findUser.getDeletedAt());

        Assertions.assertEquals(0, tuteeRepository.count());
        Assertions.assertEquals(0, tutorRepository.count());
        Assertions.assertEquals(0, careerRepository.count());
        Assertions.assertEquals(0, educationRepository.count());

    }
}