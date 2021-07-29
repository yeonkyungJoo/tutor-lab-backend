package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.request.UserUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.*;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;
    private final TuteeRepository tuteeRepository;

    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;

    public void updateUser(User user, UserUpdateRequest userUpdateRequest) {

        // TODO - check : User가 영속성 컨텍스트에 존재 X?
        System.out.println(user);
        user = userRepository.findByUsername(user.getUsername());
        if (user == null) {

        }

        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        user.setEmail(userUpdateRequest.getEmail());
        user.setNickname(userUpdateRequest.getNickname());
        user.setBio(userUpdateRequest.getBio());
        user.setZone(userUpdateRequest.getZone());
    }

    public void deleteUser(User user) {

        // TODO - check : user_id가 존재하는지
        System.out.println(user);

        // TODO - check : GrantedAuthority
        RoleType role = user.getRole();
        // TODO - check : CASCADE
        if (role == RoleType.ROLE_TUTOR) {

            Tutor tutor = tutorRepository.findByUser(user);
            // Career 삭제
            tutor.getCareers().stream()
                    .forEach(career -> {
                        careerRepository.delete(career);
                    });
            // Education 삭제
            tutor.getEducations().stream()
                    .forEach(education -> {
                        educationRepository.delete(education);
                    });
            tutor.quit();
            // Tutor 삭제
            tutorRepository.delete(tutor);

        } else if (role == RoleType.ROLE_TUTEE) {

            Tutee tutee = tuteeRepository.findByUser(user);
            // Tutee 삭제
            tutee.quit();
            tuteeRepository.delete(tutee);
        }

        // User 삭제
        // TODO - check : 영속성 컨텍스트
        userRepository.delete(user);

    }
}
