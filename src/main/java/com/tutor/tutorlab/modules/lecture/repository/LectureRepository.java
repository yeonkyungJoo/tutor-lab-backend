package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByTutor(Tutor tutor);
    Page<Lecture> findByTutor(Tutor tutor, Pageable pageable);

    Optional<Lecture> findByTutorAndId(Tutor tutor, Long lectureId);
}
