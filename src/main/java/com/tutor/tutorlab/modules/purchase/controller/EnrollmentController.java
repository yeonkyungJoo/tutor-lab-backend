package com.tutor.tutorlab.modules.purchase.controller;

import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
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
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final TutorRepository tutorRepository;
    private final TuteeRepository tuteeRepository;
    private final EnrollmentService enrollmentService;

    @ApiOperation("강의 수강")
    @PostMapping
    public ResponseEntity enroll(@CurrentUser User user,
                                 @Valid @RequestBody EnrollmentRequest enrollmentRequest) {

        Tutee tutee = tuteeRepository.findByUser(user);
        enrollmentService.enroll(tutee, enrollmentRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("강의 수강 취소")
    @DeleteMapping("/lectures/{lecture_id}")
    public ResponseEntity cancel(@CurrentUser User user,
                                @PathVariable(name = "lecture_id") Long lectureId) {

        Tutee tutee = tuteeRepository.findByUser(user);
        enrollmentService.cancel(tutee, lectureId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // TODO - CHECK : 보완 필요
    @ApiOperation("강의 종료")
    @PutMapping("/{enrollment_id}")
    public ResponseEntity close(@CurrentUser User user,
                                @PathVariable(name = "enrollment_id") Long enrollmentId) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }
        enrollmentService.close(tutor, enrollmentId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
