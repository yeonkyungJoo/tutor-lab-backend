package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.modules.account.career.Career;
import com.tutor.tutorlab.modules.account.education.Education;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/tutor")
@RestController
@RequiredArgsConstructor
public class TutorController {

    private final Integer PAGE_SIZE = 20;

    private final TutorService tutorService;
    private final TutorRepository tutorRepository;

    /**
     * 튜터 전체 조회
     */
    @ApiOperation("튜터 전체 조회")
    @RequestMapping("/")
    public ResponseEntity getTutors() {

        List<TutorDto> tutors = tutorRepository.findAll().stream()
                .map(tutor -> new TutorDto(tutor)).collect(Collectors.toList());

        // TODO - RestResponse
        return new ResponseEntity(tutors, HttpStatus.OK);
    }

    // TODO - 검색
    /**
     * 튜터 전체 조회 - 페이징
     */
    @ApiOperation("튜터 전체 조회 - 페이징")
    @RequestMapping("/list")
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
    @GetMapping("/{id}")
    public ResponseEntity getTutor(@PathVariable(name = "id") Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 튜터입니다."));
        return new ResponseEntity(new TutorDto(tutor), HttpStatus.OK);
    }

    /**
     * 튜터 등록
     */
    @ApiOperation("튜터 등록")
    @PostMapping("/new")
    public ResponseEntity newTutor() {
        return null;
    }

    /**
     * 튜터 정보 수정
     */
    @ApiOperation("튜터 정보 수정")
    @PutMapping("/edit")
    public ResponseEntity editTutor() {
        return null;
    }

    /**
     * 튜터 탈퇴
     */
    @ApiOperation("튜터 탈퇴")
    @DeleteMapping("/quit")
    public ResponseEntity quitTutor() {
        return null;
    }

    @Data
    static class TutorDto {

        private UserController.UserDto user;
        private String subject;
        private List<CareerDto> careers;
        private List<EducationDto> educations;
        private boolean specialist;

        public TutorDto(Tutor tutor) {
            this.user = new UserController.UserDto(tutor.getUser());
            this.subject = tutor.getSubject();
            this.careers = tutor.getCareers().stream()
                    // TODO - CHECK : static
                    .map(career -> new TutorController.CareerDto(career)).collect(Collectors.toList());
            this.educations = tutor.getEducations().stream()
                    .map(education -> new TutorController.EducationDto(education)).collect(Collectors.toList());
            this.specialist = tutor.isSpecialist();
        }

    }

    @Data
    static class CareerDto {

        private String companyName;
        private String duty;
        private String startDate;
        private String endDate;
        private boolean present;

        public CareerDto(Career career) {
            this.companyName = career.getCompanyName();
            this.duty = career.getDuty();
            this.startDate = LocalDateTimeUtil.getDateToString(career.getStartDate());
            this.endDate = LocalDateTimeUtil.getDateToString(career.getEndDate());
            this.present = career.isPresent();
        }
    }

    @Data
    static class EducationDto {

        private String schoolName;
        private String major;
        private String entranceDate;
        private String graduationDate;
        private double score;
        private String degree;

        public EducationDto(Education education) {
            this.schoolName = education.getSchoolName();
            this.major = education.getMajor();
            this.entranceDate = LocalDateTimeUtil.getDateToString(education.getEntranceDate());
            this.graduationDate = LocalDateTimeUtil.getDateToString(education.getGraduationDate());
            this.score = education.getScore();
            this.degree = education.getDegree();
        }
    }
}
