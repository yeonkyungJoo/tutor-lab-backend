package com.tutor.tutorlab.modules.lecture.controller.response;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LectureResponse {

    private Long id;
    private String thumbnail;
    private String title;
    private String subTitle;
    private String introduce;
    private String content;
    private DifficultyType difficultyType;
    // private String difficultyName;
    private List<SystemTypeResponse> systemTypes;
    private List<LecturePriceResponse> lecturePrices;
    private List<LectureSubjectResponse> lectureSubjects;

//    // 리뷰 총 개수
//    private Integer reviewCount;
//    // 강의 평점
//    private Integer scoreAverage;


    public LectureResponse(Lecture lecture) {
        this.id = lecture.getId();
        this.thumbnail = lecture.getThumbnail();
        this.title = lecture.getTitle();
        this.subTitle = lecture.getSubTitle();
        this.introduce = lecture.getIntroduce();
        this.content = lecture.getContent();
        this.difficultyType = lecture.getDifficultyType();
        this.systemTypes = lecture.getSystemTypes().stream()
                .map(SystemTypeResponse::new).collect(Collectors.toList());
        this.lecturePrices = lecture.getLecturePrices().stream()
                .map(LecturePriceResponse::new).collect(Collectors.toList());
        this.lectureSubjects = lecture.getLectureSubjects().stream()
                .map(LectureSubjectResponse::new).collect(Collectors.toList());
    }

    @Data
    public static class LectureTutorResponse {

        // 총 강의 수
        private Integer lectureCount;
        // 리뷰 개수
        private Integer reviewCount;
        // 닉네임
        private String nickname;
        // 프로필사진
        private String image;

        public LectureTutorResponse(Tutor tutor) {
            this.lectureCount = 0;
            this.reviewCount = 0;
            this.nickname = tutor.getUser().getNickname();
            this.image = tutor.getUser().getImage();
        }
    }

    @Data
    public static class LectureSubjectResponse {

        private String parent;
        private String krSubject;

        public LectureSubjectResponse(LectureSubject lectureSubject) {
            this.parent = lectureSubject.getParent();
            this.krSubject = lectureSubject.getKrSubject();
        }
    }

    @Data
    public static class LecturePriceResponse {

        private Boolean isGroup;
        private Integer groupNumber;
        private Integer totalTime;
        private Integer pertimeLecture;
        private Long pertimeCost;
        private Long totalCost;

        public LecturePriceResponse(LecturePrice lecturePrice) {
            this.isGroup = lecturePrice.getIsGroup();
            this.groupNumber = lecturePrice.getGroupNumber();
            this.totalTime = lecturePrice.getTotalTime();
            this.pertimeLecture = lecturePrice.getPertimeLecture();
            this.pertimeCost = lecturePrice.getPertimeCost();
            this.totalCost = lecturePrice.getTotalCost();
        }
    }

    @Data
    public static class SystemTypeResponse {

        private String type;
        private String name;

        public SystemTypeResponse(SystemType systemType) {
            this.type = systemType.getType();
            this.name = systemType.getName();
        }
    }
}
