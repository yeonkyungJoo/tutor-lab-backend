package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"TuteeReviewController"})
@RequestMapping("/tutees/my-reviews")
@RestController
@RequiredArgsConstructor
public class TuteeReviewController {

    private final ReviewService reviewService;

    @ApiOperation("작성한 리뷰 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getReviews(@CurrentUser User user,
                                        @RequestParam(defaultValue = "1") Integer page) {
        Page<ReviewResponse> reviews = reviewService.getReviewResponses(user, page);
        return ResponseEntity.ok(reviews);
    }

    @ApiOperation("리뷰 조회")
    @GetMapping("{/review_id}")
    public ResponseEntity<?> getReview(@PathVariable(name = "review_id") Long reviewId) {
        ReviewResponse review = reviewService.getReviewResponse(reviewId);
        return ResponseEntity.ok(review);
    }

}
