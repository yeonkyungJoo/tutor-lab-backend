package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;

@MockMvcTest
class EnrollmentControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorService tutorService;
    @Autowired
    LectureRepository lectureRepository;

    @BeforeEach
    public void init() throws Exception {

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
                .krSubject("자바")
                .build();

        lecture.addPrice(lecturePrice);
        lecture.addSubject(lectureSubject);
        lectureRepository.save(lecture);

    }

    @Test
    @DisplayName("강의 구매/수강")
    @WithAccount("yk")
    void enroll() {

        // Given
        // When
        // Then

    }

    @Test
    @DisplayName("강의 구매 취소")
    @WithAccount("yk")
    void cancel() {

        // Given
        // When
        // Then
    }
}