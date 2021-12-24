package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentWithSimpleLectureResponse;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.controller.response.ReviewWithSimpleLectureResponse;
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
    private final EnrollmentService enrollmentService;

    // TODO - 강의 추가
    @ApiOperation("작성한 리뷰(+강의) 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getReviews(@CurrentUser User user,
                                        @RequestParam(defaultValue = "1") Integer page) {
        Page<ReviewWithSimpleLectureResponse> reviews = reviewService.getReviewWithSimpleLectureResponses(user, page);
        return ResponseEntity.ok(reviews);
    }

    @ApiOperation("리뷰 조회")
    @GetMapping("/{review_id}")
    public ResponseEntity<?> getReview(@PathVariable(name = "review_id") Long reviewId) {
        ReviewResponse review = reviewService.getReviewResponse(reviewId);
        return ResponseEntity.ok(review);
    }

    @ApiOperation("리뷰 미작성 강의 리스트 - 페이징")
    @GetMapping("/unreviewed")
    public ResponseEntity<?> getUnreviewedLecturesOfTutee(@CurrentUser User user,
                                                          @RequestParam(defaultValue = "1") Integer page) {
        Page<EnrollmentWithSimpleLectureResponse> lectures = enrollmentService.getEnrollmentWithSimpleLectureResponses(user, false, page);
        return ResponseEntity.ok(lectures);
    }

}
