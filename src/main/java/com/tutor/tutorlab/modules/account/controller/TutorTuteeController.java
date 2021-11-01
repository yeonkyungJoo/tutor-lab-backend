package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.vo.User;
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

    private final TutorLectureService tutorLectureService;

    // 튜티 전체, 강의 진행중인 튜티, 강의 종료된 튜티
    @ApiOperation("튜티 전체 조회 - 페이징")
    public ResponseEntity<?> getMyTutees(@CurrentUser User user,
                                         @RequestParam(name = "closed", required = false) Boolean closed,
                                         @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Page<TuteeSimpleResponse> tutees = tutorLectureService.getTuteeSimpleResponses(user, closed, page);
        return ResponseEntity.ok(tutees);
    }

    // TODO - TuteeSimpleResponse에 LectureId를 함께 전달
    @ApiOperation("튜티-강의 조회")
    @GetMapping("/{tutee_id}")
    public ResponseEntity<?> getMyTutee(@CurrentUser User user,
                                        @PathVariable(name = "tutee_id") Long tuteeId) {
        return null;
    }

    @ApiOperation("튜티 리뷰 조회")
    @GetMapping("/{tutee_id}/reviews")
    public ResponseEntity<?> getReviewsOfMyTutee(@CurrentUser User user,
                                        @PathVariable(name = "tutee_id") Long tuteeId) {
        return null;
    }

}
