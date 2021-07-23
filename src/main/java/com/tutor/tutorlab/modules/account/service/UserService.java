package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.request.UpdateUserRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;
    private final TuteeRepository tuteeRepository;

    public void updateUser(User user, UpdateUserRequest updateUserRequest) {

        // TODO - check : User가 영속성 컨텍스트에 존재 X?
        System.out.println(user);
        user = userRepository.findByUsername(user.getUsername());
        if (user == null) {

        }

        user.setPhoneNumber(updateUserRequest.getPhoneNumber());
        user.setEmail(updateUserRequest.getEmail());
        user.setNickname(updateUserRequest.getNickname());
        user.setBio(updateUserRequest.getBio());
        user.setZone(updateUserRequest.getZone());
    }

    public void deleteUser(User user) {

        // TODO - check : user_id가 존재하는지
        System.out.println(user);
        // TODO - check : GrantedAuthority
        // role 확인
        List<String> roles = user.getRoleList();
        for (String role : roles) {

            // TODO - check : CASCADE
            if (role.equals("ROLE_TUTOR")) {

                Tutor tutor = tutorRepository.findByUser(user);
                // Career 삭제
                // Education 삭제
                tutor.quit();
                // Tutor 삭제
                tutorRepository.delete(tutor);

            } else if (role.equals("ROLE_TUTEE")) {

                Tutee tutee = tuteeRepository.findByUser(user);
                // Tutee 삭제
                tutee.quit();
                tuteeRepository.delete(tutee);
            }
        }

        // User 삭제
        // TODO - check : 영속성 컨텍스트
        userRepository.delete(user);

    }
}
