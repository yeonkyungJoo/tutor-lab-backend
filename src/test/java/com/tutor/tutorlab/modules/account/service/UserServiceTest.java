package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Assertions;
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

//    @Test
//    void getUsers() {
//    }
//
//    @Test
//    void getUser() {
//    }

    @WithAccount("yk")
    @Test
    void User_수정() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        user.setNickname("nickname");

        // When
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals("nickname", user.getNickname());
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .phoneNumber("010-1111-2222")
                .email("email@email.com")
                .bio("hello")
                .build();
        userService.updateUser(user, userUpdateRequest);

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertNull(user.getNickname());
        assertEquals("010-1111-2222", user.getPhoneNumber());
        assertEquals("email@email.com", user.getEmail());
        assertEquals("hello", user.getBio());
        assertEquals(RoleType.TUTEE, user.getRole());
        assertEquals(GenderType.MALE, user.getGender());
    }

    // TODO - 사용자 탈퇴 시 연관 엔티티 삭제 테스트
    // 1. User가 튜티인 경우
    // 2. User가 튜터인 경우
    @WithAccount("yk")
    @Test
    void User_탈퇴() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());
        // When
        userService.deleteUser(user);

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertNull(user);
        user = userRepository.findAllByName("yk");
        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
    }
}