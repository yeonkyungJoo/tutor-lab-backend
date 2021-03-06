package com.tutor.tutorlab.modules.purchase.controller.response;

import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EnrolledLectureResponse {

    private Long lectureId;
    private String thumbnail;
    private String title;
    private String subTitle;
    private String introduce;
    private String content;
    private LectureResponse.LecturePriceResponse lecturePrice;
    private List<LectureResponse.SystemTypeResponse> systemTypes;

    public EnrolledLectureResponse(Lecture lecture, LecturePrice lecturePrice) {
        this.lectureId = lecture.getId();
        this.thumbnail = lecture.getThumbnail();
        this.title = lecture.getTitle();
        this.subTitle = lecture.getSubTitle();
        this.introduce = lecture.getIntroduce();
        this.content = lecture.getContent();
        this.lecturePrice = new LectureResponse.LecturePriceResponse(lecturePrice);
        this.systemTypes = lecture.getSystemTypes().stream()
                .map(LectureResponse.SystemTypeResponse::new)
                .collect(Collectors.toList());
    }
}
