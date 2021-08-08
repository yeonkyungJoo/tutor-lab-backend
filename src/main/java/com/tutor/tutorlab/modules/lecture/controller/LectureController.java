package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    /**
     * 강의 한건 조회
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation("강의 한건 조회")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getLecture(@PathVariable long id) throws Exception {
        LectureResponse lectureResponse = lectureService.getLecture(id);
        return lectureResponse;
    }

    /**
     * 강의 등록
     * @param addLectureRequest
     * @return
     * @throws Exception
     */
    @ApiOperation("강의 등록")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object addLecture(@RequestBody @Validated(AddLectureRequest.Order.class) AddLectureRequest addLectureRequest) throws Exception {
        return lectureService.addLecture(addLectureRequest);
    }

}
