package com.tutor.tutorlab.modules.review.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    // 기존 데이터 테스트
    @Test
    void findWithUserByLecture() {
        // reviewRepository.findWithUserByLecture()
    }
}