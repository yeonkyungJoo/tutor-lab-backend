package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final TutorService tutorService;
    private final TuteeService tuteeService;

    public void updateUser(User user, UserUpdateRequest userUpdateRequest) {

        user = userRepository.findByUsername(user.getUsername());
        if (user == null) {

        }

        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        user.setEmail(userUpdateRequest.getEmail());
        user.setNickname(userUpdateRequest.getNickname());
        user.setBio(userUpdateRequest.getBio());
        user.setZone(userUpdateRequest.getZone());

        user.setUpdatedAt(LocalDateTime.now());
    }

    // TODO - Admin인 경우
    public void deleteUser(User user) {

        // TODO - check : GrantedAuthority
        RoleType role = user.getRole();

        // TODO - check : CASCADE
        if (role == RoleType.ROLE_TUTOR) {
            tutorService.deleteTutor(user);
        }
        tuteeService.deleteTutee(user);

    }
}
