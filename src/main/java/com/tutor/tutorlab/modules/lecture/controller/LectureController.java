package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.security.CurrentUser;
import com.tutor.tutorlab.modules.account.controller.AbstractController;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.review.dto.ReviewDto;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.REVIEW;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lectures")
public class LectureController extends AbstractController {

    private final LectureService lectureService;
    private final LectureRepository lectureRepository;
    private final ReviewRepository reviewRepository;

    @ApiOperation("강의 개별 조회")
    @GetMapping(value = "/{lecture_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getLecture(@PathVariable(name = "lecture_id") Long lectureId) {
        return new ResponseEntity(lectureService.getLecture(lectureId), HttpStatus.OK);
    }

    @ApiOperation("강의 등록")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addLecture(@CurrentUser User user,
                                     @RequestBody @Validated(AddLectureRequest.Order.class) AddLectureRequest addLectureRequest) {
        return new ResponseEntity(lectureService.addLecture(addLectureRequest, user), HttpStatus.CREATED);
    }

    // TODO
    @ApiOperation("강의 목록 조회")
    @GetMapping
    public Object getLectures(@ModelAttribute @Validated LectureListRequest lectureListRequest) {
        return lectureService.getLectures(lectureListRequest);
    }

    @ApiOperation("강의별 리뷰 리스트 - 페이징")
    @GetMapping("/{lecture_id}/reviews")
    public ResponseEntity getReviewsOfLecture(@CurrentUser User user,
                                              @PathVariable(name = "lecture_id") Long lectureId,
                                              @RequestParam(defaultValue = "1") Integer page) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Page<ReviewDto> reviews = reviewRepository.findByLecture(lecture,
                PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").ascending()))
                .map(review -> new ReviewDto(review));
        return new ResponseEntity(reviews, HttpStatus.OK);
    }

    @ApiOperation("강의 리뷰 개별 조회")
    @GetMapping("/{lecture_id}/reviews/{review_id}")
    public ResponseEntity getReviewOfLecture(@CurrentUser User user,
                                    @PathVariable(name = "lecture_id") Long lectureId,
                                    @PathVariable(name = "review_id") Long reviewId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Review review = reviewRepository.findByLectureAndId(lecture, reviewId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));
        return new ResponseEntity(new ReviewDto(review), HttpStatus.OK);
    }

}
