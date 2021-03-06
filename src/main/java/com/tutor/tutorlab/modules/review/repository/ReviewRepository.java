package com.tutor.tutorlab.modules.review.repository;

import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.vo.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select * from review where lecture_id = :lectureId", nativeQuery = true)
    List<Review> findAllByLectureId(Long lectureId);

    List<Review> findByLecture(Lecture lecture);
    // Page<Review> findByLecture(Lecture lecture, Pageable pageable);

    Review findByEnrollment(Enrollment enrollment);
    // List<Review> findByEnrollment(Enrollment enrollment);

    List<Review> findByLectureAndEnrollmentIsNotNull(Lecture lecture);

    // TODO - CHECK
    Optional<Review> findByLectureAndId(Lecture lecture, Long reviewId);
    Optional<Review> findByEnrollmentAndId(Enrollment enrollment, Long reviewId);

    Optional<Review> findByParent(Review parent);
    Optional<Review> findByParentAndId(Review parent, Long reviewId);

    // TODO - CHECK : 쿼리
    Integer countByLectureInAndEnrollmentIsNotNull(List<Lecture> lectures);
}
