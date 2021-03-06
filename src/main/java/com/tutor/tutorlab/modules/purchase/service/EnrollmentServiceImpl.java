package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.AbstractService;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.service.ChatService;
import com.tutor.tutorlab.modules.firebase.service.AndroidPushNotificationsService;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.notification.enums.NotificationType;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentWithSimpleLectureResponse;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentQueryRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final EnrollmentQueryRepository enrollmentQueryRepository;
    private final CancellationRepository cancellationRepository;
    private final TuteeRepository tuteeRepository;

    private final LectureRepository lectureRepository;
    private final LecturePriceRepository lecturePriceRepository;

    private final ChatroomRepository chatroomRepository;
    private final ChatService chatService;
    private final ReviewRepository reviewRepository;

    private final AndroidPushNotificationsService androidPushNotificationsService;
    private final NotificationService notificationService;

    private Page<Lecture> getLecturesOfTutee(User user, Integer page) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        return enrollmentRepository.findByTuteeAndCanceledFalseAndClosedFalse(tutee, PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(Enrollment::getLecture);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LectureResponse> getLectureResponsesOfTutee(User user, Integer page) {
        return getLecturesOfTutee(user, page).map(LectureResponse::new);
    }

    // getUnreviewedLecturesOfTutee
    @Transactional(readOnly = true)
    @Override
    public Page<EnrollmentWithSimpleLectureResponse> getEnrollmentWithSimpleLectureResponses(User user, boolean reviewed, Integer page) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        return enrollmentQueryRepository.findEnrollments(tutee, reviewed, Pageable.ofSize(page));
    }

    @Override
    public Enrollment createEnrollment(User user, Long lectureId, Long lecturePriceId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        // TODO - CHECK : lecture & tutor - fetch join
        // TODO - CHECK : lecture??? enrollment??? null vs tutee??? enrollment??? size = 0
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        LecturePrice lecturePrice = lecturePriceRepository.findByLectureAndId(lecture, lecturePriceId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE_PRICE));

        // ??????/????????? ?????? ????????? ??????
        if (enrollmentRepository.findAllByTuteeIdAndLectureId(tutee.getId(), lecture.getId()).isPresent()) {
            throw new AlreadyExistException(ENROLLMENT);
        }

        // TODO - ?????? ????????????
        // TODO - ?????? ?????? X ?????? (UNIQUE)

        // ?????? ???
        // TODO - CHECK
        Enrollment enrollment = enrollmentRepository.save(Enrollment.buildEnrollment(tutee, lecture, lecturePrice));

        Tutor tutor = lecture.getTutor();
        User tutorUser = tutor.getUser();
        // ?????? ??? ????????? ?????? ??????
        chatService.createChatroom(tutor, tutee, enrollment);
        // ?????? ?????? ??? ???????????? ?????? ??????
        notificationService.createNotification(tutorUser, NotificationType.ENROLLMENT);
        // androidPushNotificationsService.send(tutorUser.getFcmToken(), "?????? ??????", String.format("%s?????? %s ????????? ??????????????????", user.getNickname(), lecture.getTitle()));
        return enrollment;
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
//        // ?????? ?????? ??? ????????? ??????
//        // enrollment.setChatroom(null);
//        chatService.deleteChatroom(enrollment);
//    }
    @Override
    public void close(User user, Long lectureId) {

        Tutee tutee = Optional.ofNullable(tuteeRepository.findByUser(user))
                .orElseThrow(() -> new UnauthorizedException(TUTEE));

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Enrollment enrollment = enrollmentRepository.findByTuteeAndLectureAndCanceledFalseAndClosedFalse(tutee, lecture)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.EntityType.ENROLLMENT));

        enrollment.close();
        // ?????? ?????? ??? ????????? ??????
        // enrollment.setChatroom(null);
        chatService.deleteChatroom(enrollment);
    }

    @Override
    public void deleteEnrollment(Enrollment enrollment) {

        // TODO - Optional ?????????
        // ?????? ??????/????????? ????????? ??????
        // chatService.deleteChatroom(enrollment);
        // chatroomRepository.deleteByEnrollment(enrollment);
        chatroomRepository.findByEnrollment(enrollment).ifPresent(
                chatroom -> chatroomRepository.delete(chatroom)
        );

        Optional.ofNullable(reviewRepository.findByEnrollment(enrollment)).ifPresent(
                review -> {
                    review.delete();
                    reviewRepository.delete(review);
                }
        );

        cancellationRepository.deleteByEnrollment(enrollment);

        enrollment.delete();
        enrollmentRepository.deleteEnrollmentById(enrollment.getId());
    }
}
