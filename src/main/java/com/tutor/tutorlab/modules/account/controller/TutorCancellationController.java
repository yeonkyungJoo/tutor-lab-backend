package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.service.TutorCancellationService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.response.CancellationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"TutorCancellationController"})
@RequestMapping("/tutors/my-cancellations")
@RestController
@RequiredArgsConstructor
public class TutorCancellationController {
// TODO - test
    private final TutorCancellationService tutorCancellationService;

    @ApiOperation("환불 목록 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getMyCancellations(@CurrentUser User user,
                                                @RequestParam(name = "page", defaultValue = "1") Integer page) {
        Page<CancellationResponse> cancellations = tutorCancellationService.getCancellationResponses(user, page);
        return ResponseEntity.ok(cancellations);
    }

    @ApiOperation("환불 승인")
    @PutMapping("/{cancellation_id}")
    public ResponseEntity<?> approveCancellation(@CurrentUser User user,
                                                 @PathVariable(name = "cancellation_id") Long cancellationId) {
        tutorCancellationService.approve(user, cancellationId);
        return ResponseEntity.ok().build();
    }




}
