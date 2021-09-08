package com.tutor.tutorlab.modules.review.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.review.controller.request.ReviewCreateRequest;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.*;

@Transactional(readOnly = false)
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LectureRepository lectureRepository;

    public void createTutorReview(Tutor tutor, Long lectureId, Long parentId, ReviewCreateRequest reviewCreateRequest) {

        Lecture lecture = lectureRepository.findByTutorAndId(tutor, lectureId)
                .orElseThrow(() -> new EntityNotFoundException(LECTURE));

        Review parent = reviewRepository.findByLectureAndId(lecture, parentId)
                .orElseThrow(() -> new EntityNotFoundException(REVIEW));

        Review review = new Review();
        review.setScore(reviewCreateRequest.getScore());
        review.setContent(reviewCreateRequest.getContent());
        review.setLecture(lecture);
        review.setUser(tutor.getUser());
        review.setParent(parent);
    }

    // TODO
    public void updateTutorReview() {}

    // TODO
    public void deleteTutorReview() {}

    // TODO
    public void createTuteeReview() {}

    // TODO
    public void updateTuteeReview() {}

    // TODO
    public void deleteTuteeReview() {}

}
