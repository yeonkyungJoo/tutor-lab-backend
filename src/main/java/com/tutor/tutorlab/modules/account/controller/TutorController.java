package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstructUtil;
import com.tutor.tutorlab.modules.lecture.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
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

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"TutorController"})
@RequestMapping("/tutors")
@RestController
@RequiredArgsConstructor
public class TutorController extends AbstractController {

    private final TutorService tutorService;
    private final TutorRepository tutorRepository;

    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LectureMapstructUtil lectureMapstructUtil;

/*
    @ApiOperation("튜터 전체 조회")
    @GetMapping
    public ResponseEntity getTutors() {

        List<TutorDto> tutors = tutorRepository.findAll().stream()
                .map(tutor -> new TutorDto(tutor)).collect(Collectors.toList());
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
     * 튜터 등록
     */
    @ApiOperation("튜터 등록")
    @PostMapping
    public ResponseEntity newTutor(@CurrentUser User user,
                                @Valid @RequestBody TutorSignUpRequest tutorSignUpRequest) {

        tutorService.createTutor(user, tutorSignUpRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 튜터 정보 수정
     */
    @ApiOperation("튜터 정보 수정")
    @PutMapping
    public ResponseEntity editTutor(@CurrentUser User user,
                                    @Valid @RequestBody TutorUpdateRequest tutorUpdateRequest) {

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

    @ApiOperation("등록 강의 전체 조회 - 페이징")
    @GetMapping("/my-lectures")
    public ResponseEntity getLectures(@CurrentUser User user,
                                      @RequestParam(defaultValue = "1") Integer page) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        Page<LectureResponse> lectures = lectureRepository.findByTutor(tutor,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(lecture -> lectureMapstructUtil.getLectureResponse(lecture));
        return new ResponseEntity(lectures, HttpStatus.OK);
    }

    @ApiOperation("등록 강의 개별 조회")
    @GetMapping("/my-lectures/{lecture_id}")
    public ResponseEntity getLecture(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }
        // TODO - CHECK : lectureService의 getLecture와 동일
        // TODO - 해당 Tutor의 강의인지 체크
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));
        LectureResponse lectureResponse = lectureMapstructUtil.getLectureResponse(lecture);
        return new ResponseEntity(lectureResponse, HttpStatus.OK);
    }

    @ApiOperation("등록 강의별 튜티 조회 - 페이징")
    @GetMapping("/my-lectures/{lecture_id}/tutees")
    public ResponseEntity getTuteesOfLecture(@CurrentUser User user,
                                             @PathVariable(name = "lecture_id") Long lectureId,
                                             @RequestParam(defaultValue = "1") Integer page) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));

        Page<TuteeController.TuteeDto> tutees = enrollmentRepository.findByLecture(lecture,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(enrollment -> new TuteeController.TuteeDto(enrollment.getTutee()));
        return new ResponseEntity(tutees, HttpStatus.OK);
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
