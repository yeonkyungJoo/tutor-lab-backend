package com.tutor.tutorlab.modules.lecture.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.address.embeddable.Address;
import com.tutor.tutorlab.modules.address.repository.AddressRepository;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.mapstruct._LectureMapstruct;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class LectureSearchRepositoryTest {
    // TODO - 테스트 세분화
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    EntityManager em;

    private LectureSearchRepository lectureSearchRepository;

    @BeforeEach
    void setup() {

        assertNotNull(addressRepository);

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        lectureSearchRepository = new LectureSearchRepository(jpaQueryFactory);
    }

//    @WithAccount(NAME)
//    @Test
//    void findLecturesByZone() {
//
//        // Given
//        User user = userRepository.findByUsername(USERNAME).orElse(null);
//        Address zone = user.getZone();
//        assertAll(
//                () -> assertEquals("서울특별시", zone.getState()),
//                () -> assertEquals("강남구", zone.getSiGunGu()),
//                () -> assertEquals("삼성동", zone.getDongMyunLi())
//        );
//        tutorService.createTutor(user, tutorSignUpRequest);
//        Lecture lecture = lectureService.createLecture(user, lectureCreateRequest);
//
//        // When
//        // Then
//        Page<Lecture> lectures = lectureSearchRepository.findLecturesByZone(zone, PageRequest.of(0, 20));
//        assertEquals(1, lectures.getTotalElements());
//        lectures = lectureSearchRepository.findLecturesByZone(Address.of("서울특별시", "광진구", "능동"), PageRequest.of(0, 20));
//        assertEquals(0, lectures.getTotalElements());
//    }

    @Test
    void findLecturesByZoneAndSearch() {

        // given
        Tutor tutor = tutorRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);

        Address zone = tutor.getUser().getZone();

        Lecture _lecture = lectureRepository.findAll().stream()
                .filter(l -> l.getTutor().equals(tutor)).findFirst()
                .orElseThrow(RuntimeException::new);
        LectureListRequest listRequest = LectureListRequest.of(
                _lecture.getTitle(),
                Arrays.asList(_lecture.getLectureSubjects().get(0).getKrSubject()),
                _lecture.getSystemTypes().get(0),
                _lecture.getLecturePrices().get(0).getIsGroup(),
                Arrays.asList(_lecture.getDifficultyType())
        );

        // when
        Page<Lecture> lectures = lectureSearchRepository.findLecturesByZoneAndSearch(zone, listRequest, PageRequest.ofSize(20));
        // then
        assertThat(lectures.getTotalElements()).isGreaterThanOrEqualTo(1);
//        for (Lecture lecture : lectures) {
//            System.out.println(lecture);
//        }
    }

}
