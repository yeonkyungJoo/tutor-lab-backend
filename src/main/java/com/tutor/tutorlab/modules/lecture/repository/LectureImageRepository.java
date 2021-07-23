package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.lecture.vo.LectureImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureImageRepository extends JpaRepository<LectureImage, Long> {
}
