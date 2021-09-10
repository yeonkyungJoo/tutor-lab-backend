package com.tutor.tutorlab.config;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
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
import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import com.tutor.tutorlab.modules.purchase.controller.request.EnrollmentRequest;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitService {

    private final LoginService loginService;
    private final TutorService tutorService;
    private final LectureService lectureService;
    private final EnrollmentService enrollmentService;

    private final UserRepository userRepository;
    private final TuteeRepository tuteeRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;
    private final TutorRepository tutorRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ChatroomRepository chatroomRepository;

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

    private AddLectureRequest.AddLecturePriceRequest getAddLecturePriceRequest(Long pertimeCost, Integer pertimeLecture, Integer totalTime) {
        return AddLectureRequest.AddLecturePriceRequest.builder()
                .isGroup(true)
                .groupNumber(3)
                .pertimeCost(pertimeCost)
                .pertimeLecture(pertimeLecture)
                .totalCost(pertimeCost * pertimeLecture)
                .totalTime(totalTime)
                .build();
    }

    private AddLectureRequest.AddLectureSubjectRequest getAddLectureSubjectRequest(String krSubject) {
        return AddLectureRequest.AddLectureSubjectRequest.builder()
                .parent("개발")
                .krSubject(krSubject)
                .build();
    }

    private AddLectureRequest getAddLectureRequest(String title, Long pertimeCost, Integer pertimeLecture, Integer totalTime, String krSubject) {

        AddLectureRequest.AddLecturePriceRequest price1 = getAddLecturePriceRequest(pertimeCost, pertimeLecture, totalTime);
        AddLectureRequest.AddLectureSubjectRequest subject1 = getAddLectureSubjectRequest(krSubject);

        return AddLectureRequest.builder()
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

    private EnrollmentRequest getEnrollmentRequest(Long lectureId) {
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setLectureId(lectureId);

        return enrollmentRequest;
    }

    @PostConstruct
    @Transactional
    void init() {

        chatroomRepository.deleteAll();
        enrollmentRepository.deleteAll();
        lectureRepository.deleteAll();
        careerRepository.deleteAll();
        educationRepository.deleteAll();
        tutorRepository.deleteAll();
        tuteeRepository.deleteAll();
        userRepository.deleteAll();

        // user / tutee
        Tutee tutee1 = loginService.signUp(getSignUpRequest("user1"));
        Tutee tutee2 = loginService.signUp(getSignUpRequest("user2"));
        Tutee tutee3 = loginService.signUp(getSignUpRequest("user3"));
        Tutee tutee4 = loginService.signUp(getSignUpRequest("user4"));
        Tutee tutee5 = loginService.signUp(getSignUpRequest("user5"));

        // tutor - career, education
        User user4 = userRepository.findByName("user4");
        User user5 = userRepository.findByName("user5");
        Tutor tutor1 = tutorService.createTutor(user4, getTutorSignUpRequest("python,java", "company1", "engineer", "school1", "computer"));
        Tutor tutor2 = tutorService.createTutor(user5, getTutorSignUpRequest("go,java", "company2", "engineer", "school2", "science"));

        // lecture
        LectureResponse lectureResponse1 = lectureService.addLecture(getAddLectureRequest("파이썬강의", 1000L, 3, 10, "파이썬"), user4);
        LectureResponse lectureResponse2 = lectureService.addLecture(getAddLectureRequest("자바강의", 3000L, 3, 10, "자바"), user4);
        LectureResponse lectureResponse3 = lectureService.addLecture(getAddLectureRequest("자바강의", 2000L, 5, 20, "자바"), user5);

        // enrollment
        // chatroom
        enrollmentService.enroll(tutee1, getEnrollmentRequest(1L));
        enrollmentService.enroll(tutee1, getEnrollmentRequest(2L));
        enrollmentService.enroll(tutee2, getEnrollmentRequest(1L));
        enrollmentService.enroll(tutee2, getEnrollmentRequest(2L));
        enrollmentService.enroll(tutee3, getEnrollmentRequest(3L));

        // review
    }

}
