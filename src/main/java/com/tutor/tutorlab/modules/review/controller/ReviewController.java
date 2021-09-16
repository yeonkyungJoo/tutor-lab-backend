package com.tutor.tutorlab.modules.review.controller;

import com.tutor.tutorlab.modules.account.controller.AbstractController;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController extends AbstractController {

    private final ReviewService reviewService;

    // TODO - 유저별 리뷰 확인 : /my-reviews
    // 강의별 리뷰 리스트 : /lectures/{lecture_id}/reviews
    // 리뷰 조회 : /lectures/{lecture_id}/reviews/{review_id} or /reviews/{review_id}

    // 튜티 수강내역별 리뷰 리스트 : /my-lectures/{lecture_id}/reviews
    // 튜티 리뷰 작성 : /my-lectures/{lecture_id}/reviews or /my-enrollments/{enrollment_id}/reviews
    // 튜티 리뷰 수정 : /my-lectures/{lecture_id}/reviews/{review_id} or /my-enrollments/{enrollment_id}/reviews/{review_id}
    // 튜티 리뷰 삭제 : /my-lectures/{lecture_id}/reviews/{review_id} or /my-enrollments/{enrollment_id}/reviews/{review_id}

    // 튜터 강의별 리뷰 리스트 : /my-lectures/{lecture_id}/reviews
    // 튜터 리뷰 개별 조회 : /my-lectures/{lecture_id}/reviews/{review_id}
    // 튜터 리뷰 작성 : /my-lectures/{lecture_id}/reviews/{parent_id}
    // 튜터 리뷰 수정 : /my-lectures/{lecture_id}/reviews/{parent_id}/children/review_id}
    // 튜터 리뷰 삭제 : /my-lectures/{lecture_id}/reviews/{parent_id}/children/{review_id}
}
