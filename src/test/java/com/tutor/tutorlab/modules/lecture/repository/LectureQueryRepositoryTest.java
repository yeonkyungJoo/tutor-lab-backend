package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.lecture.repository.dto.LectureReviewQueryDto;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureTutorQueryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
class LectureQueryRepositoryTest {

    @Autowired
    LectureQueryRepository lectureQueryRepository;

    private List<Long> lectureIds = Arrays.asList(22L, 23L, 24L);

    @Test
    void findLectureReviewQueryDtoMap() {
        // Map<Long, List<LectureReviewQueryDto>> lectureReviewQueryDtoMap = lectureQueryRepository.findLectureReviewQueryDtoMap(lectureIds);
        Map<Long, LectureReviewQueryDto> lectureReviewQueryDtoMap = lectureQueryRepository.findLectureReviewQueryDtoMap(lectureIds);
        lectureReviewQueryDtoMap.values().forEach(System.out::println);
    }

    @Test
    void findLectureTutorQueryDtoMap() {
        Map<Long, LectureTutorQueryDto> lectureTutorQueryDtoMap = lectureQueryRepository.findLectureTutorQueryDtoMap(lectureIds);
        lectureTutorQueryDtoMap.values().forEach(System.out::println);
    }
}