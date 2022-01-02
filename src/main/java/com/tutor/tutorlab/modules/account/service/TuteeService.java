package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTEE;


@Service
@Transactional
@RequiredArgsConstructor
public class TuteeService extends AbstractService {

    private final TuteeRepository tuteeRepository;

    private final PickRepository pickRepository;
    private final EnrollmentService enrollmentService;
    private final EnrollmentRepository enrollmentRepository;

    private Page<Tutee> getTutees(Integer page) {
        return tuteeRepository.findAll(PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    public Page<TuteeResponse> getTuteeResponses(Integer page) {
        return getTutees(page).map(TuteeResponse::new);
    }

    private Tutee getTutee(Long tuteeId) {
        return tuteeRepository.findById(tuteeId).orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.EntityType.TUTEE));
    }

    @Transactional(readOnly = true)
    public TuteeResponse getTuteeResponse(Long tuteeId) {
        return new TuteeResponse(getTutee(tuteeId));
    }

    public void updateTutee(User user, TuteeUpdateRequest tuteeUpdateRequest) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        tutee.update(tuteeUpdateRequest);
    }

    public void deleteTutee(User user) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        // pick 삭제
        pickRepository.deleteByTutee(tutee);
        // enrollment 삭제
        enrollmentRepository.findAllByTuteeId(tutee.getId()).forEach(enrollment -> {
            enrollmentService.deleteEnrollment(enrollment);
        });
        tuteeRepository.delete(tutee);
    }
}
