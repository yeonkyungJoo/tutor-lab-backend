package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByTutee(Tutee tutee);
    List<Enrollment> findByLecture(Lecture lecture);
    Page<Enrollment> findByTutee(Tutee tutee, Pageable pageable);
    Page<Enrollment> findByLecture(Lecture lecture, Pageable pageable);

    Optional<Enrollment> findByLectureAndId(Lecture lecture, Long enrollmentId);
    Optional<Enrollment> findByTuteeAndLecture(Tutee tutee, Lecture lecture);
}
