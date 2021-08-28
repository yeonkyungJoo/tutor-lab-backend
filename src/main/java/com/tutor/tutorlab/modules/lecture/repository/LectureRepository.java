package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByTutorUser(User user);
}
