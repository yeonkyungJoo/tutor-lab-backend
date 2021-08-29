package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    @ApiOperation("강의 한건 조회")
    @GetMapping(value = "/{lecture_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getLecture(@PathVariable(name = "lecture_id") Long lectureId) {
        return new ResponseEntity(lectureService.getLecture(lectureId), HttpStatus.OK);
    }

    @ApiOperation("강의 등록")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addLecture(@RequestBody @Validated(AddLectureRequest.Order.class) AddLectureRequest addLectureRequest,
                             @CurrentUser User user) {
        return new ResponseEntity(lectureService.addLecture(addLectureRequest, user), HttpStatus.CREATED);
    }

    @ApiOperation("강의 목록 조회")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getLectures(@ModelAttribute @Validated LectureListRequest lectureListRequest) {
        return new ResponseEntity(lectureService.getLectures(lectureListRequest), HttpStatus.OK);
    }

}
