package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.response.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.response.exception.UnauthorizedException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"TutorController"})
@RequestMapping("/tutors")
@RestController
@RequiredArgsConstructor
public class TutorController extends AbstractController {

    private final TutorService tutorService;
    private final TutorRepository tutorRepository;

/*
    @ApiOperation("튜터 전체 조회")
    @GetMapping
    public ResponseEntity getTutors() {

        List<TutorDto> tutors = tutorRepository.findAll().stream()
                .map(tutor -> new TutorDto(tutor)).collect(Collectors.toList());

        // TODO - RestResponse
        return new ResponseEntity(tutors, HttpStatus.OK);
    }
*/

    // TODO - 검색
    /**
     * 튜터 전체 조회 - 페이징
     */
    @ApiOperation("튜터 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity getTutors(@RequestParam(defaultValue = "1") Integer page) {

        Page<TutorDto> tutors = tutorRepository.findAll(
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(tutor -> new TutorDto(tutor));

        // TODO - RestResponse
        return new ResponseEntity(tutors, HttpStatus.OK);
    }

    /**
     * 튜터 조회
     */
    @ApiOperation("튜터 조회")
    @GetMapping("/{tutor_id}")
    public ResponseEntity getTutor(@PathVariable(name = "tutor_id") Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 튜터입니다."));
        return new ResponseEntity(new TutorDto(tutor), HttpStatus.OK);
    }
    /**
     * 튜터가 등록한 강의조회
     */
    @ApiOperation("튜터 강의조회")
    @GetMapping("/mylectures")
    public ResponseEntity getTutorLecture(@CurrentUser User user) {

//        Tutor tutor = tutorRepository.findById(tutorId)
//                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 튜터입니다."));
        List<LectureResponse> lectures = tutorService.getTutorLecture(user).stream()
                .map(lecture -> new LectureResponse(lecture))
                .collect(Collectors.toList());

        return new ResponseEntity(lectures, HttpStatus.OK);
    }

    /**
     * 튜터 등록
     */
    @ApiOperation("튜터 등록")
    @PostMapping
    public ResponseEntity newTutor(@CurrentUser User user,
                        @RequestBody TutorSignUpRequest tutorSignUpRequest) {

        tutorService.createTutor(user, tutorSignUpRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 튜터 정보 수정
     */
    @ApiOperation("튜터 정보 수정")
    @PutMapping
    public ResponseEntity editTutor(@CurrentUser User user,
                                    @RequestBody TutorUpdateRequest tutorUpdateRequest) {

        tutorService.updateTutor(user, tutorUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 튜터 탈퇴
     */
    @ApiOperation("튜터 탈퇴")
    @DeleteMapping
    public ResponseEntity quitTutor(@CurrentUser User user) {

        tutorService.deleteTutor(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    static class TutorDto {

        private UserController.UserDto user;
        private String subjects;
        private List<CareerController.CareerDto> careers;
        private List<EducationController.EducationDto> educations;
        private boolean specialist;

        public TutorDto(Tutor tutor) {
            this.user = new UserController.UserDto(tutor.getUser());
            this.subjects = tutor.getSubjects();
            this.careers = tutor.getCareers().stream()
                    // TODO - CHECK : static
                    .map(career -> new CareerController.CareerDto(career)).collect(Collectors.toList());
            this.educations = tutor.getEducations().stream()
                    .map(education -> new EducationController.EducationDto(education)).collect(Collectors.toList());
            this.specialist = tutor.isSpecialist();
        }

    }


}
