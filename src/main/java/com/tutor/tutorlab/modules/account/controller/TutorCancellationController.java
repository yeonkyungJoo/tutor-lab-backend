package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.service.TutorCancellationService;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.vo.User;
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
// TODO
    private final TutorCancellationService tutorCancellationService;

    @ApiOperation("환불 목록 조회 - 페이징")
    public ResponseEntity<?> getMyTutees(@CurrentUser User user,
                                         @RequestParam(name = "page", defaultValue = "1") Integer page) {
        return null;
    }


}
