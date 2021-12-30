package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.EducationResponse;
import com.tutor.tutorlab.modules.account.service.EducationService;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"EducationController"})
@RequestMapping("/educations")
@RequiredArgsConstructor
@RestController
public class EducationController {

    private final EducationService educationService;

    @ApiOperation("Education 조회")
    @GetMapping("/{education_id}")
    public ResponseEntity<?> getEducation(@CurrentUser User user,
                                       @PathVariable(name = "education_id") Long educationId) {
        EducationResponse education = educationService.getEducationResponse(user, educationId);
        return ResponseEntity.ok(education);
    }

    @ApiOperation("Education 등록")
    @PostMapping
    public ResponseEntity<?> newEducation(@CurrentUser User user,
                                       @Valid @RequestBody EducationCreateRequest educationCreateRequest) {
        educationService.createEducation(user, educationCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("Education 수정")
    @PutMapping("/{education_id}")
    public ResponseEntity<?> editEducation(@CurrentUser User user,
                                        @PathVariable(name = "education_id") Long educationId,
                                        @Valid @RequestBody EducationUpdateRequest educationUpdateRequest) {
        educationService.updateEducation(user, educationId, educationUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Education 삭제")
    @DeleteMapping("/{education_id}")
    public ResponseEntity<?> deleteEducation(@CurrentUser User user,
                                          @PathVariable(name = "education_id") Long educationId) {
        educationService.deleteEducation(user, educationId);
        return ResponseEntity.ok().build();
    }

}
