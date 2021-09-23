package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class _ReviewServiceTest {
    // dummy 데이터 기반

//    @Autowired
//    EntityManager em;
//
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    TuteeRepository tuteeRepository;
//    @Autowired
//    TutorRepository tutorRepository;
//    @Autowired
//    LectureRepository lectureRepository;
//    @Autowired
//    EnrollmentRepository enrollmentRepository;
//
//    @Autowired
//    ReviewRepository reviewRepository;
//    @Autowired
//    ReviewService reviewService;
//
//    // TODO - static으로 변경
//    private TuteeReviewCreateRequest getTuteeReviewCreateRequest(Integer score, String content) {
//        return TuteeReviewCreateRequest.builder()
//                .score(score)
//                .content(content)
//                .build();
//    }
//
//    private TuteeReviewUpdateRequest getTuteeReviewUpdateRequest(Integer score, String content) {
//        return TuteeReviewUpdateRequest.builder()
//                .score(score)
//                .content(content)
//                .build();
//    }
//
//    private TutorReviewCreateRequest getTutorReviewCreateRequest(String content) {
//        return TutorReviewCreateRequest.builder()
//                .content(content)
//                .build();
//    }
//
//    private TutorReviewUpdateRequest getTutorReviewUpdateRequest(String content) {
//        return TutorReviewUpdateRequest.builder()
//                .content(content)
//                .build();
//    }
//
//    @DisplayName("튜티 리뷰 등록")
//    @WithAccount("user1")
//    @Test
//    void createTuteeReview() {
//
//        // given
//        User user = userRepository.findByName("user1");
//        Tutee tutee = tuteeRepository.findByUser(user);
//        // user1이 강의1에 등록된 상태
//
//        // when
//        reviewService.createTuteeReview(tutee, 1L, getTuteeReviewCreateRequest(5, "좋아요"));
//
//        // then
//        Lecture lecture = lectureRepository.findById(1L).get();
//        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
//        assertNotNull(enrollment);
////        reviewRepository.findByLecture(lecture).forEach(r -> System.out.println(r));
//    }
//
//    @DisplayName("튜티 리뷰 등록 - 수강 강의가 아닌 경우")
//    @WithAccount("user1")
//    @Test
//    void createTuteeReview_unEnrolled() {
//
//        // given
//        User user = userRepository.findByName("user1");
//        Tutee tutee = tuteeRepository.findByUser(user);
//
//        // when
//        // then
//        Assertions.assertThrows(EntityNotFoundException.class, () -> {
//            reviewService.createTuteeReview(tutee, 3L, getTuteeReviewCreateRequest(5, "좋아요"));
//        });
//
//    }
//
//    @DisplayName("튜티 리뷰 수정")
//    @WithAccount("user1")
//    @Test
//    void updateTuteeReview() {
//
//        // given
//        User user = userRepository.findByName("user1");
//        Tutee tutee = tuteeRepository.findByUser(user);
//        // user1이 강의1에 등록된 상태
//        Review review = reviewService.createTuteeReview(tutee, 1L, getTuteeReviewCreateRequest(5, "좋아요"));
//        reviewRepository.flush();
//
//        // when
//        reviewService.updateTuteeReview(tutee, 1L, review.getId(), getTuteeReviewUpdateRequest(3, "별로에요"));
//        reviewRepository.flush();
//
//        // then
//        Lecture lecture = lectureRepository.findById(1L).get();
//        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
//        assertNotNull(enrollment);
//
//        reviewRepository.findByEnrollment(enrollment).forEach(r -> System.out.println(r));
//    }
//
//    @DisplayName("튜티 리뷰 삭제")
//    @WithAccount("user1")
//    @Test
//    void deleteTuteeReview() {
//
//        // given
//        User user = userRepository.findByName("user1");
//        Tutee tutee = tuteeRepository.findByUser(user);
//        // user1이 강의1에 등록된 상태
//        Review review = reviewService.createTuteeReview(tutee, 1L, getTuteeReviewCreateRequest(5, "좋아요"));
//        reviewRepository.flush();
//
//        // when
//        reviewService.deleteTuteeReview(tutee, 1L, review.getId());
//
//        // then
//        Lecture lecture = lectureRepository.findById(1L).get();
//        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
//        assertNotNull(enrollment);
//        assertEquals(0, reviewRepository.findByEnrollment(enrollment).size());
//    }
//
//    private Review createTuteeReview(String tuteeName, Long lectureId, Integer score, String content) {
//
//        User user = userRepository.findByName(tuteeName);
//        Tutee tutee = tuteeRepository.findByUser(user);
//
//        Lecture lecture = lectureRepository.findById(lectureId).get();
//        assertNotNull(lecture);
//        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
//        assertNotNull(enrollment);
//
//        Review review = reviewService.createTuteeReview(tutee, lectureId, getTuteeReviewCreateRequest(score, content));
//        return review;
//    }
//
//    @DisplayName("튜터 리뷰 등록")
//    @WithAccount("user4")
//    @Test
//    void createTutorReview() {
//
//        // given
//        User user = userRepository.findByName("user4");
//        Tutor tutor = tutorRepository.findByUser(user);
//        // tutor - 강의 1
//        Review parent = createTuteeReview("user1", 1L, 5, "좋아요");
//
//        // when
//        Review child = reviewService.createTutorReview(tutor, 1L, parent.getId(), getTutorReviewCreateRequest("감사합니다!"));
//
//        // then
//        Lecture lecture = lectureRepository.findById(1L).get();
//        reviewRepository.findByLecture(lecture).forEach(r -> System.out.println(r.getChildren().size() != 0 ? r.getChildren().get(0).getId() : null));
//        parent = reviewRepository.findById(parent.getId()).get();
//        assertNotNull(parent);
//        assertNull(parent.getParent());
//
//        child = reviewRepository.findById(child.getId()).get();
//        assertNotNull(child);
//        assertNotNull(child.getParent());
//        assertEquals(parent, child.getParent());
//        assertEquals(0, child.getChildren().size());
//
//        assertEquals(1, parent.getChildren().size());
//        assertEquals(child, parent.getChildren().get(0));
//    }
//
//    @DisplayName("튜터 리뷰 수정")
//    @WithAccount("user4")
//    @Test
//    void updateTutorReview() {
//
//        // given
//        User user = userRepository.findByName("user4");
//        Tutor tutor = tutorRepository.findByUser(user);
//        // tutor - 강의 1
//        Review parent = createTuteeReview("user1", 1L, 5, "좋아요");
//        Review child = reviewService.createTutorReview(tutor, 1L, parent.getId(), getTutorReviewCreateRequest("감사합니다!"));
//
//        // when
//        reviewService.updateTutorReview(tutor, 1L, parent.getId(), child.getId(), getTutorReviewUpdateRequest("감사합니다!!!"));
//        em.flush();
//        em.clear();
//
//        // then
//        System.out.println(reviewRepository.getById(child.getId()));
//    }
//
//    @DisplayName("튜터 리뷰 삭제")
//    @WithAccount("user4")
//    @Test
//    void deleteTutorReview() {
//
//        // given
//        User user = userRepository.findByName("user4");
//        Tutor tutor = tutorRepository.findByUser(user);
//        // tutor - 강의 1
//        Review parent = createTuteeReview("user1", 1L, 5, "좋아요");
//        Review child = reviewService.createTutorReview(tutor, 1L, parent.getId(), getTutorReviewCreateRequest("감사합니다!"));
//
//        // when
//        reviewService.deleteTutorReview(tutor, 1L, parent.getId(), child.getId());
//        // em.flush();
//        // em.clear();
//
//        // then
//        parent = reviewRepository.getById(parent.getId());
//        child = reviewRepository.findById(child.getId()).orElse(null);
//        assertNotNull(parent);
//        assertNull(child);
//        assertEquals(0, parent.getChildren().size());
//    }

}