package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.response.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.response.exception.UnauthorizedException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.service.CareerService;
import com.tutor.tutorlab.modules.account.vo.Career;
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
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"CareerController"})
@RequestMapping("/careers")
@RequiredArgsConstructor
@RestController
public class CareerController extends AbstractController {

    private final CareerRepository careerRepository;
    private final CareerService careerService;
    private final TutorRepository tutorRepository;

    /**
     * Career 리스트
     */
    @ApiOperation("튜터의 Career 리스트")
    @GetMapping("/tutor/{tutor_id}")
    public ResponseEntity getCareers(@PathVariable(name = "tutor_id") Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 튜터입니다."));
        List<CareerDto> careers = careerRepository.findByTutor(tutor).stream()
                .map(career -> new CareerDto(career))
                .collect(Collectors.toList());

        return new ResponseEntity(careers, HttpStatus.OK);
    }

    /**
     * Career 조회
     */
    @ApiOperation("Career 조회")
    @GetMapping("/{career_id}")
    public ResponseEntity getCareer(@PathVariable(name = "career_id") Long careerId) {

        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 데이터입니다."));
        return new ResponseEntity(new CareerDto(career), HttpStatus.OK);
    }

    /**
     * Career 등록
     */
    @ApiOperation("Career 등록")
    @PostMapping
    public ResponseEntity newCareer(@CurrentUser User user,
                                    @RequestBody CareerCreateRequest careerCreateRequest) {
        if (user == null) {
            throw new UnauthorizedException();
        }
        careerService.createCareer(user, careerCreateRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * Career 수정
     */
    @ApiOperation("Career 수정")
    @PutMapping("/{career_id}")
    public ResponseEntity editCareer(@CurrentUser User user,
                                     @PathVariable(name = "career_id") Long careerId,
                                     @RequestBody CareerUpdateRequest careerUpdateRequest) {

        if (user == null) {
            throw new UnauthorizedException();
        }
        careerService.updateCareer(careerId, careerUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Career 삭제
     */
    @ApiOperation("Career 삭제")
    @DeleteMapping("/{career_id}")
    public ResponseEntity removeCareer(@CurrentUser User user,
                                       @PathVariable(name = "career_id") Long careerId) {

        if (user == null) {
            throw new UnauthorizedException();
        }
        careerService.deleteCareer(careerId);
        return new ResponseEntity(HttpStatus.OK);
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
}
