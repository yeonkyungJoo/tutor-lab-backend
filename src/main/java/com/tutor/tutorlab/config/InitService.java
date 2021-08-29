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
import com.tutor.tutorlab.modules.lecture.controller.request.EnrollmentRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.service.EnrollmentService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
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

    @PostConstruct
    @Transactional
    void init() {

        educationRepository.deleteAll();
        careerRepository.deleteAll();
        tutorRepository.deleteAll();
        tuteeRepository.deleteAll();
        userRepository.deleteAll();

        // Tutee
        SignUpRequest signUpRequest1 = SignUpRequest.builder()
                .username("tutee@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("tutee")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        loginService.signUp(signUpRequest1);

        // Tutor
        SignUpRequest signUpRequest2 = SignUpRequest.builder()
                .username("tutor@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("tutor")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        loginService.signUp(signUpRequest2);

        User user = userRepository.findByName("tutor");

        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();

        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorSignUpRequest.addCareerCreateRequest(careerCreateRequest);
        tutorSignUpRequest.addEducationCreateRequest(educationCreateRequest);
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

        Lecture lecture = Lecture.builder()
                .tutor(tutor)
                .title("test")
                .subTitle("test")
                .introduce("소개")
                .content("test")
                .difficultyType(DifficultyType.BEGINNER)
                .systemTypes(Arrays.asList(SystemType.OFFLINE, SystemType.ONLINE))
                .lecturePrices(new ArrayList<>())
                .lectureSubjects(new ArrayList<>())
                .build();

        LecturePrice lecturePrice = LecturePrice.builder()
                .isGroup(true)
                .groupNumber(3)
                .pertimeLecture(3)
                .pertimeCost(10000L)
                .totalTime(10)
                .totalCost(300000L)
                .build();

        LectureSubject lectureSubject = LectureSubject.builder()
                .parent("개발")
                .enSubject("java")
                .krSubject("자바")
                .build();

        lecture.addPrice(lecturePrice);
        lecture.addSubject(lectureSubject);

        Long lectureId = lectureRepository.save(lecture).getId();

        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setLectureId(lectureId);

        // When
        Tutee tutee = tuteeRepository.findByUser(userRepository.findByName("tutee"));
        enrollmentService.enroll(user, enrollmentRequest);

        // OAuth 회원가입
        String username = "sj@email.com";
        if (userRepository.findByUsername(username) == null) {
            /*
            User user = User.builder()
                    .username(username)
                    .password(bCryptPasswordEncoder.encode(username))
                    .name("sj")
                    .gender(null)
                    .phoneNumber(null)
                    .email(null)
                    .nickname(null)
                    .bio(null)
                    .zone(null)
                    .role(RoleType.ROLE_TUTEE)
                    .provider(OAuthType.GOOGLE)
                    .providerId("google1")
                    .build();

            userRepository.save(user);
            */

            OAuthInfo oAuthInfo = new OAuthInfo() {
                @Override
                public String getProviderId() {
                    return "google1";
                }

                @Override
                public OAuthType getProvider() {
                    return OAuthType.GOOGLE;
                }

                @Override
                public String getName() {
                    return "sj";
                }

                @Override
                public String getEmail() {
                    return "sj@email.com";
                }
            };

            try {
                loginService.signUpOAuth(oAuthInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
