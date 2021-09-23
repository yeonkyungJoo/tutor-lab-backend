package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.CareerResponse;
import com.tutor.tutorlab.modules.account.controller.response.EducationResponse;
import com.tutor.tutorlab.modules.account.controller.response.TutorResponse;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"TutorController"})
@RequestMapping("/tutors")
@RestController
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    // TODO - 검색
    @ApiOperation("튜터 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getTutors(@RequestParam(defaultValue = "1") Integer page) {

        Page<TutorResponse> tutors = tutorService.getTutors(page).map(TutorResponse::new);
        return ResponseEntity.ok(tutors);
    }

    @ApiOperation("튜터 조회")
    @GetMapping("/{tutor_id}")
    public ResponseEntity<?> getTutor(@PathVariable(name = "tutor_id") Long tutorId) {

        Tutor tutor = tutorService.getTutor(tutorId);
        return ResponseEntity.ok(new TutorResponse(tutor));
    }

    @ApiOperation("튜터 등록")
    @PostMapping
    public ResponseEntity<?> newTutor(@CurrentUser User user,
                                      @Valid @RequestBody TutorSignUpRequest tutorSignUpRequest) {

        tutorService.createTutor(user, tutorSignUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation("튜터 정보 수정")
    @PutMapping
    public ResponseEntity<?> editTutor(@CurrentUser User user,
                                    @Valid @RequestBody TutorUpdateRequest tutorUpdateRequest) {

        tutorService.updateTutor(user, tutorUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("튜터 탈퇴")
    @DeleteMapping
    public ResponseEntity<?> quitTutor(@CurrentUser User user) {

        tutorService.deleteTutor(user);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("튜터의 Career 리스트")
    @GetMapping("/{tutor_id}/careers")
    public ResponseEntity<?> getCareers(@PathVariable(name = "tutor_id") Long tutorId) {

        List<CareerResponse> careers = tutorService.getCareers(tutorId).stream()
                .map(CareerResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(careers);
    }

    @ApiOperation("튜터의 Education 리스트")
    @GetMapping("/{tutor_id}/educations")
    public ResponseEntity<?> getEducations(@PathVariable(name = "tutor_id") Long tutorId) {

        List<EducationResponse> educations = tutorService.getEducations(tutorId).stream()
                .map(EducationResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(educations);
    }

}
