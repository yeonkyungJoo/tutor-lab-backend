package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LectureRepositorySupportTest extends AbstractTest {

    @Autowired
    private LectureRepositorySupport lectureRepositorySupport;

    @Test
    void findLecturesBySearchTest() {
        LectureListRequest request = LectureListRequest.builder()
                .difficulty(DifficultyType.BEGINNER)
                .build();
        List<Lecture> lectures = lectureRepositorySupport.findLecturesBySearch(request);

        assertThat(lectures).isNotEmpty();

        Set<Long> idSet = lectures.stream().map(lecture -> lecture.getId()).collect(Collectors.toSet());

        assertEquals(idSet.size(), lectures.size());
        lectures.forEach(lecture -> {
            assertThat(lecture).extracting("difficultyType").isEqualTo(request.getDifficulty());
        });
    }

}