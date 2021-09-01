package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.service.TuteeService;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.controller.ChatroomController;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstructUtil;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
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

@Api(tags = {"TuteeController"})
@RequestMapping("/tutees")
@RestController
@RequiredArgsConstructor
public class TuteeController extends AbstractController {

    private final TuteeService tuteeService;
    private final TuteeRepository tuteeRepository;

    private final EnrollmentRepository enrollmentRepository;
    private final LectureMapstructUtil lectureMapstructUtil;

    private final ChatroomRepository chatroomRepository;

/*
    @ApiOperation("튜티 전체 조회")
    @GetMapping
    public ResponseEntity getTutees() {

        List<TuteeDto> tutees = tuteeRepository.findAll().stream()
                .map(tutee -> new TuteeDto(tutee)).collect(Collectors.toList());
        return new ResponseEntity(tutees, HttpStatus.OK);
    }
*/

    // TODO - 검색
    @ApiOperation("튜티 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity getTutees(@RequestParam(defaultValue = "1") Integer page) {

        Page<TuteeDto> tutees = tuteeRepository.findAll(
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(tutee -> new TuteeDto(tutee));
        return new ResponseEntity(tutees, HttpStatus.OK);
    }

    @ApiOperation("튜티 조회")
    @GetMapping("/{tutee_id}")
    public ResponseEntity getTutee(@PathVariable(name = "tutee_id") Long tuteeId) {

        Tutee tutee = tuteeRepository.findById(tuteeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 튜티입니다."));
        return new ResponseEntity(new TuteeDto(tutee), HttpStatus.OK);
    }

    @ApiOperation("튜티 정보 수정")
    @PutMapping
    public ResponseEntity editTutee(@CurrentUser User user,
                                    @Valid @RequestBody TuteeUpdateRequest tuteeUpdateRequest) {

        // TODO - CHECK : Bearer Token 없이 요청하는 경우
        // user = null
        // .antMatchers(HttpMethod.PUT, "/**").authenticated()

        tuteeService.updateTutee(user, tuteeUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("튜티 탈퇴")
    @DeleteMapping
    public ResponseEntity quitTutee(@CurrentUser User user) {

        tuteeService.deleteTutee(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("수강 강의 조회 - 페이징")
    @GetMapping("/my-lectures")
    public ResponseEntity getLectures(@CurrentUser User user,
                                      @RequestParam(defaultValue = "1") Integer page) {

        Tutee tutee = tuteeRepository.findByUser(user);
        Page<LectureResponse> lectures = enrollmentRepository.findByTutee(tutee,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(enrollment -> lectureMapstructUtil.getLectureResponse(enrollment.getLecture()));
        return new ResponseEntity(lectures, HttpStatus.OK);
    }

    @ApiOperation("채팅방 전체 조회 - 페이징")
    @GetMapping("/my-chatrooms")
    public ResponseEntity getChatrooms(@CurrentUser User user,
                                       @RequestParam(defaultValue = "1") Integer page) {

        Tutee tutee = tuteeRepository.findByUser(user);
        // TODO - CHECK : Fetch join
        Page<ChatroomController.ChatroomDto> chatrooms = chatroomRepository.findByTutee(tutee,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(chatroom -> new ChatroomController.ChatroomDto(chatroom));
        return new ResponseEntity(chatrooms, HttpStatus.OK);
    }

    @ApiOperation("채팅방 개별 조회")
    @GetMapping("/my-chatrooms/{chatroom_id}")
    public ResponseEntity getChatroom(@CurrentUser User user,
                                      @PathVariable(name = "chatroom_id") Long chatroomId) {

        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));
        return new ResponseEntity(new ChatroomController.ChatroomDto(chatroom), HttpStatus.OK);
    }

    @Data
    static class TuteeDto {

        private UserController.UserDto user;
        private String subjects;

        public TuteeDto(Tutee tutee) {

            this.user = new UserController.UserDto(tutee.getUser());
            this.subjects = tutee.getSubjects();
        }

    }
}
