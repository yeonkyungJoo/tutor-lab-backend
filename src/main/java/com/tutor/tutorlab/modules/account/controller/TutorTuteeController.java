package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.response.TuteeLectureResponse;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.service.TutorTuteeService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"TutorTuteeController"})
@RequestMapping("/tutors/my-tutees")
@RestController
@RequiredArgsConstructor
public class TutorTuteeController {

    private final TutorTuteeService tutorTuteeService;
    private final ReviewService reviewService;

    // 튜티 전체, 강의 진행중인 튜티, 강의 종료된 튜티
    @ApiOperation("튜티 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getMyTutees(@CurrentUser User user,
                                         @RequestParam(name = "closed", required = false) Boolean closed,
                                         @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Page<TuteeSimpleResponse> tutees = tutorTuteeService.getTuteeSimpleResponses(user, closed, page);
        return ResponseEntity.ok(tutees);
    }

    // TODO - TuteeSimpleResponse에 LectureId를 함께 전달
    @ApiOperation("튜티-강의 조회 - 페이징")
    @GetMapping("/{tutee_id}")
    public ResponseEntity<?> getMyTutee(@CurrentUser User user,
                                        @RequestParam(name = "closed", required = false) Boolean closed,
                                        @PathVariable(name = "tutee_id") Long tuteeId,
                                        @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Page<TuteeLectureResponse> tuteeLectures = tutorTuteeService.getTuteeLectureResponses(user, closed, tuteeId, page);
        return ResponseEntity.ok(tuteeLectures);
    }

    @ApiOperation("튜티 리뷰 조회")
    @GetMapping("/{tutee_id}/lectures/{lecture_id}/reviews/{review_id}")
    public ResponseEntity<?> getReviewsOfMyTutee(@CurrentUser User user,
                                                 @PathVariable(name = "tutee_id") Long tuteeId,
                                                 @PathVariable(name = "lecture_id") Long lectureId,
                                                 @PathVariable(name = "review_id") Long reviewId) {
        ReviewResponse review = reviewService.getReviewResponseOfLecture(tuteeId, lectureId, reviewId);
        return ResponseEntity.ok(review);
    }

}
