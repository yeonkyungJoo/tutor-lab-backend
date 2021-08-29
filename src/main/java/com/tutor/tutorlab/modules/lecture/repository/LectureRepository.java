package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByTutorUser(User user);

    List<Lecture> findByTutor(Tutor tutor);
    Page<Lecture> findByTutor(Tutor tutor, Pageable pageable);
}
