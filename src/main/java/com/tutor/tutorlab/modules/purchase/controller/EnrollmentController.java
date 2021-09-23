package com.tutor.tutorlab.modules.purchase.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @ApiOperation("강의 수강")
    @PostMapping("/lectures/{lecture_id}/enrollments")
    public ResponseEntity<?> enroll(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId) {
        enrollmentService.enroll(user, lectureId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
