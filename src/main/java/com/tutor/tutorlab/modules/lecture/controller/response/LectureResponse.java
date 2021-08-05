package com.tutor.tutorlab.modules.lecture.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
public class LectureResponse {

    private final Long id;

    // TODO UserResponse 정의해야함.

    private final String title;

    private final String subTitle;

    private final String content;

    private final Integer totalTime;

    private final Long pertimeCost;

    private final Long totalCost;

    private final String difficultyType;

    private final String difficultyName;

    @JsonProperty("is_group")
    private final Boolean isGroup;

    private final String systemType;

    private final String systemName;

}
