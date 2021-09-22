package com.tutor.tutorlab.modules.lecture.common;

import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;

import java.util.List;

public class LectureBuilder {

    public static LectureCreateRequest.LecturePriceCreateRequest getLecturePriceCreateRequest(
            Boolean isGroup, Integer groupNumber, Long pertimeCost, Integer pertimeLecture, Long totalCost, Integer totalTime) {

        return LectureCreateRequest.LecturePriceCreateRequest.builder()
                .isGroup(isGroup)
                .groupNumber(groupNumber)
                .pertimeCost(pertimeCost)
                .pertimeLecture(pertimeLecture)
                .totalCost(totalCost)
                .totalTime(totalTime)
                .build();
    }

    public static LectureCreateRequest.LectureSubjectCreateRequest getLectureSubjectCreateRequest(String parent, String krSubject) {
        return LectureCreateRequest.LectureSubjectCreateRequest.builder()
                .parent(parent)
                .krSubject(krSubject)
                .build();
    }

    public static LectureListRequest getLectureListRequest(
            List<String> parents, List<String> subjects, List<DifficultyType> difficultyTypes, List<SystemType> systemTypes, boolean isGroup) {

        return LectureListRequest.builder()
                .parents(parents)
                .subjects(subjects)
                .difficulties(difficultyTypes)
                .systems(systemTypes)
                .isGroup(isGroup)
                .build();
    }

}
