package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.CareerResponse;
import com.tutor.tutorlab.modules.account.service.CareerService;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"CareerController"})
@RequestMapping("/careers")
@RequiredArgsConstructor
@RestController
public class CareerController {

    private final CareerService careerService;

    // TODO - CHECK : user
    @ApiOperation("Career 조회")
    @GetMapping("/{career_id}")
    public ResponseEntity<?> getCareer(@CurrentUser User user,
                                       @PathVariable(name = "career_id") Long careerId) {
        Career career = careerService.getCareer(user, careerId);
        return ResponseEntity.ok(new CareerResponse(career));
    }

    @ApiOperation("Career 등록")
    @PostMapping
    public ResponseEntity<?> newCareer(@CurrentUser User user,
                                       @Valid @RequestBody CareerCreateRequest careerCreateRequest) {
        careerService.createCareer(user, careerCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("Career 수정")
    @PutMapping("/{career_id}")
    public ResponseEntity<?> editCareer(@CurrentUser User user,
                                        @PathVariable(name = "career_id") Long careerId,
                                        @Valid @RequestBody CareerUpdateRequest careerUpdateRequest) {
        careerService.updateCareer(user, careerId, careerUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Career 삭제")
    @DeleteMapping("/{career_id}")
    public ResponseEntity<?> deleteCareer(@CurrentUser User user,
                                          @PathVariable(name = "career_id") Long careerId) {
        careerService.deleteCareer(user, careerId);
        return ResponseEntity.ok().build();
    }

}
