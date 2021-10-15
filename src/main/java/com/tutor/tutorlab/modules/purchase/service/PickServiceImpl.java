package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.controller.response.PickResponse;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.PICK;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTEE;

@Service
@Transactional
@RequiredArgsConstructor
public class PickServiceImpl extends AbstractService implements PickService {

    private final PickRepository pickRepository;
    private final TuteeRepository tuteeRepository;
    private final LectureRepository lectureRepository;

    private Page<Pick> getPicks(User user, Integer page) {

        // TODO - AuthAspect or Interceptor로 처리
        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));
        return pickRepository.findByTutee(tutee, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PickResponse> getPickResponses(User user, Integer page) {
        return getPicks(user, page).map(PickResponse::new);
    }

    @Override
    public Pick createPick(User user, Long lectureId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Pick pick = Pick.of(tutee, lecture);
        // TODO - CHECK
        tutee.addPick(pick);
        return pickRepository.save(pick);
    }

    @Override
    public void deletePick(User user, Long pickId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Pick pick = pickRepository.findByTuteeAndId(tutee, pickId)
                .orElseThrow(() -> new EntityNotFoundException(PICK));

        pick.delete();
        pickRepository.delete(pick);
    }

    @Override
    public void deleteAllPicks(User user) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));
        // TODO - batch
        pickRepository.deleteByTutee(tutee);
    }
}
