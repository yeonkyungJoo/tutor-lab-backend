package com.tutor.tutorlab.modules.lecture.controller.response;

import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SimpleLectureResponse {

    private Long id;
    private String thumbnail;
    private String title;
    private String subTitle;
    private String introduce;
    private DifficultyType difficultyType;
    private List<LectureResponse.SystemTypeResponse> systemTypes;
    private List<LectureResponse.LecturePriceResponse> lecturePrices;
    private List<LectureResponse.LectureSubjectResponse> lectureSubjects;

    public SimpleLectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.thumbnail = lecture.getThumbnail();
        this.title = lecture.getTitle();
        this.subTitle = lecture.getSubTitle();
        this.introduce = lecture.getIntroduce();
        this.difficultyType = lecture.getDifficultyType();
        this.systemTypes = lecture.getSystemTypes().stream()
                .map(LectureResponse.SystemTypeResponse::new).collect(Collectors.toList());
        this.lecturePrices = lecture.getLecturePrices().stream()
                .map(LectureResponse.LecturePriceResponse::new).collect(Collectors.toList());
        this.lectureSubjects = lecture.getLectureSubjects().stream()
                .map(LectureResponse.LectureSubjectResponse::new).collect(Collectors.toList());
    }
}
