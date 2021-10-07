package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@MockMvcTest
class LectureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LectureService lectureService;
    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TutorService tutorService;
    @Autowired
    TutorRepository tutorRepository;

    @Test
    void newLecture() {
    }

    @WithAccount("yk")
    @Test
    void newLecture_invalidInput() {

//        // Given
//        User user = userRepository.findByUsername("yk@email.com").orElse(null);
//        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
//                .subjects("java,spring")
//                .specialist(false)
//                .build();
//        tutorService.createTutor(user, tutorSignUpRequest);
//
//        // When
//        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest1
//                = LectureBuilder.getLecturePriceCreateRequest(true, null, 1000L, 3, 3000L, 10);
//        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest2
//                = LectureBuilder.getLecturePriceCreateRequest(false, 3, 1000L, 3, 30000L, 10);
//
//        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest1
//                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바");
//        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest2
//                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바스크립트");
//
//        LectureCreateRequest lectureCreateRequest = LectureCreateRequest.builder()
//                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
//                .title("제목")
//                .subTitle("소제목")
//                .introduce("소개")
//                .difficulty(DifficultyType.BEGINNER)
//                .content("<p>본문</p>")
//                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
//                .lecturePrices(Arrays.asList(lecturePriceCreateRequest1, lecturePriceCreateRequest2))
//                .subjects(Arrays.asList(lectureSubjectCreateRequest1, lectureSubjectCreateRequest2))
//                .build();
//
//        lectureService.createLecture(user, lectureCreateRequest);
    }

    @Test
    void editLecture() {
    }

    @Test
    void deleteLecture() {
    }

}