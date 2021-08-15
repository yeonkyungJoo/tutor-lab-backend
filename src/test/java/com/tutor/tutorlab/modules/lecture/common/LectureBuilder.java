package com.tutor.tutorlab.modules.lecture.common;

import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;

import java.util.List;

public class LectureBuilder {
    public static AddLectureRequest.AddLecturePriceRequest getAddLecturePriceRequest(
            Boolean isGroup,
            Integer groupNumber,
            Long pertimeCost,
            Integer pertimeLecture,
            Long totalCost,
            Integer totalTime
    ) {
        AddLectureRequest.AddLecturePriceRequest price = AddLectureRequest.AddLecturePriceRequest.builder()
                .isGroup(isGroup)
                .groupNumber(groupNumber)
                .pertimeCost(pertimeCost)
                .pertimeLecture(pertimeLecture)
                .totalCost(totalCost)
                .totalTime(totalTime)
                .build();
        return price;
    }

    public static AddLectureRequest.AddLectureSubjectRequest getAddLectureSubjectRequest(
            String parent,
            String enSubject,
            String krSubject
    ) {
        return AddLectureRequest.AddLectureSubjectRequest.builder()
                .parent(parent)
                .enSubject(enSubject)
                .krSubject(krSubject)
                .build();
    }

    public static LectureListRequest getLectureListRequest(
            List<String> parents,
            List<String> subjects,
            List<DifficultyType> difficultyTypes,
            List<SystemType> systemTypes,
            boolean isGroup
    ) {
        return LectureListRequest.builder()
                .parents(parents)
                .subjects(subjects)
                .difficulties(difficultyTypes)
                .systems(systemTypes)
                .isGroup(isGroup)
                .build();
    }

}
