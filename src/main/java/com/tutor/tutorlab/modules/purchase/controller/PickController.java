package com.tutor.tutorlab.modules.purchase.controller;

import com.tutor.tutorlab.config.response.Response;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.service.PickService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tutor.tutorlab.config.response.Response.created;

@Api(tags = {"PickController"})
@RestController
@RequiredArgsConstructor
public class PickController {

    private final PickService pickService;

    @ApiOperation("좋아요")
    //@ApiOperation("장바구니 추가")
    @PostMapping("/api/lectures/{lecture_id}/picks")
    public ResponseEntity<?> addPick(@CurrentUser User user,
                                     @PathVariable(name = "lecture_id") Long lectureId) {
        pickService.createPick(user, lectureId);
        return created();
    }

}
