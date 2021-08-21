package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.EnrollmentRequest;
import com.tutor.tutorlab.modules.lecture.service.EnrollmentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lecture/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @ApiOperation("강의 수강")
    @PostMapping
    public ResponseEntity enroll(@CurrentUser User user,
                                 @RequestBody EnrollmentRequest enrollmentRequest) {


        return null;
    }

    @ApiOperation("강의 수강 취소")
    @DeleteMapping("/{lecture_id}")
    public ResponseEntity cancel(@CurrentUser User user,
                                @PathVariable(name = "lecture_id") Long lectureId) {
        return null;
    }

//    // TODO - 위치 변경
//    // tutees/{tutee_id}/lectures
//    // 튜터별 수강 강의 조회
//    @ApiOperation("튜터별 수강 강의 조회")
//    @GetMapping
//    public ResponseEntity getLectures(@CurrentUser User user,
//                              @PathVariable(name = "tutee_id") Long tuteeId) {
//        return null;
//    }
//
//    // TODO - 위치 변경
//    // lectures/{lecture_id}/tutees
//    // 강의별 수강 튜티 조회
//    @ApiOperation("강의별 수강 튜티 조회")
//    @GetMapping
//    public ResponseEntity getTutees(@CurrentUser User user,
//                            @PathVariable(name = "lecture_id") Long lectureId) {
//        return null;
//    }

}
