package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.repository.ReviewQueryRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.LECTURE;

@Transactional
@SpringBootTest
public class ReviewListTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewQueryRepository reviewQueryRepository;

    @Autowired
    LectureRepository lectureRepository;

    @Test
    void findWithUserByLecture() {

        Lecture lecture = lectureRepository.findById(10L)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> START");
        Page<Review> review = reviewQueryRepository.findReviewsWithUserByLecture(lecture, PageRequest.of(0, 20, Sort.by("id").ascending()));
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> END");
    }

    @Test
    void getReviewResponsesOfLecture() {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> START");
        Page<ReviewResponse> reviewResponses = reviewService.getReviewResponsesOfLecture(10L, 1);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> END");
    }
}
