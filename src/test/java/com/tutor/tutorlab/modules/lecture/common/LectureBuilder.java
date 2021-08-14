package com.tutor.tutorlab.modules.lecture.common;

import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import org.springframework.boot.test.context.TestComponent;

import java.util.Arrays;
import java.util.List;

@TestComponent
public class LectureBuilder {
    public AddLectureRequest.AddLecturePriceRequest getAddLecturePriceRequest(
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

    public AddLectureRequest.AddLectureSubjectRequest getAddLectureSubjectRequest(String parent, String enSubject, String krSubject) {
        return AddLectureRequest.AddLectureSubjectRequest.builder()
                .parent(parent)
                .enSubject(enSubject)
                .krSubject(krSubject)
                .build();
    }

    public LectureListRequest getLectureListRequest(
            List<String> parents,
            List<String> subjects,
            List<DifficultyType> difficultyTypes,
            List<SystemType> systemTypes,
            boolean isGroup
    ) {
        return LectureListRequest.builder()
                .parent(Arrays.asList("개발", "프로그래밍언어"))
                .subject(Arrays.asList("자바", "백엔드", "프론트엔드"))
                .difficulty(Arrays.asList(DifficultyType.BEGINNER))
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .isGroup(true)
                .build();
    }

}
