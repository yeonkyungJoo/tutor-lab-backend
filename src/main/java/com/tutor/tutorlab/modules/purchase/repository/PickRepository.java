package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Pick;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface PickRepository extends JpaRepository<Pick, Long> {

    List<Pick> findByTutee(Tutee tutee);
    Page<Pick> findByTutee(Tutee tutee, Pageable pageable);

    Optional<Pick> findByTuteeAndId(Tutee tutee, Long pickId);
    @Query(value = "select p from Pick p where p.tutee = :tutee and p.lecture.id = :lectureId")
    Optional<Pick> findByTuteeAndLectureId(Tutee tutee, Long lectureId);

    @Transactional
    void deleteByTutee(Tutee tutee);

    @Transactional
    void deleteByLecture(Lecture lecture);

    @Query(value = "select p from Pick p where p.lecture.id = :lectureId")
    List<Pick> findByLectureId(Long lectureId);
}
