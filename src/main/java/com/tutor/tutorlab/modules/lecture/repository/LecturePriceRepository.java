package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface LecturePriceRepository extends JpaRepository<LecturePrice, Long> {

    List<LecturePrice> findByLecture(Lecture lecture);
}
