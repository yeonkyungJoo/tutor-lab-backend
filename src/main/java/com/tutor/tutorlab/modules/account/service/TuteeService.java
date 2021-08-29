package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TuteeService {

    private final UserRepository userRepository;
    private final TuteeRepository tuteeRepository;

    public void updateTutee(User user, TuteeUpdateRequest tuteeUpdateRequest) {

        Tutee tutee = tuteeRepository.findByUser(user);
        if (tutee == null) {
            throw new UnauthorizedException();
        }

        tutee.setSubjects(tuteeUpdateRequest.getSubjects());
    }

    // TODO - check : CASCADE
    public void deleteTutee(User user) {

        if (user.getRole() != RoleType.ROLE_TUTEE) {
            throw new UnauthorizedException();
        }

        Tutee tutee = tuteeRepository.findByUser(user);
        if (tutee == null) {
            throw new UnauthorizedException();
        }

        // 튜티 탈퇴 = 회원 탈퇴
        tutee.quit();
        tuteeRepository.delete(tutee);
        userRepository.delete(user);

        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
