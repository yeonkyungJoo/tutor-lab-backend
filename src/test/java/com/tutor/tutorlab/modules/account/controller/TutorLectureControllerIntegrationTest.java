package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.configuration.annotation.MockMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@MockMvcTest
class TutorLectureControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

//    @Test
//    void getLectures() {
//    }
//
//    @Test
//    void getLecture() {
//    }
//
//    @Test
//    void getReviewsOfLecture() {
//    }
//
//    @Test
//    void getReviewOfLecture() {
//    }

    @Test
    void newReview() {
    }

    @Test
    void editReview() {
    }

    @Test
    void deleteReview() {
    }

//    @Test
//    void getTuteesOfLecture() {
//    }
//
//    @Test
//    void getEnrollmentsOfLecture() {
//    }

    @Test
    void close() {
    }
}