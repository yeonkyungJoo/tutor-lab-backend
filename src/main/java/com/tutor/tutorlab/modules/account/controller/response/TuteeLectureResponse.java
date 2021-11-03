package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrolledLectureResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
public class TuteeLectureResponse {

    private Long tuteeId;
    private EnrolledLectureResponse lecture;
    private Long reviewId;

    @Builder(access = AccessLevel.PUBLIC)
    private TuteeLectureResponse(Long tuteeId, Lecture lecture, LecturePrice lecturePrice, Long reviewId) {
        this.tuteeId = tuteeId;
        this.lecture = new EnrolledLectureResponse(lecture, lecturePrice);
        this.reviewId = reviewId;
    }
}
