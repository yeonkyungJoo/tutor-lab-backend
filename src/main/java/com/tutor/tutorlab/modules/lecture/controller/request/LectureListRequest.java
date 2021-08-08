package com.tutor.tutorlab.modules.lecture.controller.request;

import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.GroupSequence;
import javax.validation.constraints.AssertTrue;

@Builder
@AllArgsConstructor
@Getter
public class LectureListRequest {

    // 강의종류
    private String parent; // 종류

    private String subject; // 언어

    // 수업 방식
    private SystemType system; // 온/오프라인

    private Boolean isGroup; // 그룹여부

    // TODO 주소

    // 레벨
    private DifficultyType difficulty; // 난이도
}
