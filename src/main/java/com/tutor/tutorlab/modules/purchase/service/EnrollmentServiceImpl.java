package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.request.EnrollmentRequest;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CancellationRepository cancellationRepository;

    private final TuteeRepository tuteeRepository;
    private final LectureRepository lectureRepository;

    @Override
    public void enroll(User user, EnrollmentRequest enrollmentRequest) {

        // TODO - 구매 프로세스

        Lecture lecture = lectureRepository.findById(enrollmentRequest.getLectureId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));

        // TODO - 구매 중복 X 체크 (UNIQUE)
        // 성공 시
        // 튜티 확인
        Tutee tutee = tuteeRepository.findByUser(user);
        Enrollment enrollment = Enrollment.builder()
                .lecture(lecture)
                .tutee(tutee)
                .build();
        tutee.addEnrollment(enrollment);
    }

    @Override
    public void cancel(User user, Long lectureId) {

        // TODO - 환불 프로세스

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));
        Tutee tutee = tuteeRepository.findByUser(user);

        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);

        // TODO - Entity Listener 활용해 변경
        Cancellation cancellation = Cancellation.builder()
                .tutee(tutee)
                .lecture(lecture)
                .enrolledAt(enrollment.getCreatedAt())
                .build();
        cancellationRepository.save(cancellation);

        enrollmentRepository.delete(enrollment);
    }
}
