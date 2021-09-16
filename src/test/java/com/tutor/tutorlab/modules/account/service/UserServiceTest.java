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

// @Transactional
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

//    @BeforeEach
//    void init() {
//
//        CareerCreateRequest careerCreateRequest1 = CareerCreateRequest.builder()
//                .companyName("tutorlab")
//                .duty("engineer")
//                .startDate("2007-12-03")
//                .endDate("2007-12-04")
//                .present(false)
//                .build();
//
//        CareerCreateRequest careerCreateRequest2 = CareerCreateRequest.builder()
//                .companyName("tutorlab2")
//                .duty("engineer")
//                .startDate("2007-12-05")
//                .endDate("2007-12-06")
//                .present(false)
//                .build();
//        List<CareerCreateRequest> careers = new ArrayList<>();
//        careers.add(careerCreateRequest1);
//        careers.add(careerCreateRequest2);
//
//        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
//                .schoolName("school")
//                .major("computer")
//                .entranceDate("2021-01-01")
//                .graduationDate("2021-02-01")
//                .score(4.01)
//                .degree("Bachelor")
//                .build();
//        List<EducationCreateRequest> educations = new ArrayList<>();
//        educations.add(educationCreateRequest);
//
//        tutorSignUpRequest = TutorSignUpRequest.builder()
//                .subjects("java,spring")
//                .careers(careers)
//                .educations(educations)
//                .specialist(false)
//                .build();
//    }

    @DisplayName("트랜잭션 쓰기지연 테스트")
    @Transactional
    @Test
    void transactionTest1() {
        educationRepository.deleteById(1L);
        /*
        Hibernate: select education0_.education_id as educatio1_4_0_, education0_.created_at as created_2_4_0_, education0_.updated_at as updated_3_4_0_, education0_.degree as degree4_4_0_, education0_.entrance_date as entrance5_4_0_, education0_.graduation_date as graduati6_4_0_, education0_.major as major7_4_0_, education0_.school_name as school_n8_4_0_, education0_.score as score9_4_0_, education0_.tutor_id as tutor_i10_4_0_ from education education0_ where education0_.education_id=?
         */
    }

    @DisplayName("트랜잭션 쓰기지연 테스트")
    @Transactional
    @Test
    void transactionTest2() {

        User user = User.builder()
                .username("yk@email.com")
                .password("password")
                .name("yk")
                .gender(null)
                .phoneNumber(null)
                .email("yk@email.com")
                .nickname(null)
                .bio(null)
                .zone(null)
                .role(RoleType.ROLE_TUTEE)
                .provider(null)
                .providerId(null)
                .build();

        userRepository.save(user);
        userRepository.delete(user);

        /*
        Hibernate: insert into user (created_at, bio, deleted, deleted_at, email, gender, name, nickname, password, phone_number, provider, provider_id, role, username, zone) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
         */
    }

    @DisplayName("트랜잭션 쓰기지연 테스트")
    @Transactional
    @Test
    void transactionTest3() {

        User user = User.builder()
                .username("yk@email.com")
                .password("password")
                .name("yk")
                .gender(null)
                .phoneNumber(null)
                .email("yk@email.com")
                .nickname(null)
                .bio(null)
                .zone(null)
                .role(RoleType.ROLE_TUTEE)
                .provider(null)
                .providerId(null)
                .build();

        userRepository.save(user);

        user.setNickname("nickname");

        System.out.println(">>> 1 : " + userRepository.findById(6L).get());
        userRepository.flush();
        System.out.println(">>> 2 : " + userRepository.findById(6L).get());

        /*
        Hibernate: insert into user (created_at, bio, deleted, deleted_at, email, gender, name, nickname, password, phone_number, provider, provider_id, role, username, zone) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        >>> 1 : User(super=com.tutor.tutorlab.modules.account.vo.User@6a29f6aa, username=yk@email.com, password=password, name=yk, gender=FEMALE, phoneNumber=null,
                    email=yk@email.com, nickname=nickname, bio=null, zone=null, role=ROLE_TUTEE, provider=null, providerId=null, deleted=false, deletedAt=null)

        Hibernate: update user set updated_at=?, bio=?, deleted=?, deleted_at=?, email=?, gender=?, name=?, nickname=?, password=?, phone_number=?, provider=?, provider_id=?, role=?, zone=? where user_id=?
        >>> 2 : User(super=com.tutor.tutorlab.modules.account.vo.User@6a29f6aa, username=yk@email.com, password=password, name=yk, gender=FEMALE, phoneNumber=null,
                    email=yk@email.com, nickname=nickname, bio=null, zone=null, role=ROLE_TUTEE, provider=null, providerId=null, deleted=false, deletedAt=null)
         */
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