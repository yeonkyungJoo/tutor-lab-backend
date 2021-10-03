package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query(value = "select * from enrollment where enrollment_id = :enrollmentId", nativeQuery = true)
    Enrollment findAllById(Long enrollmentId);

    @Query(value = "select * from enrollment where tutee_id = :tuteeId", nativeQuery = true)
    List<Enrollment> findAllByTutee(Long tuteeId);

    List<Enrollment> findByTutee(Tutee tutee);
    // List<Enrollment> findByLecture(Lecture lecture);
    Page<Enrollment> findByTutee(Tutee tutee, Pageable pageable);
    Page<Enrollment> findByLecture(Lecture lecture, Pageable pageable);

    Optional<Enrollment> findByLectureAndId(Lecture lecture, Long enrollmentId);
    Optional<Enrollment> findByTuteeAndLecture(Tutee tutee, Lecture lecture);

    @Query(value = "select * from enrollment where tutee_id = :tuteeId and lecture_id = :lectureId", nativeQuery = true)
    Optional<Enrollment> findAllByTuteeIdAndLectureId(Long tuteeId, Long lectureId);

    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "delete from enrollment", nativeQuery = true)
    void deleteAllEnrollments();
}
