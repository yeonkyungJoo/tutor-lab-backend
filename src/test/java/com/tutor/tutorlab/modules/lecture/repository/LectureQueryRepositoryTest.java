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

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class LectureQueryRepositoryTest {

    private LectureQueryRepository lectureQueryRepository;

    private List<LectureCreateRequest> lectureCreateRequests;
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
    void setup() {

        lectureQueryRepository = new LectureQueryRepository(em);

        LectureCreateRequest lectureCreateRequest1 = LectureCreateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "제목1",
                "소제목1",
                "소개1",
                DifficultyType.BASIC,
                "<p>본문1</p>",
                Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE),
                Arrays.asList(LectureCreateRequest.LecturePriceCreateRequest.of(
                        false, null, 1000L, 1, 10, 10000L
                )),
                Arrays.asList(LectureCreateRequest.LectureSubjectCreateRequest.of(
                        LearningKindType.IT, "자바")
                )
        );

        LectureCreateRequest lectureCreateRequest2 = LectureCreateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "제목2",
                "소제목2",
                "소개2",
                DifficultyType.BEGINNER,
                "<p>본문2</p>",
                Arrays.asList(SystemType.ONLINE),
                Arrays.asList(LectureCreateRequest.LecturePriceCreateRequest.of(
                        true, 5, 1000L, 2, 10, 20000L
                )),
                Arrays.asList(LectureCreateRequest.LectureSubjectCreateRequest.of(
                        LearningKindType.IT, "파이썬")
                )
        );

        LectureCreateRequest lectureCreateRequest3 = LectureCreateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "제목3",
                "소제목3",
                "소개3",
                DifficultyType.INTERMEDIATE,
                "<p>본문3</p>",
                Arrays.asList(SystemType.OFFLINE),
                Arrays.asList(LectureCreateRequest.LecturePriceCreateRequest.of(
                        true, 10, 1000L, 3, 10, 30000L
                )),
                Arrays.asList(LectureCreateRequest.LectureSubjectCreateRequest.of(
                        LearningKindType.IT, "자바")
                )
        );

        LectureCreateRequest lectureCreateRequest4 = LectureCreateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "제목4",
                "소제목4",
                "소개4",
                DifficultyType.ADVANCED,
                "<p>본문4</p>",
                Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE),
                Arrays.asList(LectureCreateRequest.LecturePriceCreateRequest.of(
                        false, null, 1000L, 4, 10, 40000L
                )),
                Arrays.asList(LectureCreateRequest.LectureSubjectCreateRequest.of(
                        LearningKindType.IT, "파이썬")
                )
        );

        lectureCreateRequests.addAll(Arrays.asList(lectureCreateRequest1, lectureCreateRequest2, lectureCreateRequest3, lectureCreateRequest4));

        User user = userRepository.findAll().stream()
                .filter(u -> u.getRole().equals(RoleType.TUTOR)).findFirst()
                .orElseThrow(RuntimeException::new);
        tutor = tutorRepository.findByUser(user);

        for (LectureCreateRequest lectureCreateRequest : lectureCreateRequests) {
            lectureRepository.save(buildLecture(lectureCreateRequest, tutor));
        }
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