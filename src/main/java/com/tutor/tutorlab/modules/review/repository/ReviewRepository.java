package com.tutor.tutorlab.modules.review.repository;

import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.vo.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByLecture(Lecture lecture);
    Page<Review> findByLecture(Lecture lecture, Pageable pageable);
    List<Review> findByEnrollment(Enrollment enrollment);

    // TODO - CHECK
    Optional<Review> findByLectureAndId(Lecture lecture, Long reviewId);
    Optional<Review> findByEnrollmentAndId(Enrollment enrollment, Long reviewId);

    Optional<Review> findByParentAndId(Review parent, Long reviewId);
}