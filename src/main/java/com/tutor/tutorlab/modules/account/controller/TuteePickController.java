package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.response.PickResponse;
import com.tutor.tutorlab.modules.purchase.service.PickService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tutees/my-picks")
@RestController
@RequiredArgsConstructor
public class TuteePickController {

    private final PickService pickService;

    @ApiOperation("장바구니 조회 - 페이징")
    @GetMapping
    public ResponseEntity getPicks(@CurrentUser User user,
                                   @RequestParam(defaultValue = "1") Integer page) {

        Page<PickResponse> picks = pickService.getPicks(user, page).map(PickResponse::new);
        return ResponseEntity.ok(picks);
    }

    @ApiOperation("장바구니 제거")
    @DeleteMapping("/{pick_id}")
    public ResponseEntity<?> subtractPick(@CurrentUser User user,
                                          @RequestParam(name = "pick_id") Long pickId) {
        pickService.subtract(user, pickId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("장바구니 비우기")
    @DeleteMapping
    public ResponseEntity<?> clear(@CurrentUser User user) {

        pickService.clear(user);
        return ResponseEntity.ok().build();
    }
}
