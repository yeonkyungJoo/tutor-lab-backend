package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TuteeService {

    private final UserRepository userRepository;
    private final TuteeRepository tuteeRepository;

    public void updateTutee(User user, TuteeUpdateRequest tuteeUpdateRequest) {

        Tutee tutee = tuteeRepository.findByUser(user);
        if (tutee == null) {

        }

        tutee.setSubjects(tuteeUpdateRequest.getSubjects());
    }

    public void deleteTutee(User user) {

        System.out.println(user);
        User findUser = userRepository.findByUsername(user.getUsername());
        RoleType role = findUser.getRole();

        if (role == RoleType.ROLE_TUTEE) {

            Tutee tutee = tuteeRepository.findByUser(user);
            if (tutee == null) {
                // TODO - 에러 발생
            }

            tutee.quit();

            // 튜티 탈퇴 = 회원 탈퇴
            tuteeRepository.delete(tutee);
            userRepository.delete(user);

        } else {
            // TODO - 에러 발생
        }
    }
}
