package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByTutee(Tutee tutee);
    List<Enrollment> findByLecture(Lecture lecture);
    Page<Enrollment> findByTutee(Tutee tutee, Pageable pageable);
    Page<Enrollment> findByLecture(Lecture lecture, Pageable pageable);

    Enrollment findByTuteeAndLecture(Tutee tutee, Lecture lecture);
    void deleteByTuteeAndLecture(Tutee tutee, Lecture lecture);
}
