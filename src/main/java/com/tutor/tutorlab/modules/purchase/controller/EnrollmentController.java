package com.tutor.tutorlab.modules.purchase.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.request.EnrollmentRequest;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/lecture/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @ApiOperation("강의 수강")
    @PostMapping
    public ResponseEntity enroll(@CurrentUser User user,
                                 @Valid @RequestBody EnrollmentRequest enrollmentRequest) {

        enrollmentService.enroll(user, enrollmentRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("강의 수강 취소")
    @DeleteMapping("/{lecture_id}")
    public ResponseEntity cancel(@CurrentUser User user,
                                @PathVariable(name = "lecture_id") Long lectureId) {
        enrollmentService.cancel(user, lectureId);
        return null;
    }

}
