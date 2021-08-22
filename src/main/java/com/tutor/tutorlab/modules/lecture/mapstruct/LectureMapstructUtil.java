package com.tutor.tutorlab.modules.lecture.mapstruct;

import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LectureMapstructUtil {

    private final LectureMapstruct lectureMapstruct;

    public List<LectureResponse> getLectureResponses(List<Lecture> lectures) {
        return lectureMapstruct.lectureListToLectureResponseList(lectures);
    }

    public LectureResponse getLectureResponse(Lecture lecture) {

        List<LectureResponse.LecturePriceResponse> prices = lectureMapstruct.lecturePriceListToLecturePriceResponseList(lecture.getLecturePrices());
        List<LectureResponse.LectureSubjectResponse> subjects = lectureMapstruct.lectureSubjectListToLectureSubjectResponseList(lecture.getLectureSubjects());
        List<LectureResponse.SystemTypeResponse> systemTypes = lectureMapstruct.systemTypeListToSystemTypeResponseList(lecture.getSystemTypes().stream().map(systemType -> SystemType.find(systemType.getType())).collect(Collectors.toList()));

        return lectureMapstruct.lectureToLectureResponse(lecture, prices, systemTypes, subjects);
    }
}
