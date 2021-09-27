package com.tutor.tutorlab.config;

import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.*;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitService {

    private final LoginService loginService;
    private final TutorService tutorService;
    private final LectureService lectureService;
    private final EnrollmentService enrollmentService;

    private final ReviewService reviewService;

    private final UserRepository userRepository;
    private final TuteeRepository tuteeRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final TutorRepository tutorRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ChatroomRepository chatroomRepository;
    private final ReviewRepository reviewRepository;

    private SignUpRequest getSignUpRequest(String name) {
        return SignUpRequest.builder()
                .username(name + "@email.com")
                .password("password")
                .passwordConfirm("password")
                .name(name)
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
    }

    private CareerCreateRequest getCareerCreateRequest(String companyName, String duty) {
        return CareerCreateRequest.builder()
                .companyName(companyName)
                .duty(duty)
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
    }

    private EducationCreateRequest getEducationCreateRequest(String schoolName, String major) {
        return EducationCreateRequest.builder()
                .schoolName(schoolName)
                .major(major)
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
    }

    private TutorSignUpRequest getTutorSignUpRequest(String subjects, String companyName, String duty, String schoolName, String major) {
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects(subjects)
                .specialist(false)
                .build();
        tutorSignUpRequest.addCareerCreateRequest(getCareerCreateRequest(companyName, duty));
        tutorSignUpRequest.addEducationCreateRequest(getEducationCreateRequest(schoolName, major));
        return tutorSignUpRequest;
    }

    private LectureCreateRequest.LecturePriceCreateRequest getLecturePriceCreateRequest(Long pertimeCost, Integer pertimeLecture, Integer totalTime) {
        return LectureCreateRequest.LecturePriceCreateRequest.builder()
                .isGroup(true)
                .groupNumber(3)
                .pertimeCost(pertimeCost)
                .pertimeLecture(pertimeLecture)
                .totalCost(pertimeCost * pertimeLecture)
                .totalTime(totalTime)
                .build();
    }

    private LectureCreateRequest.LectureSubjectCreateRequest getLectureSubjectCreateRequest(String krSubject) {
        return LectureCreateRequest.LectureSubjectCreateRequest.builder()
                .parent("개발")
                .krSubject(krSubject)
                .build();
    }

    private LectureCreateRequest getLectureCreateRequest(String title, Long pertimeCost, Integer pertimeLecture, Integer totalTime, String krSubject) {

        LectureCreateRequest.LecturePriceCreateRequest price1 = getLecturePriceCreateRequest(pertimeCost, pertimeLecture, totalTime);
        LectureCreateRequest.LectureSubjectCreateRequest subject1 = getLectureSubjectCreateRequest(krSubject);

        return LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title(title)
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE))
                .lecturePrices(Arrays.asList(price1))
                .subjects(Arrays.asList(subject1))
                .build();
    }

    private TuteeReviewCreateRequest getTuteeReviewCreateRequest(Integer score, String content) {
        return TuteeReviewCreateRequest.builder()
                .score(score)
                .content(content)
                .build();
    }

    private TuteeReviewUpdateRequest getTuteeReviewUpdateRequest(Integer score, String content) {
        return TuteeReviewUpdateRequest.builder()
                .score(score)
                .content(content)
                .build();
    }

    private TutorReviewCreateRequest getTutorReviewCreateRequest(String content) {
        return TutorReviewCreateRequest.builder()
                .content(content)
                .build();
    }

    private TutorReviewUpdateRequest getTutorReviewUpdateRequest(String content) {
        return TutorReviewUpdateRequest.builder()
                .content(content)
                .build();
    }

    // @PostConstruct
    @Transactional
    void init() {

        reviewRepository.deleteAll();
        chatroomRepository.deleteAll();
        enrollmentRepository.deleteAll();
        lectureRepository.deleteAll();
        careerRepository.deleteAll();
        educationRepository.deleteAll();
        tutorRepository.deleteAll();
        tuteeRepository.deleteAll();
        userRepository.deleteAll();

        // user / tutee
        User user1 = loginService.signUp(getSignUpRequest("user1"));
        Tutee tutee1 = loginService.verifyEmail(user1.getUsername(), user1.getEmailVerifyToken());

        User user2 = loginService.signUp(getSignUpRequest("user2"));
        Tutee tutee2 = loginService.verifyEmail(user2.getUsername(), user2.getEmailVerifyToken());

        User user3 = loginService.signUp(getSignUpRequest("user3"));
        Tutee tutee3 = loginService.verifyEmail(user3.getUsername(), user3.getEmailVerifyToken());

        User user4 = loginService.signUp(getSignUpRequest("user4"));
        Tutee tutee4 = loginService.verifyEmail(user4.getUsername(), user4.getEmailVerifyToken());

        User user5 = loginService.signUp(getSignUpRequest("user5"));
        Tutee tutee5 = loginService.verifyEmail(user5.getUsername(), user5.getEmailVerifyToken());

        Tutor tutor1 = tutorService.createTutor(user4, getTutorSignUpRequest("python,java", "company1", "engineer", "school1", "computer"));
        Tutor tutor2 = tutorService.createTutor(user5, getTutorSignUpRequest("go,java", "company2", "engineer", "school2", "science"));

        // lecture
        Lecture lecture1 = lectureService.createLecture(user4, getLectureCreateRequest("파이썬강의", 1000L, 3, 10, "파이썬"));
        Lecture lecture2 = lectureService.createLecture(user4, getLectureCreateRequest("자바강의", 3000L, 3, 10, "자바"));
        Lecture lecture3 = lectureService.createLecture(user5, getLectureCreateRequest("자바강의", 2000L, 5, 20, "자바"));

        // enrollment
        // chatroom
        enrollmentService.enroll(user1, lecture1.getId());
        enrollmentService.enroll(user1, lecture2.getId());
        enrollmentService.enroll(user2, lecture1.getId());
        enrollmentService.enroll(user2, lecture2.getId());
        enrollmentService.enroll(user3, lecture3.getId());

        // review
        Review parent1 = reviewService.createTuteeReview(user1, lecture1.getId(), getTuteeReviewCreateRequest(5, "좋아요"));
        Review child1 = reviewService.createTutorReview(user4, lecture1.getId(), parent1.getId(), getTutorReviewCreateRequest("감사합니다!"));

        Review parent2 = reviewService.createTuteeReview(user2, lecture1.getId(), getTuteeReviewCreateRequest(3, "별로에요"));
        Review child2 = reviewService.createTutorReview(user4, lecture1.getId(), parent2.getId(), getTutorReviewCreateRequest("아쉽네요!"));
    }

}
