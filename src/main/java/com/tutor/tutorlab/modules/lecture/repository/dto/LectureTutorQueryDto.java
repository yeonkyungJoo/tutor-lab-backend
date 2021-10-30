package com.tutor.tutorlab.modules.lecture.repository.dto;

import lombok.Data;

@Data
public class LectureTutorQueryDto {

    private Long tutorId;
    private Long lectureCount;   // 총 강의 수
    private Long reviewCount;    // 리뷰 개수

    public LectureTutorQueryDto(Long tutorId, Long lectureCount, Long reviewCount) {
        this.tutorId = tutorId;
        this.lectureCount = lectureCount;
        this.reviewCount = reviewCount;
    }
}
