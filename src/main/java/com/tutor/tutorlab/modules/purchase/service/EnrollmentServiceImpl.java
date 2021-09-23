package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.chat.service.ChatService;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.ENROLLMENT;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTEE;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentServiceImpl extends AbstractService implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CancellationRepository cancellationRepository;
    private final TuteeRepository tuteeRepository;
    private final TutorRepository tutorRepository;
    private final LectureRepository lectureRepository;

    private final ChatService chatService;

    @Transactional(readOnly = true)
    @Override
    public Page<Lecture> getLecturesOfTutee(User user, Integer page) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        return enrollmentRepository.findByTutee(tutee, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(Enrollment::getLecture);
    }

    @Override
    public void enroll(User user, Long lectureId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        // TODO - CHECK : lecture & tutor - fetch join
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO - 구매 프로세스
        // TODO - 구매 중복 X 체크 (UNIQUE)

        // 성공 시
        Enrollment enrollment = Enrollment.builder()
                .lecture(lecture)
                .tutee(tutee)
                .build();
        // TODO - CHECK
        tutee.addEnrollment(enrollment);
        enrollmentRepository.save(enrollment);

        // 수강 시 채팅방 자동 생성
        chatService.createChatroom(lecture.getTutor(), tutee, enrollment);

    }

    @Override
    public void cancel(User user, Long lectureId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT));

        // TODO - 환불

        // TODO - Entity Listener 활용해 변경
        Cancellation cancellation = Cancellation.builder()
                .tutee(tutee)
                .lecture(lecture)
                .enrolledAt(enrollment.getCreatedAt())
                .build();
        cancellationRepository.save(cancellation);
        // 수강 취소 시 채팅방 자동 삭제 - cascade
        enrollmentRepository.delete(enrollment);
    }

    @Override
    public void close(User user, Long lectureId, Long enrollmentId) {

        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Enrollment enrollment = enrollmentRepository.findByLectureAndId(lecture, enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException(ENROLLMENT));

        enrollment.close();
        // 수강 종료 시 채팅방 삭제
        chatService.deleteChatroom(enrollment);
    }
}
