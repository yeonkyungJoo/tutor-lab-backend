package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturePriceRepository extends JpaRepository<LecturePrice, Long> {
}
