package com.tutor.tutorlab.modules.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.controller.response.ReviewWithSimpleLectureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class ReviewQueryRepositoryTest {

    @Autowired
    UserRepository userRepository;
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
    }

    @Test
    void findReviewsWithChildByLecture() {
    }

    @Test
    void findReviewsWithChildAndSimpleLectureByUser() {

        // given
        assertNotNull(reviewQueryRepository);
        assertNotNull(userRepository);

        // when
        User user = userRepository.findAll().stream().filter(u -> u.getRole().equals(RoleType.TUTEE)).findFirst()
                .orElseThrow(RuntimeException::new);
        Page<ReviewWithSimpleLectureResponse> responses = reviewQueryRepository.findReviewsWithChildAndSimpleLectureByUser(user, PageRequest.of(0, 20));

        // then
        responses.forEach(System.out::println);
    }
}