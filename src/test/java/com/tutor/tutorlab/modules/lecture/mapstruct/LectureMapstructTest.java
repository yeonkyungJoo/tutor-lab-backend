package com.tutor.tutorlab.modules.lecture.mapstruct;

import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LectureMapstructTest {

    @Autowired
    private LectureMapstruct lectureMapstruct;

    @Test
    void lecturePriceToLecturePriceResponseTest() {
        LecturePrice lecturePrice = LecturePrice.builder()
                .isGroup(true)
                .groupNumber(3)
                .pertimeLecture(3)
                .pertimeCost(1000L)
                .totalTime(3)
                .totalCost(9000L)
                .build();

        LectureResponse.LecturePriceResponse response = lectureMapstruct.lecturePriceToLecturePriceResponse(lecturePrice);

        assertEquals(lecturePrice.getIsGroup(), response.getIsGroup());
        assertEquals(lecturePrice.getGroupNumber(), response.getGroupNumber());
        assertEquals(lecturePrice.getPertimeLecture(), response.getPertimeLecture());
        assertEquals(lecturePrice.getPertimeCost(), response.getPertimeCost());
        assertEquals(lecturePrice.getTotalTime(), response.getTotalTime());
        assertEquals(lecturePrice.getTotalCost(), response.getTotalCost());

    }

    @Test
    void lecturePriceListToLecturePriceResponseListTest() {
        LecturePrice lecturePrice = LecturePrice.builder()
                .isGroup(true)
                .groupNumber(3)
                .pertimeLecture(3)
                .pertimeCost(1000L)
                .totalTime(3)
                .totalCost(9000L)
                .build();
        List<LecturePrice> lecturePrices = Arrays.asList(lecturePrice);

        List<LectureResponse.LecturePriceResponse> results = lectureMapstruct.lecturePriceListToLecturePriceResponseList(lecturePrices);
        results.forEach(result -> {
            assertEquals(lecturePrice.getIsGroup(), result.getIsGroup());
            assertEquals(lecturePrice.getGroupNumber(), result.getGroupNumber());
            assertEquals(lecturePrice.getPertimeLecture(), result.getPertimeLecture());
            assertEquals(lecturePrice.getPertimeCost(), result.getPertimeCost());
            assertEquals(lecturePrice.getTotalTime(), result.getTotalTime());
            assertEquals(lecturePrice.getTotalCost(), result.getTotalCost());
        });
    }

    @Test
    void systemTypeToSystemTypeResponseTest() {
        SystemType online = SystemType.ONLINE;
        LectureResponse.SystemTypeResponse result = lectureMapstruct.systemTypeToSystemTypeResponse(online);
        assertEquals(online.getName(), result.getName());
        assertEquals(online.getType(), result.getType());
    }

    @Test
    void systemTypeListToSystemTypeResponseListTest() {
        List<SystemType> systemTypes = new ArrayList<>();
        systemTypes.add(SystemType.ONLINE);
        systemTypes.add(SystemType.OFFLINE);
        systemTypes.add(SystemType.NEGOTIABLE);

        List<LectureResponse.SystemTypeResponse> results = lectureMapstruct.systemTypeListToSystemTypeResponseList(systemTypes);
        results.forEach(result -> {
            systemTypes.remove(SystemType.find(result.getType()));
        });
        assertThat(systemTypes).hasSize(0);
    }

    @Test
    void lectureSubjectToLectureSubjectResponseTest() {
        LectureSubject lectureSubject = LectureSubject.builder()
                .parent("개발")
                .krSubject("자바")
                .build();

        LectureResponse.LectureSubjectResponse result = lectureMapstruct.lectureSubjectToLectureSubjectResponse(lectureSubject);
        assertEquals(result.getParent(), lectureSubject.getParent());
        assertEquals(result.getKrSubject(), lectureSubject.getKrSubject());
    }

    @Test
    void lectureSubjectSetToLectureSubjectResponseSetTest() {
        LectureSubject lectureSubject = LectureSubject.builder()
                .parent("개발")
                .krSubject("자바")
                .build();

        List<LectureSubject> lectureSubjects = Arrays.asList(lectureSubject, lectureSubject, lectureSubject);
        List<LectureResponse.LectureSubjectResponse> results = lectureMapstruct.lectureSubjectListToLectureSubjectResponseList(lectureSubjects);
        results.forEach(result -> {
            assertEquals(result.getParent(), lectureSubject.getParent());
            assertEquals(result.getKrSubject(), lectureSubject.getKrSubject());
        });
    }
}
