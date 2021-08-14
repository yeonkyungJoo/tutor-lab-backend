package com.tutor.tutorlab.modules.lecture.controller.response;

import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
public class LectureResponse {
    private final Long id;

    // TODO UserResponse 정의해야함.
    private String thumbnail;

    private final String title;

    private final String subTitle;

    private final String introduce;

    private final String content;

    private final String difficultyType;

    private final String difficultyName;

    private final List<SystemTypeResponse> systemTypes;

    private final List<LecturePriceResponse> lecturePrices;

    private final List<LectureSubjectResponse> lectureSubjects;

    @Value
    public static class LectureSubjectResponse {
        private final String parent;

        private final String enSubject;

        private final String krSubject;
    }

    @Value
    public static class LecturePriceResponse {
        private final Boolean isGroup;

        private final Integer groupNumber;

        private final Integer totalTime;

        private final Integer pertimeLecture;

        private final Long pertimeCost;

        private final Long totalCost;
    }

    @Value
    public static class SystemTypeResponse {
        private final String type;

        private final String name;
    }
}
