package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.service.TuteeService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.repository.MessageRepository;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstructUtil;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"TuteeController"})
@RequestMapping("/tutees")
@RestController
@RequiredArgsConstructor
public class TuteeController {

    private final TuteeService tuteeService;

    // TODO - 검색
    @ApiOperation("튜티 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getTutees(@RequestParam(defaultValue = "1") Integer page) {

        Page<TuteeResponse> tutees = tuteeService.getTutees(page).map(TuteeResponse::new);
        return ResponseEntity.ok(tutees);
    }

    @ApiOperation("튜티 조회")
    @GetMapping("/{tutee_id}")
    public ResponseEntity<?> getTutee(@PathVariable(name = "tutee_id") Long tuteeId) {

        Tutee tutee = tuteeService.getTutee(tuteeId);
        return ResponseEntity.ok(tutee);
    }

    @ApiOperation("튜티 정보 수정")
    @PutMapping
    public ResponseEntity<?> editTutee(@CurrentUser User user,
                                       @Valid @RequestBody TuteeUpdateRequest tuteeUpdateRequest) {

        // TODO - CHECK : Bearer Token 없이 요청하는 경우
        // user = null
        // .antMatchers(HttpMethod.PUT, "/**").authenticated()

        tuteeService.updateTutee(user, tuteeUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("튜티 탈퇴")
    @DeleteMapping
    public ResponseEntity<?> quitTutee(@CurrentUser User user) {

        tuteeService.deleteTutee(user);
        return ResponseEntity.ok().build();
    }

}
