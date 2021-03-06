package com.tutor.tutorlab.modules.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.controller.response.ReviewWithSimpleLectureResponse;
import com.tutor.tutorlab.modules.review.vo.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ReviewQueryRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    EntityManager em;

    private JPAQueryFactory jpaQueryFactory;
    private ReviewQueryRepository reviewQueryRepository;

    @BeforeEach
    void setup() {
        jpaQueryFactory = new JPAQueryFactory(em);
        reviewQueryRepository = new ReviewQueryRepository(jpaQueryFactory, em);
    }

    @Test
    void findReviewsWithUserByLecture() {

        // given
        Lecture lecture = lectureRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);
        // when
        Page<Review> reviews = reviewQueryRepository.findReviewsWithUserByLecture(lecture, Pageable.ofSize(20));
        // then
        for (Review review : reviews.getContent()) {
            System.out.println(review);
        }
    }

    @Test
    void findReviewsWithChildByLecture() {

        // given
        Lecture lecture = lectureRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);
        // when
        Page<ReviewResponse> reviews = reviewQueryRepository.findReviewsWithChildByLecture(lecture, Pageable.ofSize(20));
        // then
        for (ReviewResponse review : reviews.getContent()) {
            System.out.println(review);
        }
    }

    @Test
    void findReviewsWithChildAndSimpleLectureByUser() {

        // given
        assertNotNull(reviewQueryRepository);
        assertNotNull(userRepository);

        User user = userRepository.findAll().stream()
                .filter(u -> u.getRole().equals(RoleType.TUTEE)).findFirst()
                .orElseThrow(RuntimeException::new);

        // when
        Page<ReviewWithSimpleLectureResponse> responses = reviewQueryRepository.findReviewsWithChildAndSimpleLectureByUser(user, PageRequest.of(0, 20));

        // then
        responses.forEach(System.out::println);
    }
}