package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureSubjectRepository extends JpaRepository<LectureSubject, Long> {
}
