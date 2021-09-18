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
import com.tutor.tutorlab.modules.chat.repository.MessageRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.chat.vo.Message;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstructUtil;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.dto.ReviewDto;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import com.tutor.tutorlab.modules.review.vo.Review;
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

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.REVIEW;

@Api(tags = {"TuteeController"})
@RequestMapping("/tutees")
@RestController
@RequiredArgsConstructor
public class TuteeController extends AbstractController {

    private final TuteeService tuteeService;
    private final TuteeRepository tuteeRepository;

    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;
    private final LectureMapstructUtil lectureMapstructUtil;

    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    private final ChatroomRepository chatroomRepository;
    private final PickRepository pickRepository;

    private final MessageRepository messageRepository;

    // TODO - 검색
    @ApiOperation("튜티 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity<?> getTutees(@RequestParam(defaultValue = "1") Integer page) {

        Page<TuteeDto> tutees = tuteeRepository.findAll(
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(tutee -> new TuteeDto(tutee));
        return new ResponseEntity(tutees, HttpStatus.OK);
    }

    @ApiOperation("튜티 조회")
    @GetMapping("/{tutee_id}")
    public ResponseEntity<?> getTutee(@PathVariable(name = "tutee_id") Long tuteeId) {

        Tutee tutee = tuteeRepository.findById(tuteeId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 튜티입니다."));
        return new ResponseEntity(new TuteeDto(tutee), HttpStatus.OK);
    }

    @ApiOperation("튜티 정보 수정")
    @PutMapping
    public ResponseEntity<?> editTutee(@CurrentUser User user,
                                    @Valid @RequestBody TuteeUpdateRequest tuteeUpdateRequest) {

        // TODO - CHECK : Bearer Token 없이 요청하는 경우
        // user = null
        // .antMatchers(HttpMethod.PUT, "/**").authenticated()

        tuteeService.updateTutee(user, tuteeUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("튜티 탈퇴")
    @DeleteMapping
    public ResponseEntity<?> quitTutee(@CurrentUser User user) {

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

    @ApiOperation("수강 강의별 리뷰 조회 - 페이징")
    @GetMapping("/my-lectures/{lecture_id}/reviews")
    public ResponseEntity<?> getReviewsOfLecture(@CurrentUser User user,
                                              @PathVariable(name = "lecture_id") Long lectureId,
                                              @RequestParam(defaultValue = "1") Integer page) {

        Tutee tutee = tuteeRepository.findByUser(user);
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO - CHECK : findByLectureId(lectureId) vs (findById(lectureId) & findByLecture(lecture))
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
        // TODO - CHECK
        if (enrollment == null) {

        }
        Page<ReviewDto> reviews = reviewRepository.findByLecture(lecture,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(review -> new ReviewDto(review));
        return new ResponseEntity(reviews, HttpStatus.OK);
    }

    @ApiOperation("수강 강의별 리뷰 개별 조회")
    @GetMapping("/my-lectures/{lecture_id}/reviews/{review_id}")
    public ResponseEntity<?> getReviewOfLecture(@CurrentUser User user,
                                                @PathVariable(name = "lecture_id") Long lectureId,
                                                @PathVariable(name = "review_id") Long reviewId) {

        Tutee tutee = tuteeRepository.findByUser(user);
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        // TODO - CHECK : findByLectureId(lectureId) vs (findById(lectureId) & findByLecture(lecture))
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);
        // TODO - CHECK
        if (enrollment == null) {

        }

        Review review = reviewRepository.findByLectureAndId(lecture, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        return new ResponseEntity(new ReviewDto(review), HttpStatus.OK);
    }

    @ApiOperation("튜티 리뷰 작성")
    @PostMapping("/my-lectures/{lecture_id}/reviews")
    public ResponseEntity newReview(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId,
                                    @RequestBody @Valid TuteeReviewCreateRequest tuteeReviewCreateRequest) {

        Tutee tutee = tuteeRepository.findByUser(user);
        Review review = reviewService.createTuteeReview(tutee, lectureId, tuteeReviewCreateRequest);
        return new ResponseEntity(new ReviewDto(review), HttpStatus.CREATED);
    }

    @ApiOperation("튜티 리뷰 수정")
    @PutMapping("/my-lectures/{lecture_id}/reviews/{review_id}")
    public ResponseEntity editReview(@CurrentUser User user,
                                     @PathVariable(name = "lecture_id") Long lectureId,
                                     @PathVariable(name = "review_id") Long reviewId,
                                     @RequestBody @Valid TuteeReviewUpdateRequest tuteeReviewUpdateRequest) {

        Tutee tutee = tuteeRepository.findByUser(user);
        reviewService.updateTuteeReview(tutee, lectureId, reviewId, tuteeReviewUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("튜티 리뷰 삭제")
    @DeleteMapping("/my-lectures/{lecture_id}/reviews/{review_id}")
    public ResponseEntity deleteReview(@CurrentUser User user,
                                       @PathVariable(name = "lecture_id") Long lectureId,
                                       @PathVariable(name = "review_id") Long reviewId) {

        Tutee tutee = tuteeRepository.findByUser(user);
        reviewService.deleteTuteeReview(tutee, lectureId, reviewId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("채팅방 전체 조회 - 페이징")
    @GetMapping("/my-chatrooms")
    public ResponseEntity getChatrooms(@CurrentUser User user,
                                       @RequestParam(defaultValue = "1") Integer page) {

        Tutee tutee = tuteeRepository.findByUser(user);
        // TODO - CHECK : Fetch join
        Page<ChatroomController.ChatroomDto> chatrooms = chatroomRepository.findByTutee(tutee,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(chatroom -> {
                    ChatroomController.ChatroomDto chatroomDto = new ChatroomController.ChatroomDto(chatroom);
                    chatroomDto.setLastMessage(messageRepository.findFirstByChatroomIdOrderByIdDesc(chatroom.getId()));
                    return chatroomDto;
                });
        return new ResponseEntity(chatrooms, HttpStatus.OK);
    }

    @ApiOperation("채팅방 개별 조회")
    @GetMapping("/my-chatrooms/{chatroom_id}")
    public ResponseEntity getChatroom(@CurrentUser User user,
                                      @PathVariable(name = "chatroom_id") Long chatroomId) {
        List<Message> message = messageRepository.findAllByChatroomId(chatroomId);

//        Chatroom chatroom = chatroomRepository.findById(chatroomId)
//                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."));
        return new ResponseEntity(message,HttpStatus.OK);
    }


    @ApiOperation("장바구니 조회 - 페이징")
    @GetMapping("/my-picks")
    public ResponseEntity getPicks(@CurrentUser User user,
                                   @RequestParam(defaultValue = "1") Integer page) {

        Tutee tutee = tuteeRepository.findByUser(user);
        Page<Pick> picks = pickRepository.findByTutee(tutee,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()));
        // TODO - PickDto
        return new ResponseEntity(picks, HttpStatus.OK);
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
