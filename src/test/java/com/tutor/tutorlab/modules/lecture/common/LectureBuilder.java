package com.tutor.tutorlab.modules.lecture.common;

import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import org.springframework.boot.test.context.TestComponent;

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

}
