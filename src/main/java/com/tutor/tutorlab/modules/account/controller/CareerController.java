package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.service.CareerService;
import com.tutor.tutorlab.modules.account.vo.Career;
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

@Api(tags = {"CareerController"})
@RequestMapping("/careers")
@RequiredArgsConstructor
@RestController
public class CareerController extends AbstractController {

    private final CareerRepository careerRepository;
    private final CareerService careerService;

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
                                    @Valid @RequestBody CareerCreateRequest careerCreateRequest) {
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
                                     @Valid @RequestBody CareerUpdateRequest careerUpdateRequest) {

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
