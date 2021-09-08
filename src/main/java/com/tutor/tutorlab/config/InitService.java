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
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import com.tutor.tutorlab.modules.purchase.controller.request.EnrollmentRequest;
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

    private final EducationRepository educationRepository;
    private final CareerRepository careerRepository;
    private final TutorRepository tutorRepository;
    private final TuteeRepository tuteeRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;

    private final EnrollmentService enrollmentService;

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

    @PostConstruct
    @Transactional
    void init() {

        // user / tutee
        loginService.signUp(getSignUpRequest("user1"));
        loginService.signUp(getSignUpRequest("user2"));
        loginService.signUp(getSignUpRequest("user3"));
        loginService.signUp(getSignUpRequest("user4"));
        loginService.signUp(getSignUpRequest("user5"));

        // tutor - career, education
        User user4 = userRepository.findByName("user4");
        User user5 = userRepository.findByName("user5");
        Tutor tutor1 = tutorService.createTutor(user4, getTutorSignUpRequest("python,java", "company1", "engineer", "school1", "computer"));
        Tutor tutor2 = tutorService.createTutor(user5, getTutorSignUpRequest("go,java", "company2", "engineer", "school2", "science"));

        // lecture
        // enrollment
        // chatroom
        // review
    }

//    @PostConstruct
//    @Transactional
//    void init() {
//
//        educationRepository.deleteAll();
//        careerRepository.deleteAll();
//        tutorRepository.deleteAll();
//        tuteeRepository.deleteAll();
//        userRepository.deleteAll();

//        Lecture lecture = Lecture.builder()
//                .tutor(tutor)
//                .title("test")
//                .subTitle("test")
//                .introduce("소개")
//                .content("test")
//                .difficultyType(DifficultyType.BEGINNER)
//                .systemTypes(Arrays.asList(SystemType.OFFLINE, SystemType.ONLINE))
//                .lecturePrices(new ArrayList<>())
//                .lectureSubjects(new ArrayList<>())
//                .build();
//
//        LecturePrice lecturePrice = LecturePrice.builder()
//                .isGroup(true)
//                .groupNumber(3)
//                .pertimeLecture(3)
//                .pertimeCost(10000L)
//                .totalTime(10)
//                .totalCost(300000L)
//                .build();
//
//        LectureSubject lectureSubject = LectureSubject.builder()
//                .parent("개발")
//                .enSubject("java")
//                .krSubject("자바")
//                .build();
//
//        lecture.addPrice(lecturePrice);
//        lecture.addSubject(lectureSubject);
//
//        Long lectureId = lectureRepository.save(lecture).getId();
//
//        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
//        enrollmentRequest.setLectureId(lectureId);
//
//        // When
//        Tutee tutee = tuteeRepository.findByUser(userRepository.findByName("tutee"));
//        enrollmentService.enroll(user, enrollmentRequest);


}
