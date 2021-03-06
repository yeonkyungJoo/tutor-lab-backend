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
    // TODO - CHECK

//    private LecturePrice lecturePrice = LecturePrice.of(null, true, 3, 3, 3, 1000L, 9000L);
//    private LectureSubject lectureSubject = LectureSubject.of(null, "개발", "자바");
//
//    @Autowired
//    private _LectureMapstruct lectureMapstruct;
//
//    @Test
//    void lecturePriceToLecturePriceResponseTest() {
//
//        LectureResponse.LecturePriceResponse response = lectureMapstruct.lecturePriceToLecturePriceResponse(lecturePrice);
//
//        assertEquals(lecturePrice.getIsGroup(), response.getIsGroup());
//        assertEquals(lecturePrice.getGroupNumber(), response.getGroupNumber());
//        assertEquals(lecturePrice.getPertimeLecture(), response.getPertimeLecture());
//        assertEquals(lecturePrice.getPertimeCost(), response.getPertimeCost());
//        assertEquals(lecturePrice.getTotalTime(), response.getTotalTime());
//        assertEquals(lecturePrice.getTotalCost(), response.getTotalCost());
//
//    }
//
//    @Test
//    void lecturePriceListToLecturePriceResponseListTest() {
//
//        List<LecturePrice> lecturePrices = Arrays.asList(lecturePrice);
//        List<LectureResponse.LecturePriceResponse> results = lectureMapstruct.lecturePriceListToLecturePriceResponseList(lecturePrices);
//        results.forEach(result -> {
//            assertEquals(lecturePrice.getIsGroup(), result.getIsGroup());
//            assertEquals(lecturePrice.getGroupNumber(), result.getGroupNumber());
//            assertEquals(lecturePrice.getPertimeLecture(), result.getPertimeLecture());
//            assertEquals(lecturePrice.getPertimeCost(), result.getPertimeCost());
//            assertEquals(lecturePrice.getTotalTime(), result.getTotalTime());
//            assertEquals(lecturePrice.getTotalCost(), result.getTotalCost());
//        });
//    }
//
//    @Test
//    void systemTypeToSystemTypeResponseTest() {
//        SystemType online = SystemType.ONLINE;
//        LectureResponse.SystemTypeResponse result = lectureMapstruct.systemTypeToSystemTypeResponse(online);
//        assertEquals(online.getName(), result.getName());
//        assertEquals(online.getType(), result.getType());
//    }
//
//    @Test
//    void systemTypeListToSystemTypeResponseListTest() {
//
//        List<SystemType> systemTypes = new ArrayList<>();
//        systemTypes.add(SystemType.ONLINE);
//        systemTypes.add(SystemType.OFFLINE);
//        systemTypes.add(SystemType.NEGOTIABLE);
//
//        List<LectureResponse.SystemTypeResponse> results = lectureMapstruct.systemTypeListToSystemTypeResponseList(systemTypes);
//        results.forEach(result -> {
//            systemTypes.remove(SystemType.find(result.getType()));
//        });
//        assertThat(systemTypes).hasSize(0);
//    }
//
//    @Test
//    void lectureSubjectToLectureSubjectResponseTest() {
//
//        LectureResponse.LectureSubjectResponse result = lectureMapstruct.lectureSubjectToLectureSubjectResponse(lectureSubject);
//        assertEquals(result.getParent(), lectureSubject.getParent());
//        assertEquals(result.getKrSubject(), lectureSubject.getKrSubject());
//    }
//
//    @Test
//    void lectureSubjectSetToLectureSubjectResponseSetTest() {
//
//        List<LectureSubject> lectureSubjects = Arrays.asList(lectureSubject, lectureSubject, lectureSubject);
//        List<LectureResponse.LectureSubjectResponse> results = lectureMapstruct.lectureSubjectListToLectureSubjectResponseList(lectureSubjects);
//        results.forEach(result -> {
//            assertEquals(result.getParent(), lectureSubject.getParent());
//            assertEquals(result.getKrSubject(), lectureSubject.getKrSubject());
//        });
//    }
}
