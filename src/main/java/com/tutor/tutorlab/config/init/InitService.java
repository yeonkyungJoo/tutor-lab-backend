package com.tutor.tutorlab.config.init;

import com.tutor.tutorlab.modules.account.enums.EducationLevelType;
import com.tutor.tutorlab.modules.account.repository.*;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TutorCancellationService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.lecture.embeddable.LearningKind;
import com.tutor.tutorlab.modules.lecture.enums.LearningKindType;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureSubjectRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.notification.repository.NotificationRepository;
import com.tutor.tutorlab.modules.purchase.controller.request.CancellationCreateRequest;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.service.CancellationService;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import com.tutor.tutorlab.modules.review.vo.Review;
import com.tutor.tutorlab.modules.subject.repository.SubjectRepository;
import com.tutor.tutorlab.modules.subject.vo.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static com.tutor.tutorlab.config.init.TestDataBuilder.*;

@Component
@RequiredArgsConstructor
public class InitService {

    private final LoginService loginService;
    private final TutorService tutorService;
    private final LectureService lectureService;
    private final EnrollmentService enrollmentService;
    private final CancellationService cancellationService;
    private final ReviewService reviewService;
    private final TutorCancellationService tutorCancellationService;

    private final UserRepository userRepository;
    private final TuteeRepository tuteeRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final TutorRepository tutorRepository;
    private final LectureRepository lectureRepository;
    private final LecturePriceRepository lecturePriceRepository;
    private final LectureSubjectRepository lectureSubjectRepository;

    private final CancellationRepository cancellationRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ChatroomRepository chatroomRepository;
    private final ReviewRepository reviewRepository;

    private final NotificationRepository notificationRepository;
    private final SubjectRepository subjectRepository;

    // @PostConstruct
    @Transactional
    void init() {

        reviewRepository.deleteAll();
        chatroomRepository.deleteAll();
        cancellationRepository.deleteAll();
        enrollmentRepository.deleteAllEnrollments();
        lecturePriceRepository.deleteAll();
        lectureSubjectRepository.deleteAll();
        lectureRepository.deleteAll();
        careerRepository.deleteAll();
        educationRepository.deleteAll();
        tutorRepository.deleteAll();
        tuteeRepository.deleteAll();
        notificationRepository.deleteAll();
        userRepository.deleteAll();
        subjectRepository.deleteAll();

        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.IT), "자바"));
        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.IT), "파이썬"));
        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.IT), "C/C++"));
        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.LANGUAGE), "영어"));
        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.LANGUAGE), "중국어"));

        // user / tutee
        User user1 = loginService.signUp(getSignUpRequest("user1", "부산광역시 기장군 내리"));
        Tutee tutee1 = loginService.verifyEmail(user1.getUsername(), user1.getEmailVerifyToken());

        User user2 = loginService.signUp(getSignUpRequest("user2", "서울특별시 종로구 효자동"));
        Tutee tutee2 = loginService.verifyEmail(user2.getUsername(), user2.getEmailVerifyToken());

        User user3 = loginService.signUp(getSignUpRequest("user3", "경상북도 영주시 영주동"));
        Tutee tutee3 = loginService.verifyEmail(user3.getUsername(), user3.getEmailVerifyToken());

        User user4 = loginService.signUp(getSignUpRequest("user4", "부산광역시 금정구 금사동"));
        Tutee tutee4 = loginService.verifyEmail(user4.getUsername(), user4.getEmailVerifyToken());

        User user5 = loginService.signUp(getSignUpRequest("user5", "경상남도 진주시 망경동"));
        Tutee tutee5 = loginService.verifyEmail(user5.getUsername(), user5.getEmailVerifyToken());

        Tutor tutor1 = tutorService.createTutor(user4, getTutorSignUpRequest("engineer", "company1", EducationLevelType.HIGH, "school1", "computer"));
        Tutor tutor2 = tutorService.createTutor(user5, getTutorSignUpRequest("engineer", "company2", EducationLevelType.UNIVERSITY, "school2", "computer"));

        // lecture
        Lecture lecture1 = lectureService.createLecture(user4, getLectureCreateRequest("파이썬강의", 1000L, 3, 10, LearningKindType.IT, "파이썬"));
        Lecture lecture2 = lectureService.createLecture(user4, getLectureCreateRequest("자바강의", 3000L, 3, 10, LearningKindType.IT, "자바"));
        Lecture lecture3 = lectureService.createLecture(user5, getLectureCreateRequest("C/C++강의", 2000L, 5, 20, LearningKindType.IT, "C/C++"));

        // enrollment
        // chatroom
        LecturePrice lecturePrice1 = lecturePriceRepository.findByLecture(lecture1).get(0);
        LecturePrice lecturePrice2 = lecturePriceRepository.findByLecture(lecture2).get(0);
        LecturePrice lecturePrice3 = lecturePriceRepository.findByLecture(lecture3).get(0);
        Enrollment enrollment1 = enrollmentService.createEnrollment(user1, lecture1.getId(), lecturePrice1.getId());
        Enrollment enrollment2 = enrollmentService.createEnrollment(user1, lecture2.getId(), lecturePrice2.getId());
        Enrollment enrollment3 = enrollmentService.createEnrollment(user2, lecture1.getId(), lecturePrice1.getId());
        Enrollment enrollment4 = enrollmentService.createEnrollment(user2, lecture2.getId(), lecturePrice2.getId());
        Enrollment enrollment5 = enrollmentService.createEnrollment(user3, lecture3.getId(), lecturePrice3.getId());

        // 강의 종료
        // enrollmentService.close(user4, lecture1.getId(), enrollment1.getId());
        enrollmentService.close(user1, lecture1.getId());
        
        // 강의 취소 요청
        Cancellation cancellation = cancellationService.cancel(user1, lecture2.getId(), CancellationCreateRequest.of("너무 어려워요"));
        tutorCancellationService.approve(user4, cancellation.getId());
        
        // review
        Review parent1 = reviewService.createTuteeReview(user1, lecture1.getId(), getTuteeReviewCreateRequest(5, "좋아요"));
        Review child1 = reviewService.createTutorReview(user4, lecture1.getId(), parent1.getId(), getTutorReviewCreateRequest("감사합니다!"));

        Review parent2 = reviewService.createTuteeReview(user2, lecture1.getId(), getTuteeReviewCreateRequest(3, "별로에요"));
        Review child2 = reviewService.createTutorReview(user4, lecture1.getId(), parent2.getId(), getTutorReviewCreateRequest("아쉽네요!"));

        Review parent3 = reviewService.createTuteeReview(user1, lecture2.getId(), getTuteeReviewCreateRequest(1, "환불했어요"));
        Review child3 = reviewService.createTutorReview(user4, lecture2.getId(), parent3.getId(), getTutorReviewCreateRequest("죄송합니다"));
    }

}
