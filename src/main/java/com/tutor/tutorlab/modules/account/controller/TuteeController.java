package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.service.TuteeService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tutee")
@RestController
@RequiredArgsConstructor
public class TuteeController {

    private final TuteeService tuteeService;
    private final TuteeRepository tuteeRepository;

    /**
     * 튜티 전체 조회
     */
    @ApiOperation("튜티 전체 조회")
    @RequestMapping("/")
    public ResponseEntity getTutees() {
        return null;
    }

    // TODO - 검색
    /**
     * 튜티 전체 조회 - 페이징
     */
    @ApiOperation("튜티 전체 조회 - 페이징")
    @RequestMapping("/list")
    public ResponseEntity getTutees(@RequestParam(defaultValue = "1") Integer page) {
        return null;
    }

    /**
     * 튜티 조회
     */
    @ApiOperation("튜티 조회")
    @GetMapping("/{id}")
    public ResponseEntity getTutee(@PathVariable(name = "id") Long tuteeId) {
        return null;
    }

    /**
     * 튜티 정보 수정
     */
    @ApiOperation("튜티 정보 수정")
    @PutMapping("/edit")
    public ResponseEntity editTutee() {
        return null;
    }

    /**
     * 튜티 탈퇴
     */
    @ApiOperation("튜티 탈퇴")
    @DeleteMapping("/quit")
    public ResponseEntity quitTutee() {
        return null;
    }

    @Data
    static class TuteeDto {

        private UserController.UserDto user;
        private String subject;

        public TuteeDto(Tutee tutee) {

            this.user = new UserController.UserDto(tutee.getUser());

        }

    }
}
