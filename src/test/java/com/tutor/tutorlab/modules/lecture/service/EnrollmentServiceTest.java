package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.*;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.service.UserService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.request.EnrollmentRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.*;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
class EnrollmentServiceTest {

    private Long lectureId = null;

    @Autowired
    LoginService loginService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    TuteeRepository tuteeRepository;
    @Autowired
    TutorService tutorService;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    EducationRepository educationRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    EnrollmentService enrollmentService;
    @Autowired
    CancellationRepository cancellationRepository;

    @BeforeEach
    public void init() throws Exception {

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk2@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk2")
                .gender("FEMALE")
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .build();
        loginService.signUp(signUpRequest);

        User user = userRepository.findByName("yk2");

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

        lectureId = lectureRepository.save(lecture).getId();
    }

    @Test
    @DisplayName("강의 구매/수강")
    @WithAccount("yk")
    void enroll() {

        // Given
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setLectureId(lectureId);

        // When
        User user = userRepository.findByName("yk");
        Tutee tutee = tuteeRepository.findByUser(user);
        enrollmentService.enroll(user, enrollmentRequest);

        // Then
        assertEquals(1, enrollmentRepository.findByTutee(tutee).size());

        Enrollment enrollment = enrollmentRepository.findByTutee(tutee).get(0);
        assertNotNull(enrollment);
        assertEquals("test", enrollment.getLecture().getTitle());
        assertEquals("java,spring", enrollment.getLecture().getTutor().getSubjects());
        assertFalse(enrollment.getLecture().getTutor().isSpecialist());

        assertEquals("yk", enrollment.getTutee().getUser().getName());
    }

    @Test
    @DisplayName("회원/Tutee 탈퇴 시 구매내역 일괄 삭제")
    @WithAccount("yk")
    void quitTutee() {

        // Given
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setLectureId(lectureId);

        User user = userRepository.findByName("yk");
        Tutee tutee = tuteeRepository.findByUser(user);
        enrollmentService.enroll(user, enrollmentRequest);

        Assertions.assertEquals(2, userRepository.count());
        Assertions.assertEquals(2, tuteeRepository.count());
        Assertions.assertEquals(1, tutorRepository.count());
        Assertions.assertEquals(1, careerRepository.count());
        Assertions.assertEquals(1, educationRepository.count());
        Assertions.assertEquals(1, enrollmentRepository.count());
        Assertions.assertEquals(0, cancellationRepository.count());

        // When
        userService.deleteUser(user);

        // Then
        Assertions.assertEquals(2, userRepository.count());
        Assertions.assertEquals(1, tuteeRepository.count());
        Assertions.assertEquals(1, tutorRepository.count());
        Assertions.assertEquals(1, careerRepository.count());
        Assertions.assertEquals(1, educationRepository.count());
        Assertions.assertEquals(0, enrollmentRepository.count());

        Assertions.assertEquals(1, userRepository.findByDeleted(true).size());
        User deletedUser = userRepository.findByDeleted(true).get(0);
        Assertions.assertNotNull(deletedUser.getDeletedAt());
    }

    @Test
    @DisplayName("강의 구매 취소")
    @WithAccount("yk")
    void cancel() {

        // Given
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setLectureId(1L);

        User user = userRepository.findByName("yk");
        Tutee tutee = tuteeRepository.findByUser(user);
        enrollmentService.enroll(user, enrollmentRequest);

        // When
        enrollmentService.cancel(user, 1L);

        // Then
        assertEquals(0, enrollmentRepository.findByTutee(tutee).size());
        assertEquals(1, cancellationRepository.count());

        Cancellation cancellation = cancellationRepository.findAll().get(0);
        assertNotNull(cancellation);
        assertEquals("test", cancellation.getLecture().getTitle());
        assertEquals("yk", cancellation.getTutee().getUser().getName());
    }
}