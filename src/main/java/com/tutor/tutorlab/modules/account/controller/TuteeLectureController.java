package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstructUtil;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import com.tutor.tutorlab.modules.review.vo.Review;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/tutees/my-lectures")
@RestController
@RequiredArgsConstructor
public class TuteeLectureController {

    private final EnrollmentService enrollmentService;
    private final LectureMapstructUtil lectureMapstructUtil;

    private final LectureService lectureService;
    private final ReviewService reviewService;

    @ApiOperation("수강 강의 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getLectures(@CurrentUser User user,
                                         @RequestParam(defaultValue = "1") Integer page) {

        Page<LectureResponse> lectures = enrollmentService.getLecturesOfTutee(user, page)
                // TODO - MapstructUtil
                .map(lectureMapstructUtil::getLectureResponse);
        return ResponseEntity.ok(lectures);
    }

    // TODO - CHECK : user
    @ApiOperation("수강 강의 개별 조회")
    @GetMapping("/{lecture_id}")
    public ResponseEntity<?> getLecture(@PathVariable(name = "lecture_id") Long lectureId) {
        Lecture lecture = lectureService.getLecture(lectureId);
        return ResponseEntity.ok(new LectureResponse(lecture));
    }

    @ApiOperation("강의 수강 취소")
    @DeleteMapping("/{lecture_id}")
    public ResponseEntity<?> cancel(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId) {
        enrollmentService.cancel(user, lectureId);
        return ResponseEntity.ok().build();
    }

    // TODO - CHECK : user가 필요한가?
    @ApiOperation("수강 강의별 리뷰 조회 - 페이징")
    @GetMapping("/{lecture_id}/reviews")
    public ResponseEntity<?> getReviewsOfLecture(@PathVariable(name = "lecture_id") Long lectureId,
                                                 @RequestParam(defaultValue = "1") Integer page) {

        Page<ReviewResponse> reviews = reviewService.getReviewsOfLecture(lectureId, page)
                .map(ReviewResponse::new);
        return ResponseEntity.ok(reviews);
    }

    // TODO - CHECK : user가 필요한가?
    @ApiOperation("수강 강의별 리뷰 개별 조회")
    @GetMapping("/{lecture_id}/reviews/{review_id}")
    public ResponseEntity<?> getReviewOfLecture(@PathVariable(name = "lecture_id") Long lectureId,
                                                @PathVariable(name = "review_id") Long reviewId) {

        Review review = reviewService.getReview(lectureId, reviewId);
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    @ApiOperation("튜티 리뷰 작성")
    @PostMapping("/{lecture_id}/reviews")
    public ResponseEntity<?> newReview(@CurrentUser User user,
                                       @PathVariable(name = "lecture_id") Long lectureId,
                                       @RequestBody @Valid TuteeReviewCreateRequest tuteeReviewCreateRequest) {

        reviewService.createTuteeReview(user, lectureId, tuteeReviewCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("튜티 리뷰 수정")
    @PutMapping("/{lecture_id}/reviews/{review_id}")
    public ResponseEntity<?> editReview(@CurrentUser User user,
                                        @PathVariable(name = "lecture_id") Long lectureId,
                                        @PathVariable(name = "review_id") Long reviewId,
                                        @RequestBody @Valid TuteeReviewUpdateRequest tuteeReviewUpdateRequest) {

        reviewService.updateTuteeReview(user, lectureId, reviewId, tuteeReviewUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("튜티 리뷰 삭제")
    @DeleteMapping("/{lecture_id}/reviews/{review_id}")
    public ResponseEntity<?> deleteReview(@CurrentUser User user,
                                          @PathVariable(name = "lecture_id") Long lectureId,
                                          @PathVariable(name = "review_id") Long reviewId) {

        reviewService.deleteTuteeReview(user, lectureId, reviewId);
        return ResponseEntity.ok().build();
    }
}
