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
@RequestMapping("/picks")
public class PickController {

    private final TuteeRepository tuteeRepository;
    private final PickService pickService;

    @ApiOperation("장바구니 추가")
    @PostMapping("/{lecture_id}")
    public ResponseEntity addPick(@CurrentUser User user,
                                  @RequestParam(name = "lecture_id") Long lectureId) {

        // TODO - AuthAspect or Interceptor로 처리
        Tutee tutee = tuteeRepository.findByUser(user);
        pickService.add(tutee, lectureId);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ApiOperation("장바구니 제거")
    @DeleteMapping("/{pick_id}")
    public ResponseEntity subtractPick(@CurrentUser User user,
                                       @RequestParam(name = "pick_id") Long pickId) {

        Tutee tutee = tuteeRepository.findByUser(user);
        pickService.subtract(tutee, pickId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("장바구니 비우기")
    public ResponseEntity clear(@CurrentUser User user) {

        Tutee tutee = tuteeRepository.findByUser(user);
        pickService.clear(tutee);
        return new ResponseEntity(HttpStatus.OK);
    }
}
