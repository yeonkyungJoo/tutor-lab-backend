package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TuteeService {

    private final TuteeRepository tuteeRepository;

    public void updateTutee(User user, TuteeUpdateRequest tuteeUpdateRequest) {

        Tutee tutee = tuteeRepository.findByUser(user);
        if (tutee == null) {
            throw new UnauthorizedException();
        }

        tutee.setSubjects(tuteeUpdateRequest.getSubjects());
    }

    public void deleteTutee(User user) {

        if (user.getRole() != RoleType.ROLE_TUTEE) {
            throw new UnauthorizedException();
        }

        Tutee tutee = tuteeRepository.findByUser(user);
        if (tutee == null) {
            throw new UnauthorizedException();
        }

        // update user set updated_at=?, bio=?, deleted=?, deleted_at=?, email=?, gender=?, name=?, nickname=?, password=?, phone_number=?, provider=?, provider_id=?, role=?, zone=? where user_id=?
        user.quit();
        tuteeRepository.delete(tutee);

        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
