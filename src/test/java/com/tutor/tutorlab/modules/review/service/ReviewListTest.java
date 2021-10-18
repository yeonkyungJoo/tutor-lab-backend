package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class ReviewListTest {

    @Autowired
    ReviewService reviewService;

    @Test
    void getReviewResponsesOfLecture() {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> START");
        Page<ReviewResponse> reviewResponses = reviewService.getReviewResponsesOfLecture(10L, 1);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> END");
    }
}
