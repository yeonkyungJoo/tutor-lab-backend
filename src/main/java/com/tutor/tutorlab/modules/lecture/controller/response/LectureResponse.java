package com.tutor.tutorlab.modules.lecture.controller.response;

import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Value
public class LectureResponse {

    private final Long id;
    // TODO UserResponse 정의해야함.
    private final String thumbnail;

    private final String title;

    private final String subTitle;

    private final String introduce;

    private final String content;

    private final DifficultyType difficultyType;

    private final String difficultyName;

    private final List<SystemTypeResponse> systemTypes;

    private final List<LecturePriceResponse> lecturePrices;

    private final List<LectureSubjectResponse> lectureSubjects;



    public LectureResponse(){
        this.id = 0L;
        this.thumbnail = "";
        this.title = "";
        this.subTitle = "";
        this.introduce = "";
        this.content = "";
        this.difficultyType = null;
        this.difficultyName = null;
        this.systemTypes = null;
        this.lecturePrices = null;
        this.lectureSubjects = null;
    }


    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.thumbnail = lecture.getThumbnail();
        this.title = lecture.getTitle();
        this.subTitle = lecture.getSubTitle();
        this.introduce = lecture.getIntroduce();
        this.content = lecture.getContent();
        this.difficultyType = lecture.getDifficultyType();
        this.difficultyName = "";
        this.systemTypes = lecture.getSystemTypes().stream().map(systemType->new SystemTypeResponse(systemType)).collect(Collectors.toList());
        this.lecturePrices = lecture.getLecturePrices().stream().map(lecturePrice->new LecturePriceResponse(lecturePrice)).collect(Collectors.toList());
        this.lectureSubjects =lecture.getLectureSubjects().stream().map(lectureSubject -> new LectureSubjectResponse(lectureSubject)).collect(Collectors.toList());

    }

    @Value
    public static class LectureSubjectResponse {
        private final String parent;

        private final String krSubject;

        public LectureSubjectResponse(LectureSubject lectureSubject) {
            this.parent = lectureSubject.getParent();
            this.krSubject = lectureSubject.getKrSubject();
        }
    }

    @Value
    public static class LecturePriceResponse {
        private final Boolean isGroup;

        private final Integer groupNumber;

        private final Integer totalTime;

        private final Integer pertimeLecture;

        private final Long pertimeCost;

        private final Long totalCost;

        public LecturePriceResponse(LecturePrice lecturePrice) {
            this.isGroup = lecturePrice.getIsGroup();
            this.groupNumber = lecturePrice.getGroupNumber();
            this.totalTime = lecturePrice.getTotalTime();
            this.pertimeLecture = lecturePrice.getPertimeLecture();
            this.pertimeCost = lecturePrice.getPertimeCost();
            this.totalCost = lecturePrice.getTotalCost();
        }


    }

    @Value
    public static class SystemTypeResponse {
        private final String type;

        private final String name;

        public SystemTypeResponse(SystemType systemType) {
            this.type = systemType.getType();
            this.name = systemType.getName();
        }
    }
}
