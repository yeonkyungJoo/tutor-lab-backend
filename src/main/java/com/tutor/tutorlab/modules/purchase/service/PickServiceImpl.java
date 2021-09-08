package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class PickServiceImpl implements PickService {

    private final PickRepository pickRepository;
    private final LectureRepository lectureRepository;

    @Override
    public void add(Tutee tutee, Long lectureId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));

        Pick pick = new Pick();
        pick.setLecture(lecture);
        // TODO - CHECK
        tutee.addPick(pick);

        pickRepository.save(pick);
    }

    @Override
    public void subtract(Tutee tutee, Long pickId) {

        Pick pick = pickRepository.findById(pickId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 내역입니다."));
        pickRepository.delete(pick);
    }

    @Override
    public void clear(Tutee tutee) {
        pickRepository.deleteAllByTutee(tutee);
    }
}
