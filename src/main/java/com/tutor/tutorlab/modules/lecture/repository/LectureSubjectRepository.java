package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface LectureSubjectRepository extends JpaRepository<LectureSubject, Long> {
}
