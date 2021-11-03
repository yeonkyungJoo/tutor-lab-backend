package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.purchase.controller.response.CancellationResponse;
import com.tutor.tutorlab.modules.purchase.repository.CancellationQueryRepository;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.CANCELLATION;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class TutorCancellationService extends AbstractService {

    private final CancellationRepository cancellationRepository;
    private final CancellationQueryRepository cancellationQueryRepository;
    private final TutorRepository tutorRepository;

    public Page<CancellationResponse> getCancellationResponses(User user, Integer page) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        return cancellationQueryRepository.findCancellationsOfTutor(tutor, getPageRequest(page));
    }

    @Transactional
    public void approve(User user, Long cancellationId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Cancellation cancellation = cancellationRepository.findById(cancellationId)
                .orElseThrow(() -> new EntityNotFoundException(CANCELLATION));
        cancellation.setApproved(true);
    }

}
