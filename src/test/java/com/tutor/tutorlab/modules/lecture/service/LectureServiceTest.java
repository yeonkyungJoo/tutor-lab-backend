package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.address.embeddable.Address;
import com.tutor.tutorlab.modules.lecture.common.LectureBuilder;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureUpdateRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureSubjectRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import com.tutor.tutorlab.modules.subject.vo.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class LectureServiceTest {

    @Autowired
    LectureService lectureService;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    LectureSubjectRepository lectureSubjectRepository;
    @Autowired
    LecturePriceRepository lecturePriceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TutorService tutorService;
    @Autowired
    TutorRepository tutorRepository;

    @WithAccount("yk")
    @Test
    void 강의_등록() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest1
                = LectureBuilder.getLecturePriceCreateRequest(true, 3, 1000L, 3, 3000L, 10);
        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest2
                = LectureBuilder.getLecturePriceCreateRequest(false, 3, 1000L, 3, 30000L, 10);

        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest1
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바");
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest2
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바스크립트");

        LectureCreateRequest lectureCreateRequest = LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목")
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(lecturePriceCreateRequest1, lecturePriceCreateRequest2))
                .subjects(Arrays.asList(lectureSubjectCreateRequest1, lectureSubjectCreateRequest2))
                .build();

        lectureService.createLecture(user, lectureCreateRequest);

        // Then
        Tutor tutor = tutorRepository.findByUser(user);
        List<Lecture> lectures = lectureRepository.findByTutor(tutor);
        assertEquals(1, lectures.size());
        Lecture lecture = lectures.get(0);

        assertAll(
                () -> assertThat(lecture.getId()).isNotNull(),
                () -> assertThat(lecture).extracting("thumbnail").isEqualTo(lectureCreateRequest.getThumbnailUrl()),
                () -> assertThat(lecture).extracting("title").isEqualTo(lectureCreateRequest.getTitle()),
                () -> assertThat(lecture).extracting("subTitle").isEqualTo(lectureCreateRequest.getSubTitle()),
                () -> assertThat(lecture).extracting("introduce").isEqualTo(lectureCreateRequest.getIntroduce()),
                () -> assertThat(lecture).extracting("difficultyType").isEqualTo(lectureCreateRequest.getDifficulty()),
                () -> assertThat(lecture).extracting("content").isEqualTo(lectureCreateRequest.getContent()),
                () -> assertThat(lecture.getSystemTypes()).hasSize(lectureCreateRequest.getSystems().size()),
                () -> assertThat(lecture.getLecturePrices()).hasSize(lectureCreateRequest.getLecturePrices().size()),
                () -> assertThat(lecture.getLectureSubjects()).hasSize(lectureCreateRequest.getSubjects().size())
        );

    }

    @DisplayName("실패 - 튜티가 강의 등록")
    @WithAccount("yk")
    @Test
    void createLecture_notTutor() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);

        // When
        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest1
                = LectureBuilder.getLecturePriceCreateRequest(true, 3, 1000L, 3, 3000L, 10);
        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest2
                = LectureBuilder.getLecturePriceCreateRequest(false, 3, 1000L, 3, 30000L, 10);

        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest1
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바");
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest2
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바스크립트");

        LectureCreateRequest lectureCreateRequest = LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목")
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(lecturePriceCreateRequest1, lecturePriceCreateRequest2))
                .subjects(Arrays.asList(lectureSubjectCreateRequest1, lectureSubjectCreateRequest2))
                .build();

        // Then
        assertThrows(UnauthorizedException.class, () -> {
            lectureService.createLecture(user, lectureCreateRequest);
        });
    }

    @WithAccount("yk")
    @Test
    void updateLecture() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest1
                = LectureBuilder.getLecturePriceCreateRequest(true, 3, 1000L, 3, 3000L, 10);
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest1
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바");

        LectureCreateRequest lectureCreateRequest = LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목")
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(lecturePriceCreateRequest1))
                .subjects(Arrays.asList(lectureSubjectCreateRequest1))
                .build();
        Lecture lecture = lectureService.createLecture(user, lectureCreateRequest);
        Long lectureId = lecture.getId();

        // When
        LectureUpdateRequest.LecturePriceUpdateRequest lecturePriceUpdateRequest
                = LectureUpdateRequest.LecturePriceUpdateRequest.builder()
                .isGroup(false)
                .groupNumber(3)
                .pertimeCost(1000L)
                .pertimeLecture(3)
                .totalCost(30000L)
                .totalTime(10)
                .build();
        LectureUpdateRequest.LectureSubjectUpdateRequest lectureSubjectUpdateRequest
                = LectureUpdateRequest.LectureSubjectUpdateRequest.builder()
                .parent("개발")
                .krSubject("자바스크립트")
                .build();

        LectureUpdateRequest lectureUpdateRequest = LectureUpdateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목2")
                .subTitle("소제목2")
                .introduce("소개2")
                .difficulty(DifficultyType.INTERMEDIATE)
                .content("<p>본문2</p>")
                .systems(Arrays.asList(SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(lecturePriceUpdateRequest))
                .subjects(Arrays.asList(lectureSubjectUpdateRequest))
                .build();

        lectureService.updateLecture(user, lectureId, lectureUpdateRequest);

        // Then
        Tutor tutor = tutorRepository.findByUser(user);
        List<Lecture> lectures = lectureRepository.findByTutor(tutor);
        assertEquals(1, lectures.size());
        Lecture updatedLecture = lectures.get(0);

        assertAll(
                () -> assertThat(updatedLecture.getId()).isNotNull(),
                () -> assertThat(updatedLecture).extracting("thumbnail").isEqualTo(lectureUpdateRequest.getThumbnailUrl()),
                () -> assertThat(updatedLecture).extracting("title").isEqualTo(lectureUpdateRequest.getTitle()),
                () -> assertThat(updatedLecture).extracting("subTitle").isEqualTo(lectureUpdateRequest.getSubTitle()),
                () -> assertThat(updatedLecture).extracting("introduce").isEqualTo(lectureUpdateRequest.getIntroduce()),
                () -> assertThat(updatedLecture).extracting("difficultyType").isEqualTo(lectureUpdateRequest.getDifficulty()),
                () -> assertThat(updatedLecture).extracting("content").isEqualTo(lectureUpdateRequest.getContent()),
                () -> assertThat(updatedLecture.getSystemTypes()).hasSize(lectureUpdateRequest.getSystems().size()),
                () -> assertThat(updatedLecture.getLecturePrices()).hasSize(lectureUpdateRequest.getLecturePrices().size()),
                () -> assertThat(updatedLecture.getLectureSubjects()).hasSize(lectureUpdateRequest.getSubjects().size())
        );

        List<LectureSubject> lectureSubjects = lectureSubjectRepository.findByLecture(updatedLecture);
        assertEquals(1, lectureSubjects.size());
        LectureSubject lectureSubject = lectureSubjects.get(0);
        assertAll(
                () -> assertThat(lectureSubject.getId()).isNotNull(),
                () -> assertThat(lectureSubject).extracting("parent").isEqualTo(lectureSubjectUpdateRequest.getParent()),
                () -> assertThat(lectureSubject).extracting("krSubject").isEqualTo(lectureSubjectUpdateRequest.getKrSubject())
        );

        List<LecturePrice> lecturePrices = lecturePriceRepository.findByLecture(updatedLecture);
        assertEquals(1, lecturePrices.size());
        LecturePrice lecturePrice = lecturePrices.get(0);
        assertAll(
                () -> assertThat(lecturePrice.getId()).isNotNull(),
                () -> assertThat(lecturePrice).extracting("isGroup").isEqualTo(lecturePriceUpdateRequest.getIsGroup()),
                () -> assertThat(lecturePrice).extracting("groupNumber").isEqualTo(lecturePriceUpdateRequest.getGroupNumber()),
                () -> assertThat(lecturePrice).extracting("pertimeCost").isEqualTo(lecturePriceUpdateRequest.getPertimeCost()),
                () -> assertThat(lecturePrice).extracting("pertimeLecture").isEqualTo(lecturePriceUpdateRequest.getPertimeLecture()),
                () -> assertThat(lecturePrice).extracting("totalTime").isEqualTo(lecturePriceUpdateRequest.getTotalTime()),
                () -> assertThat(lecturePrice).extracting("totalCost").isEqualTo(lecturePriceUpdateRequest.getTotalCost())
        );

    }

    // TODO - 연관 엔티티 삭제
    @WithAccount("yk")
    @Test
    void deleteLecture() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest1
                = LectureBuilder.getLecturePriceCreateRequest(true, 3, 1000L, 3, 3000L, 10);
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest1
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바");

        LectureCreateRequest lectureCreateRequest = LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목")
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(lecturePriceCreateRequest1))
                .subjects(Arrays.asList(lectureSubjectCreateRequest1))
                .build();
        Lecture lecture = lectureService.createLecture(user, lectureCreateRequest);
        Long lectureId = lecture.getId();

        List<LectureSubject> lectureSubjects = lectureSubjectRepository.findByLecture(lecture);
        assertEquals(1, lectureSubjects.size());
        LectureSubject lectureSubject = lectureSubjects.get(0);
        Long lectureSubjectId = lectureSubject.getId();

        List<LecturePrice> lecturePrices = lecturePriceRepository.findByLecture(lecture);
        assertEquals(1, lecturePrices.size());
        LecturePrice lecturePrice = lecturePrices.get(0);
        Long lecturePriceId = lecturePrice.getId();

        // When
        lectureService.deleteLecture(user, lectureId);

        // Then
        Tutor tutor = tutorRepository.findByUser(user);
        List<Lecture> lectures = lectureRepository.findByTutor(tutor);
        assertEquals(0, lectures.size());

        assertFalse(lectureSubjectRepository.findById(lectureSubjectId).isPresent());
        assertFalse(lecturePriceRepository.findById(lecturePriceId).isPresent());

        // review
        // enrollment, cancellation
        // pick
        // chatroom
        // message

    }

    private Subject getSubject(String parent, String krSubject) {
        return Subject.builder()
                .parent(parent)
                .krSubject(krSubject)
                .learningKind("coding")
                .build();
    }

    private List<Subject> subjects = new ArrayList<>();

    @BeforeEach
    void init() {

        subjects.add(getSubject("개발", "웹개발"));
        subjects.add(getSubject("개발", "백엔드"));
        subjects.add(getSubject("개발", "프론트엔드"));
        subjects.add(getSubject("개발", "게임개발"));
        subjects.add(getSubject("개발", "모바일개발"));
        subjects.add(getSubject("개발", "정보/보안"));
        subjects.add(getSubject("프로그래밍언어", "자바스크립트"));
        subjects.add(getSubject("프로그래밍언어", "파이썬"));
        subjects.add(getSubject("프로그래밍언어", "자바"));
        subjects.add(getSubject("프로그래밍언어", "고"));
        subjects.add(getSubject("프로그래밍언어", "루비"));
        subjects.add(getSubject("프로그래밍언어", "러스트"));


    }

    @WithAccount("yk")
    @Test
    void 강의목록() {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        Address zone = user.getZone();
        assertAll(
                () -> assertEquals("서울특별시", zone.getState()),
                () -> assertEquals("강남구", zone.getSiGunGu()),
                () -> assertEquals("삼성동", zone.getDongMyunLi())
        );
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        LectureCreateRequest.LecturePriceCreateRequest lecturePriceCreateRequest1
                = LectureBuilder.getLecturePriceCreateRequest(true, 3, 1000L, 3, 3000L, 10);
        LectureCreateRequest.LectureSubjectCreateRequest lectureSubjectCreateRequest1
                = LectureBuilder.getLectureSubjectCreateRequest("개발", "자바");

        LectureCreateRequest lectureCreateRequest = LectureCreateRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목")
                .subTitle("소제목")
                .introduce("소개")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문</p>")
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(lecturePriceCreateRequest1))
                .subjects(Arrays.asList(lectureSubjectCreateRequest1))
                .build();
        Lecture lecture = lectureService.createLecture(user, lectureCreateRequest);

        // When
        // Then
        Page<LectureResponse> lectureResponses = lectureService.getLectureResponses("서울특별시 강남구", 1);
        assertEquals(1, lectureResponses.getTotalElements());
        lectureResponses.stream().forEach(lectureResponse -> {

            assertAll(
                    () -> assertEquals(lectureCreateRequest.getTitle(), lectureResponse.getTitle()),
                    () -> assertEquals(1, lectureResponse.getLecturePrices().size()),
                    () -> assertNotNull(lectureResponse.getLectureTutor()),
                    () -> assertEquals(1, lectureResponse.getLectureTutor().getLectureCount()),
                    () -> assertEquals(0, lectureResponse.getLectureTutor().getReviewCount())
                    // TODO - 리뷰 확인
            );
        });
    }
}