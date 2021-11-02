package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class TuteeLectureResponse {

    private Long tuteeId;
    private Long lectureId;
    private String thumbnail;
    private String title;
    private String subTitle;
    private String introduce;
    private String content;
    private LectureResponse.LecturePriceResponse lecturePrice;
    private List<LectureResponse.SystemTypeResponse> systemTypes;
    private Long reviewId;

    public TuteeLectureResponse(Long tuteeId, Lecture lecture, LecturePrice lecturePrice, Long reviewId) {
        this.tuteeId = tuteeId;
        this.lectureId = lectureId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.subTitle = subTitle;
        this.introduce = introduce;
        this.content = content;
        // this.lecturePrice = lecturePrice;
        this.systemTypes = systemTypes;
        this.reviewId = reviewId;
    }
}
