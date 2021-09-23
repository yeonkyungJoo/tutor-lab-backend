package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureUpdateRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.review.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import com.tutor.tutorlab.modules.review.vo.Review;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;
    private final ReviewService reviewService;

    // TODO - 페이징
    // TODO - CHECK : @ModelAttribute
    @ApiOperation("강의 목록 조회")
    @GetMapping
    public ResponseEntity<?> getLectures(@ModelAttribute @Valid LectureListRequest lectureListRequest) {
        List<LectureResponse> lectures = lectureService.getLectures(lectureListRequest);
        return ResponseEntity.ok(lectures);
    }

    @ApiOperation("강의 개별 조회")
    @GetMapping(value = "/{lecture_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLecture(@PathVariable(name = "lecture_id") Long lectureId) {
        LectureResponse lecture = lectureService.getLectureResponse(lectureId);
        return ResponseEntity.ok(lecture);
    }

    @ApiOperation("강의 등록")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newLecture(@CurrentUser User user,
                                        @RequestBody @Validated(LectureCreateRequest.Order.class) LectureCreateRequest lectureCreateRequest) {
        lectureService.createLecture(user, lectureCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("강의 수정")
    @PutMapping("/{lecture_id}")
    public ResponseEntity<?> editLecture(@CurrentUser User user,
                                         @PathVariable(name = "lecture_id") Long lectureId,
                                         @RequestBody @Valid LectureUpdateRequest lectureUpdateRequest) {
        lectureService.updateLecture(user, lectureId, lectureUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("강의 삭제")
    @DeleteMapping("/{lecture_id}")
    public ResponseEntity<?> deleteLecture(@CurrentUser User user,
                                           @PathVariable(name = "lecture_id") Long lectureId) {
        lectureService.deleteLecture(user, lectureId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("강의별 리뷰 리스트 - 페이징")
    @GetMapping("/{lecture_id}/reviews")
    public ResponseEntity<?> getReviewsOfLecture(@PathVariable(name = "lecture_id") Long lectureId,
                                                 @RequestParam(defaultValue = "1") Integer page) {

        Page<ReviewResponse> reviews = reviewService.getReviewsOfLecture(lectureId, page)
                .map(ReviewResponse::new);
        return ResponseEntity.ok(reviews);
    }

    @ApiOperation("강의 리뷰 개별 조회")
    @GetMapping("/{lecture_id}/reviews/{review_id}")
    public ResponseEntity<?> getReviewOfLecture(@PathVariable(name = "lecture_id") Long lectureId,
                                                @PathVariable(name = "review_id") Long reviewId) {
        Review review = reviewService.getReview(lectureId, reviewId);
        return ResponseEntity.ok(new ReviewResponse(review));
    }

}
