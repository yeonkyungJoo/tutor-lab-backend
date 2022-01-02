package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@Transactional
@SpringBootTest
class UserServiceIntegrationTest extends AbstractTest {

//    @Test
//    void getUsers() {
//    }
//
//    @Test
//    void getUser() {
//    }

    @WithAccount(NAME)
    @Test
    void User_수정() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);

        // When
        userService.updateUser(user, userUpdateRequest);

        // Then
        User updatedUser = userRepository.findByUsername(USERNAME).orElse(null);
        assertAll(
                () -> assertEquals(RoleType.TUTEE, updatedUser.getRole()),
                () -> assertEquals(userUpdateRequest.getPhoneNumber(), updatedUser.getPhoneNumber()),
                () -> assertEquals(userUpdateRequest.getEmail(), updatedUser.getEmail()),
                () -> assertEquals(userUpdateRequest.getBio(), updatedUser.getBio()),
                () -> assertEquals(userUpdateRequest.getGender(), updatedUser.getGender().toString())
        );
    }

    // TODO - 사용자 탈퇴 시 연관 엔티티 삭제 테스트
    // 1. User가 튜티인 경우
    // 2. User가 튜터인 경우
    @WithAccount(NAME)
    @Test
    void User_탈퇴() {

//        // Given
//        User user = userRepository.findByUsername(USERNAME).orElse(null);
//        assertEquals(RoleType.TUTEE, user.getRole());
//
//        // When
//        userService.deleteUser(user);
//
//        // Then
//        user = userRepository.findByUsername(USERNAME).orElse(null);
//        assertNull(user);
//
//        User deletedUser = userRepository.findAllByUsername(USERNAME);
//        assertTrue(deletedUser.isDeleted());
//        assertNotNull(deletedUser.getDeletedAt());
    }
}