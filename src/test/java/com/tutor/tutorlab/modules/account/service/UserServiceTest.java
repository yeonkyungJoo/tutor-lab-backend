package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
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
        userRepository.findAll().stream()
                .forEach(u -> System.out.println(u.getUpdatedAt())); // 2021-08-26T23:26:14.841354400
    }
}