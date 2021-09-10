package com.tutor.tutorlab.test;

import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MainTestServiceTest {

    @Autowired
    MainTestService mainTestService;
    @Autowired
    IsolationTestService isolationTestService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorRepository tutorRepository;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
        tutorRepository.deleteAll();
    }

    @Test
    void propagationTest() {
        try {
            mainTestService.saveTutor();
        } catch (RuntimeException e) {
            System.out.println(">>> " + e.getMessage());
        }

        System.out.println("user : " + userRepository.findAll());
        System.out.println("tutor : " + tutorRepository.findAll());

    }

    @Test
    void transactionTest() {


    }

    @Test
    void isolationTest() {

        String name = "yk";
        User user = User.builder()
                .username(name + "@email.com")
                .password("password")
                .name(name)
                .gender("MALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .role(RoleType.ROLE_TUTEE)
                .provider(null)
                .providerId(null)
                .build();
        Long userId = userRepository.save(user).getId();

        isolationTestService.get(userId);
        System.out.println(userRepository.findAll());

    }

}