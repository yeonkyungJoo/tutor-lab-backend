package com.tutor.tutorlab.modules.purchase.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.service.PickService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PickController {

    private final PickService pickService;

    @ApiOperation("장바구니 추가")
    @PostMapping("/lectures/{lecture_id}/picks")
    public ResponseEntity<?> addPick(@CurrentUser User user,
                                     @RequestParam(name = "lecture_id") Long lectureId) {
        pickService.add(user, lectureId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
