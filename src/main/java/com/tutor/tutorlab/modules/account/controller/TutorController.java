package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.response.Response;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.CareerResponse;
import com.tutor.tutorlab.modules.account.controller.response.EducationResponse;
import com.tutor.tutorlab.modules.account.controller.response.TutorResponse;
import com.tutor.tutorlab.modules.account.service.TutorLectureService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.tutor.tutorlab.config.response.Response.created;
import static com.tutor.tutorlab.config.response.Response.ok;

@Api(tags = {"TutorController"})
@RequestMapping("/api/tutors")
@RestController
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;
    private final TutorLectureService tutorLectureService;

    // TODO - 검색
    @ApiOperation("튜터 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getTutors(@RequestParam(defaultValue = "1") Integer page) {

        Page<TutorResponse> tutors = tutorService.getTutorResponses(page);
        return ResponseEntity.ok(tutors);
    }

    @ApiOperation("내 튜터 정보 조회")
    @GetMapping("/my-info")
    public ResponseEntity<?> getMyInfo(@CurrentUser User user) {
        return ResponseEntity.ok(tutorService.getTutorResponse(user));
    }

    @ApiOperation("튜터 조회")
    @GetMapping("/{tutor_id}")
    public ResponseEntity<?> getTutor(@PathVariable(name = "tutor_id") Long tutorId) {

        TutorResponse tutor = tutorService.getTutorResponse(tutorId);
        return ResponseEntity.ok(tutor);
    }

    @ApiOperation("튜터 등록")
    @PostMapping
    public ResponseEntity<?> newTutor(@CurrentUser User user,
                                      @Valid @RequestBody TutorSignUpRequest tutorSignUpRequest) {

        tutorService.createTutor(user, tutorSignUpRequest);
        return created();
    }

    @ApiOperation("튜터 정보 수정")
    @PutMapping("/my-info")
    public ResponseEntity<?> editTutor(@CurrentUser User user,
                                       @Valid @RequestBody TutorUpdateRequest tutorUpdateRequest) {

        tutorService.updateTutor(user, tutorUpdateRequest);
        return ok();
    }

    @ApiOperation("튜터 탈퇴")
    @DeleteMapping
    public ResponseEntity<?> quitTutor(@CurrentUser User user) {

        tutorService.deleteTutor(user);
        return ok();
    }

    @ApiOperation("튜터의 Career 리스트")
    @GetMapping("/{tutor_id}/careers")
    public ResponseEntity<?> getCareers(@PathVariable(name = "tutor_id") Long tutorId) {

        List<CareerResponse> careers = tutorService.getCareerResponses(tutorId);
        return ResponseEntity.ok(careers);
    }

    @ApiOperation("튜터의 Education 리스트")
    @GetMapping("/{tutor_id}/educations")
    public ResponseEntity<?> getEducations(@PathVariable(name = "tutor_id") Long tutorId) {

        List<EducationResponse> educations = tutorService.getEducationResponses(tutorId);
        return ResponseEntity.ok(educations);
    }

    @ApiOperation("튜터의 강의 리스트")
    @GetMapping("/{tutor_id}/lectures")
    public ResponseEntity<?> getLectures(@PathVariable(name = "tutor_id") Long tutorId,
                                         @RequestParam(defaultValue = "1") Integer page) {

        Page<LectureResponse> lectures = tutorLectureService.getLectureResponses(tutorId, page);
        return ResponseEntity.ok(lectures);
    }

}
