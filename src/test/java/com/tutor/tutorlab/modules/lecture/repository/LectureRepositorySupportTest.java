package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.address.embeddable.Address;
import com.tutor.tutorlab.modules.address.repository.AddressRepository;
import com.tutor.tutorlab.modules.lecture.common.LectureBuilder;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class LectureRepositorySupportTest {

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    LectureRepositorySupport lectureRepositorySupport;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TutorService tutorService;
    @Autowired
    LectureService lectureService;

    @BeforeEach
    void init() {

    }

    @WithAccount("yk")
    @Test
    void findLecturesByZone() {

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
        Page<Lecture> lectures = lectureRepositorySupport.findLecturesByZone(zone, PageRequest.of(0, 20));
        assertEquals(1, lectures.getTotalElements());
        lectures = lectureRepositorySupport.findLecturesByZone(new Address("서울특별시", "광진구", "능동"), PageRequest.of(0, 20));
        assertEquals(0, lectures.getTotalElements());
    }

    // @Test
    void findLecturesBySearch() {

//        LectureListRequest request = LectureListRequest.builder()
//                .parents(Arrays.asList("개발", "프로그래밍언어"))
//                .subjects(Arrays.asList("자바", "백엔드", "프론트엔드"))
//                .difficulties(Arrays.asList(DifficultyType.BEGINNER))
//                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
//                .isGroup(true)
//                .build();
//
//        List<Lecture> lectures = lectureRepositorySupport.findLecturesBySearch(request);
//        assertThat(lectures).isNotEmpty();
//
//        Set<Long> idSet = lectures.stream().map(lecture -> lecture.getId()).collect(Collectors.toSet());
//        assertEquals(idSet.size(), lectures.size());
//        lectures.forEach(lecture -> {
//            lecture.getLectureSubjects().forEach(subject -> {
//                assertThat(subject.getParent()).isIn(request.getParents());
//                assertThat(subject.getKrSubject()).isIn(request.getSubjects());
//            });
//            assertThat(lecture).extracting("difficultyType").isIn(request.getDifficulties());
//            lecture.getSystemTypes().forEach(systemType -> assertThat(systemType).isIn(request.getSystems()));
//            lecture.getLecturePrices().forEach(lecturePrice -> assertThat(lecturePrice.getIsGroup()).isEqualTo(request.getIsGroup()));
//        });
    }

}
