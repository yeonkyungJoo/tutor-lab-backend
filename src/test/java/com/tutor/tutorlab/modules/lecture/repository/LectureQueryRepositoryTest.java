package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.LearningKindType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureReviewQueryDto;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureTutorQueryDto;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class LectureQueryRepositoryTest {

    private LectureQueryRepository lectureQueryRepository;
    private Tutor tutor;

    @Autowired
    EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    LectureRepository lectureRepository;

    @BeforeEach
    void init() {

        assertNotNull(em);
        lectureQueryRepository = new LectureQueryRepository(em);

        User user = userRepository.findAll().stream()
                .filter(u -> u.getRole().equals(RoleType.TUTOR)).findFirst()
                .orElseThrow(RuntimeException::new);
        tutor = tutorRepository.findByUser(user);
    }

    private Lecture buildLecture(LectureCreateRequest lectureCreateRequest, Tutor tutor) {

        Lecture lecture = Lecture.of(
                tutor,
                lectureCreateRequest.getTitle(),
                lectureCreateRequest.getSubTitle(),
                lectureCreateRequest.getIntroduce(),
                lectureCreateRequest.getContent(),
                lectureCreateRequest.getDifficulty(),
                lectureCreateRequest.getSystems(),
                lectureCreateRequest.getThumbnailUrl()
        );

        for (LectureCreateRequest.LecturePriceCreateRequest lecturePriceRequest : lectureCreateRequest.getLecturePrices()) {
            lecture.addPrice(buildLecturePrice(lecturePriceRequest));
        }

        for (LectureCreateRequest.LectureSubjectCreateRequest subjectRequest : lectureCreateRequest.getSubjects()) {
            lecture.addSubject(buildLectureSubject(subjectRequest));
        }

        return lecture;
    }

    private LectureSubject buildLectureSubject(LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest) {
        return LectureSubject.of(
                null,
                lectureSubjectCreateRequest.getLearningKindId(),
                lectureSubjectCreateRequest.getLearningKind(),
                lectureSubjectCreateRequest.getKrSubject()
        );
    }

    private LecturePrice buildLecturePrice(LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest) {
        return LecturePrice.of(
                null,
                lecturePriceCreateRequest.getIsGroup(),
                lecturePriceCreateRequest.getGroupNumber(),
                lecturePriceCreateRequest.getTotalTime(),
                lecturePriceCreateRequest.getPertimeLecture(),
                lecturePriceCreateRequest.getPertimeCost(),
                lecturePriceCreateRequest.getTotalCost()
        );
    }

    @Test
    void findLectureReviewQueryDtoMap() {
//        Map<Long, LectureReviewQueryDto> lectureReviewQueryDtoMap = lectureQueryRepository.findLectureReviewQueryDtoMap(lectureIds);
//        lectureReviewQueryDtoMap.values().forEach(System.out::println);
    }

    @Test
    void findLectureTutorQueryDtoMap() {
//        Map<Long, LectureTutorQueryDto> lectureTutorQueryDtoMap = lectureQueryRepository.findLectureTutorQueryDtoMap(lectureIds);
//        lectureTutorQueryDtoMap.values().forEach(System.out::println);
    }
}