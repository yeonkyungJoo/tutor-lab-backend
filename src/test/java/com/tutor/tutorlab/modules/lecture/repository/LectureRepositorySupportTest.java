/*
package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureRepositorySupportTest extends AbstractTest {

    @Autowired
    private LectureRepositorySupport lectureRepositorySupport;

    @Transactional
    @Test
    void findLecturesBySearchTest() {
        LectureListRequest request = LectureListRequest.builder()
                .parents(Arrays.asList("개발", "프로그래밍언어"))
                .subjects(Arrays.asList("자바", "백엔드", "프론트엔드"))
                .difficulties(Arrays.asList(DifficultyType.BEGINNER))
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .isGroup(true)
                .build();

        List<Lecture> lectures = lectureRepositorySupport.findLecturesBySearch(request);

        assertThat(lectures).isNotEmpty();

        Set<Long> idSet = lectures.stream().map(lecture -> lecture.getId()).collect(Collectors.toSet());

        assertEquals(idSet.size(), lectures.size());
        lectures.forEach(lecture -> {
            lecture.getLectureSubjects().forEach(subject -> {
                assertThat(subject.getParent()).isIn(request.getParents());
                assertThat(subject.getKrSubject()).isIn(request.getSubjects());
            });
            assertThat(lecture).extracting("difficultyType").isIn(request.getDifficulties());
            lecture.getSystemTypes().forEach(systemType -> assertThat(systemType).isIn(request.getSystems()));
            lecture.getLecturePrices().forEach(lecturePrice -> assertThat(lecturePrice.getIsGroup()).isEqualTo(request.getIsGroup()));
        });
    }

}*/
