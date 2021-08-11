package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.response.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.response.exception.UnauthorizedException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.service.EducationService;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"EducationController"})
@RequestMapping("/educations")
@RequiredArgsConstructor
@RestController
public class EducationController extends AbstractController {

    private final EducationRepository educationRepository;
    private final EducationService educationService;
    private final TutorRepository tutorRepository;

    /**
     * Education 리스트
     */
    @ApiOperation("튜터의 Education 리스트")
    @GetMapping("/tutor/{tutor_id}")
    public ResponseEntity getEducations(@PathVariable(name = "tutor_id") Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 튜터입니다."));
        List<EducationDto> educations = educationRepository.findByTutor(tutor).stream()
                .map(education -> new EducationDto(education))
                .collect(Collectors.toList());

        return new ResponseEntity(educations, HttpStatus.OK);
    }

    /**
     * Education 조회
     */
    @ApiOperation("Education 조회")
    @GetMapping("/{education_id}")
    public ResponseEntity getEducation( // @PathVariable(name = "tutor_id") Long tutorId,
                                            @PathVariable(name = "education_id") Long educationId) {

        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 데이터입니다."));

        return new ResponseEntity(new EducationDto(education), HttpStatus.OK);
    }

    /**
     * Education 등록
     */
    @ApiOperation("Education 등록")
    @PostMapping
    public ResponseEntity newEducation(@CurrentUser User user,
                                       @RequestBody EducationCreateRequest educationCreateRequest) {

        if (user == null) {
            throw new UnauthorizedException();
        }
        educationService.createEducation(user, educationCreateRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Education 수정
     */
    @ApiOperation("Education 수정")
    @PutMapping("/{education_id}")
    public ResponseEntity editEducation(@CurrentUser User user,
                                        @PathVariable(name = "education_id") Long educationId,
                                        @RequestBody EducationUpdateRequest educationUpdateRequest) {

        if (user == null) {
            throw new UnauthorizedException();
        }
        educationService.updateEducation(educationId, educationUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Education 삭제
     */
    @ApiOperation("Education 삭제")
    @DeleteMapping("/{education_id}")
    public ResponseEntity removeEducation(@CurrentUser User user,
                                          @PathVariable(name = "education_id") Long educationId) {

        if (user == null) {
            throw new UnauthorizedException();
        }
        educationService.deleteEducation(educationId);
        return new ResponseEntity(HttpStatus.OK);
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
