package com.tutor.tutorlab.modules.account.controller;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.controller.ChatroomController;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.repository.MessageRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstructUtil;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
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
import java.util.stream.Collectors;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.*;

@Api(tags = {"TutorController"})
@RequestMapping("/tutors")
@RestController
@RequiredArgsConstructor
public class TutorController extends AbstractController {

    private final TutorService tutorService;
    private final TutorRepository tutorRepository;
    private final CareerRepository careerRepository;
    private final EducationRepository educationRepository;

    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LectureMapstructUtil lectureMapstructUtil;

    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;

    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;


    // TODO - 검색
    @ApiOperation("튜터 전체 조회 - 페이징")
    @GetMapping
    public ResponseEntity getTutors(@RequestParam(defaultValue = "1") Integer page) {

        Page<TutorDto> tutors = tutorRepository.findAll(
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(tutor -> new TutorDto(tutor));

        return new ResponseEntity(tutors, HttpStatus.OK);
    }

    @ApiOperation("튜터 조회")
    @GetMapping("/{tutor_id}")
    public ResponseEntity getTutor(@PathVariable(name = "tutor_id") Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException(TUTOR));
        return new ResponseEntity(new TutorDto(tutor), HttpStatus.OK);
    }

    @ApiOperation("튜터 강의조회")
    @GetMapping("/mylectures")
    public ResponseEntity getTutorLecture(@CurrentUser User user) {

//        Tutor tutor = tutorRepository.findById(tutorId)
//                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 튜터입니다."));
        List<LectureResponse> lectures = tutorService.getTutorLecture(user).stream()
                .map(lecture -> new LectureResponse(lecture))
                .collect(Collectors.toList());

        return new ResponseEntity(lectures, HttpStatus.OK);
    }

    @ApiOperation("튜터 등록")
    @PostMapping
    public ResponseEntity newTutor(@CurrentUser User user,
                                @Valid @RequestBody TutorSignUpRequest tutorSignUpRequest) {

        tutorService.createTutor(user, tutorSignUpRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ApiOperation("튜터 정보 수정")
    @PutMapping
    public ResponseEntity editTutor(@CurrentUser User user,
                                    @Valid @RequestBody TutorUpdateRequest tutorUpdateRequest) {

        tutorService.updateTutor(user, tutorUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("튜터 탈퇴")
    @DeleteMapping
    public ResponseEntity quitTutor(@CurrentUser User user) {

        tutorService.deleteTutor(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("튜터의 Career 리스트")
    @GetMapping("/{tutor_id}/careers")
    public ResponseEntity getCareers(@PathVariable(name = "tutor_id") Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException(TUTOR));
        List<CareerController.CareerDto> careers = careerRepository.findByTutor(tutor).stream()
                .map(career -> new CareerController.CareerDto(career))
                .collect(Collectors.toList());

        return new ResponseEntity(careers, HttpStatus.OK);
    }

    @ApiOperation("튜터의 Education 리스트")
    @GetMapping("/{tutor_id}/educations")
    public ResponseEntity getEducations(@PathVariable(name = "tutor_id") Long tutorId) {

        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException(TUTOR));
        List<EducationController.EducationDto> educations = educationRepository.findByTutor(tutor).stream()
                .map(education -> new EducationController.EducationDto(education))
                .collect(Collectors.toList());

        return new ResponseEntity(educations, HttpStatus.OK);
    }

    @ApiOperation("등록 강의 전체 조회 - 페이징")
    @GetMapping("/my-lectures")
    public ResponseEntity getLectures(@CurrentUser User user,
                                      @RequestParam(defaultValue = "1") Integer page) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        Page<LectureResponse> lectures = lectureRepository.findByTutor(tutor,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(lecture -> lectureMapstructUtil.getLectureResponse(lecture));
        return new ResponseEntity(lectures, HttpStatus.OK);
    }

    @ApiOperation("등록 강의 개별 조회")
    @GetMapping("/my-lectures/{lecture_id}")
    public ResponseEntity getLecture(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }
        // TODO - CHECK : lectureService의 getLecture와 동일
        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));
        LectureResponse lectureResponse = lectureMapstructUtil.getLectureResponse(lecture);
        return new ResponseEntity(lectureResponse, HttpStatus.OK);
    }

    @ApiOperation("등록 강의별 튜티 조회 - 페이징")
    @GetMapping("/my-lectures/{lecture_id}/tutees")
    public ResponseEntity getTuteesOfLecture(@CurrentUser User user,
                                             @PathVariable(name = "lecture_id") Long lectureId,
                                             @RequestParam(defaultValue = "1") Integer page) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Page<TuteeController.TuteeDto> tutees = enrollmentRepository.findByLecture(lecture,
                // TODO - CHECK
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(enrollment -> new TuteeController.TuteeDto(enrollment.getTutee()));
        return new ResponseEntity(tutees, HttpStatus.OK);
    }

    @ApiOperation("등록 강의별 리뷰 조회 - 페이징")
    @GetMapping("/my-lectures/{lecture_id}/reviews")
    public ResponseEntity getReviewsOfLecture(@CurrentUser User user,
                                              @PathVariable(name = "lecture_id") Long lectureId,
                                              @RequestParam(defaultValue = "1") Integer page) {

        // TODO - CHECK : 조회도 Service Layer에서?
        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }
        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Page<ReviewDto> reviews = reviewRepository.findByLecture(lecture,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(review -> new ReviewDto(review));
        return new ResponseEntity(reviews, HttpStatus.OK);
    }

    @ApiOperation("등록 강의별 리뷰 개별 조회")
    @GetMapping("/my-lectures/{lecture_id}/reviews/{review_id}")
    public ResponseEntity getReviewOfLecture(@CurrentUser User user,
                                             @PathVariable(name = "lecture_id") Long lectureId,
                                             @PathVariable(name = "review_id") Long reviewId) {

        // TODO - CHECK : 조회도 Service Layer에서?
        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Review review = reviewRepository.findByLectureAndId(lecture, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        return new ResponseEntity(new ReviewDto(review), HttpStatus.OK);
    }

    @ApiOperation("튜터 리뷰 작성")
    @PostMapping("/my-lectures/{lecture_id}/reviews/{parent_id}")
    public ResponseEntity newReview(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId,
                                    @PathVariable(name = "parent_id") Long parentId,
                                    @RequestBody @Valid TutorReviewCreateRequest tutorReviewCreateRequest) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }
        Review review = reviewService.createTutorReview(tutor, lectureId, parentId, tutorReviewCreateRequest);
        return new ResponseEntity(new ReviewDto(review), HttpStatus.CREATED);
    }

    @ApiOperation("튜터 리뷰 수정")
    @PutMapping("/my-lectures/{lecture_id}/reviews/{parent_id}/children/{review_id}")
    public ResponseEntity editReview(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId,
                                    @PathVariable(name = "parent_id") Long parentId,
                                    @PathVariable(name = "review_id") Long reviewId,
                                    @RequestBody @Valid TutorReviewUpdateRequest tutorReviewUpdateRequest) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }
        reviewService.updateTutorReview(tutor, lectureId, parentId, reviewId, tutorReviewUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("튜터 리뷰 삭제")
    @DeleteMapping("/my-lectures/{lecture_id}/reviews/{parent_id}/children/review_id}")
    public ResponseEntity deleteReview(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId,
                                    @PathVariable(name = "parent_id") Long parentId,
                                    @PathVariable(name = "review_id") Long reviewId) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }
        reviewService.deleteTutorReview(tutor, lectureId, parentId, reviewId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("채팅방 전체 조회 - 페이징")
    @GetMapping("/my-chatrooms")
    public ResponseEntity getChatrooms(@CurrentUser User user,
                                       @RequestParam(defaultValue = "1") Integer page) {

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        // TODO - CHECK : Fetch join
        Page<ChatroomController.ChatroomDto> chatrooms = chatroomRepository.findByTutor(tutor,
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

        Tutor tutor = tutorRepository.findByUser(user);
        if (tutor == null) {
            throw new UnauthorizedException();
        }

        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new EntityNotFoundException(CHATROOM));
        return new ResponseEntity(new ChatroomController.ChatroomDto(chatroom), HttpStatus.OK);
    }

    @Data
    static class TutorDto {

        private UserController.UserDto user;
        private String subjects;
        private List<CareerController.CareerDto> careers;
        private List<EducationController.EducationDto> educations;
        private boolean specialist;

        public TutorDto(Tutor tutor) {
            this.user = new UserController.UserDto(tutor.getUser());
            this.subjects = tutor.getSubjects();
            this.careers = tutor.getCareers().stream()
                    .map(career -> new CareerController.CareerDto(career)).collect(Collectors.toList());
            this.educations = tutor.getEducations().stream()
                    .map(education -> new EducationController.EducationDto(education)).collect(Collectors.toList());
            this.specialist = tutor.isSpecialist();
        }

    }

}
