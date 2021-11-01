package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorQueryRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class TutorTuteeService extends AbstractService {

    private final TutorRepository tutorRepository;
    private final TutorQueryRepository tutorQueryRepository;

    @Transactional(readOnly = true)
    public Page<TuteeSimpleResponse> getTuteeSimpleResponses(User user, Boolean closed, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        return tutorQueryRepository.findTuteesOfTutor(tutor, closed, getPageRequest(page));
    }

}
