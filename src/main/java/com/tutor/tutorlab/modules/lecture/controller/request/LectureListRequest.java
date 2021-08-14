package com.tutor.tutorlab.modules.lecture.controller.request;

import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class LectureListRequest {

    private String lectureName;

    // 강의종류
    private List<String> parent; // 종류

    private List<String> subject; // 언어

    // 수업 방식
    private List<SystemType> systems; // 온/오프라인

    private Boolean isGroup; // 그룹여부

    // TODO 주소

    // 레벨
    private List<DifficultyType> difficulty; // 난이도
}
