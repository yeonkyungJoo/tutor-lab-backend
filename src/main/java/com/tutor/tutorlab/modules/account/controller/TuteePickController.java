package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.response.PickResponse;
import com.tutor.tutorlab.modules.purchase.service.PickService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"TuteePickController"})
@RequestMapping("/tutees/my-picks")
@RestController
@RequiredArgsConstructor
public class TuteePickController {

    private final PickService pickService;

    @GetMapping
    public ResponseEntity<?> getPicks(@CurrentUser User user,
                                      @RequestParam(defaultValue = "1") Integer page) {
        Page<PickResponse> picks = pickService.getPickResponses(user, page);
        return ResponseEntity.ok(picks);
    }

    @DeleteMapping("/{pick_id}")
    public ResponseEntity<?> subtractPick(@CurrentUser User user,
                                          @PathVariable(name = "pick_id") Long pickId) {
        pickService.deletePick(user, pickId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> clear(@CurrentUser User user) {

        pickService.deleteAllPicks(user);
        return ResponseEntity.ok().build();
    }
}
