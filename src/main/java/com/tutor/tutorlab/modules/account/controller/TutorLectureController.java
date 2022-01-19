package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.response.Response;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentResponse;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.tutor.tutorlab.config.response.Response.created;
import static com.tutor.tutorlab.config.response.Response.ok;

@Api(tags = {"TutorLectureController"})
@RequestMapping("/tutors/my-lectures")
@RestController
@RequiredArgsConstructor
public class TutorLectureController {

    private final TutorLectureService tutorLectureService;
    private final LectureService lectureService;
    private final ReviewService reviewService;

    @ApiOperation("등록 강의 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getLectures(@CurrentUser User user,
                                         @RequestParam(defaultValue = "1") Integer page) {

        Page<LectureResponse> lectures = tutorLectureService.getLectureResponses(user, page);
        return ResponseEntity.ok(lectures);
    }

    // TODO - CHECK : user
    @ApiOperation("등록 강의 개별 조회")
    @GetMapping("/{lecture_id}")
    public ResponseEntity<?> getLecture(@PathVariable(name = "lecture_id") Long lectureId) {

        LectureResponse lecture = lectureService.getLectureResponse(lectureId);
        // lectureMapstructUtil.getLectureResponse(lecture);
        return ResponseEntity.ok(lecture);
    }

    @ApiOperation("등록 강의별 리뷰 조회 - 페이징")
    @GetMapping("/{lecture_id}/reviews")
    public ResponseEntity<?> getReviewsOfLecture(@PathVariable(name = "lecture_id") Long lectureId,
                                                 @RequestParam(defaultValue = "1") Integer page) {

        Page<ReviewResponse> reviews = reviewService.getReviewResponsesOfLecture(lectureId, page);
        return ResponseEntity.ok(reviews);
    }

    @ApiOperation("등록 강의별 리뷰 개별 조회")
    @GetMapping("/{lecture_id}/reviews/{review_id}")
    public ResponseEntity<?> getReviewOfLecture(@PathVariable(name = "lecture_id") Long lectureId,
                                                @PathVariable(name = "review_id") Long reviewId) {

        ReviewResponse review = reviewService.getReviewResponseOfLecture(lectureId, reviewId);
        return ResponseEntity.ok(review);
    }

    @ApiOperation("튜터 리뷰 작성")
    @PostMapping("/{lecture_id}/reviews/{parent_id}")
    public ResponseEntity<?> newReview(@CurrentUser User user,
                                       @PathVariable(name = "lecture_id") Long lectureId,
                                       @PathVariable(name = "parent_id") Long parentId,
                                       @RequestBody @Valid TutorReviewCreateRequest tutorReviewCreateRequest) {

        reviewService.createTutorReview(user, lectureId, parentId, tutorReviewCreateRequest);
        return created();
    }

    @ApiOperation("튜터 리뷰 수정")
    @PutMapping("/{lecture_id}/reviews/{parent_id}/children/{review_id}")
    public ResponseEntity<?> editReview(@CurrentUser User user,
                                        @PathVariable(name = "lecture_id") Long lectureId,
                                        @PathVariable(name = "parent_id") Long parentId,
                                        @PathVariable(name = "review_id") Long reviewId,
                                        @RequestBody @Valid TutorReviewUpdateRequest tutorReviewUpdateRequest) {

        reviewService.updateTutorReview(user, lectureId, parentId, reviewId, tutorReviewUpdateRequest);
        return ok();
    }

    @ApiOperation("튜터 리뷰 삭제")
    @DeleteMapping("/{lecture_id}/reviews/{parent_id}/children/{review_id}")
    public ResponseEntity<?> deleteReview(@CurrentUser User user,
                                          @PathVariable(name = "lecture_id") Long lectureId,
                                          @PathVariable(name = "parent_id") Long parentId,
                                          @PathVariable(name = "review_id") Long reviewId) {

        reviewService.deleteTutorReview(user, lectureId, parentId, reviewId);
        return ok();
    }

    @ApiOperation("등록 강의별 튜티 조회 - 페이징")
    @GetMapping("/{lecture_id}/tutees")
    public ResponseEntity<?> getTuteesOfLecture(@CurrentUser User user,
                                                @PathVariable(name = "lecture_id") Long lectureId,
                                                @RequestParam(defaultValue = "1") Integer page) {

        Page<TuteeResponse> tutees = tutorLectureService.getTuteeResponsesOfLecture(user, lectureId, page);
        return ResponseEntity.ok(tutees);
    }

    @ApiOperation("등록 강의별 수강내역 조회 - 페이징")
    @GetMapping("/{lecture_id}/enrollments")
    public ResponseEntity<?> getEnrollmentsOfLecture(@CurrentUser User user,
                                                     @PathVariable(name = "lecture_id") Long lectureId,
                                                     @RequestParam(defaultValue = "1") Integer page) {

        Page<EnrollmentResponse> enrollments = tutorLectureService.getEnrollmentResponsesOfLecture(user, lectureId, page);
        return ResponseEntity.ok(enrollments);
    }

    // 튜티가 강의 종료
//    @ApiOperation("강의 종료")
//    @PutMapping("/{lecture_id}/enrollments/{enrollment_id}")
//    public ResponseEntity<?> close(@CurrentUser User user,
//                                   @PathVariable(name = "lecture_id") Long lectureId,
//                                   @PathVariable(name = "enrollment_id") Long enrollmentId) {
//        enrollmentService.close(user, lectureId, enrollmentId);
//        return ResponseEntity.ok().build();
//    }
}
