package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
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
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.notification.enums.NotificationType;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
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

import static com.tutor.tutorlab.config.exception.AlreadyExistException.ENROLLMENT;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE_PRICE;
import static com.tutor.tutorlab.modules.account.enums.RoleType.TUTEE;

@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentServiceImpl extends AbstractService implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CancellationRepository cancellationRepository;
    private final TuteeRepository tuteeRepository;

    private final LectureRepository lectureRepository;
    private final LecturePriceRepository lecturePriceRepository;
    private final ChatService chatService;

    private final NotificationService notificationService;

    private Page<Lecture> getLecturesOfTutee(User user, Integer page) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        return enrollmentRepository.findByTutee(tutee, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(Enrollment::getLecture);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LectureResponse> getLectureResponsesOfTutee(User user, Integer page) {
        return getLecturesOfTutee(user, page).map(LectureResponse::new);
    }

    @Override
    public Enrollment createEnrollment(User user, Long lectureId, Long lecturePriceId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        // TODO - CHECK : lecture & tutor - fetch join
        // TODO - CHECK : lecture의 enrollment가 null vs tutee의 enrollment는 size = 0
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        LecturePrice lecturePrice = lecturePriceRepository.findByLectureAndId(lecture, lecturePriceId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE_PRICE));

        // 종료/취소된 강의 재구매 불가
        if (enrollmentRepository.findAllByTuteeIdAndLectureId(tutee.getId(), lecture.getId()).isPresent()) {
            throw new AlreadyExistException(ENROLLMENT);
        }

        // TODO - 구매 프로세스
        // TODO - 구매 중복 X 체크 (UNIQUE)

        // 성공 시
        Enrollment enrollment = Enrollment.of(
                tutee,
                lecture,
                lecturePrice
        );
        // TODO - CHECK
        enrollment = enrollmentRepository.save(enrollment);
        tutee.addEnrollment(enrollment);
        lecture.addEnrollment(enrollment);

        // 수강 시 채팅방 자동 생성
        chatService.createChatroom(lecture.getTutor(), tutee, enrollment);
        // 강의 등록 시 튜터에게 알림 전송
        notificationService.createNotification(lecture.getTutor().getUser(), NotificationType.ENROLLMENT);

        return enrollment;
    }

    @Override
    public Cancellation cancel(User user, Long lectureId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.EntityType.ENROLLMENT));

        // TODO - 환불

        // TODO - Entity Listener 활용해 변경
        Cancellation cancellation = cancellationRepository.save(Cancellation.of(enrollment));
        enrollment.cancel();

        chatService.deleteChatroom(enrollment);

        return cancellation;
    }

//    @Override
//    public void close(User user, Long lectureId, Long enrollmentId) {
//
//        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user))
//                .orElseThrow(() -> new UnauthorizedException(RoleType.TUTOR));
//
//        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
//                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
//
//        Enrollment enrollment = enrollmentRepository.findByLectureAndId(lecture, enrollmentId)
//                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.EntityType.ENROLLMENT));
//
//        enrollment.close();
//        // 수강 종료 시 채팅방 삭제
//        // enrollment.setChatroom(null);
//        chatService.deleteChatroom(enrollment);
//    }
    @Override
    public void close(User user, Long lectureId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.EntityType.ENROLLMENT));

        enrollment.close();
        // 수강 종료 시 채팅방 삭제
        // enrollment.setChatroom(null);
        chatService.deleteChatroom(enrollment);
    }
}
