package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.configuration.auth.WithAccount;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.address.embeddable.Address;
import com.tutor.tutorlab.modules.address.repository.AddressRepository;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class LectureSearchRepositoryTest extends AbstractTest {

    @Autowired
    AddressRepository addressRepository;

    @WithAccount(NAME)
    @Test
    void findLecturesByZone() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Address zone = user.getZone();
        assertAll(
                () -> assertEquals("서울특별시", zone.getState()),
                () -> assertEquals("강남구", zone.getSiGunGu()),
                () -> assertEquals("삼성동", zone.getDongMyunLi())
        );
        tutorService.createTutor(user, tutorSignUpRequest);
        Lecture lecture = lectureService.createLecture(user, lectureCreateRequest);

        // When
        // Then
        Page<Lecture> lectures = lectureSearchRepository.findLecturesByZone(zone, PageRequest.of(0, 20));
        assertEquals(1, lectures.getTotalElements());
        lectures = lectureSearchRepository.findLecturesByZone(Address.of("서울특별시", "광진구", "능동"), PageRequest.of(0, 20));
        assertEquals(0, lectures.getTotalElements());
    }

    @WithAccount(NAME)
    @Test
    void findLecturesByZoneAndSearch() {

        // Given
        User user = userRepository.findByUsername(USERNAME).orElse(null);
        Address zone = user.getZone();
        assertAll(
                () -> assertEquals("서울특별시", zone.getState()),
                () -> assertEquals("강남구", zone.getSiGunGu()),
                () -> assertEquals("삼성동", zone.getDongMyunLi())
        );
        tutorService.createTutor(user, tutorSignUpRequest);
        Lecture lecture = lectureService.createLecture(user, lectureCreateRequest);

        // When
        // Then
        Page<Lecture> lectures = lectureSearchRepository.findLecturesByZoneAndSearch(zone, LectureListRequest.of("제목"), PageRequest.of(0, 20));
        assertEquals(1, lectures.getTotalElements());
        lectures.get().forEach(l -> System.out.println(l));
//        lectures = lectureRepositorySupport.findLecturesByZoneAndSearch(zone, LectureListRequest.of("제목2"), PageRequest.of(0, 20));
//        assertEquals(0, lectures.getTotalElements());
//        lectures = lectureRepositorySupport.findLecturesByZoneAndSearch(new Address("서울특별시", "광진구", "능동"), LectureListRequest.of("제목"), PageRequest.of(0, 20));
//        assertEquals(0, lectures.getTotalElements());

        lectures = lectureSearchRepository.findLecturesByZoneAndSearch(null, LectureListRequest.of(""), PageRequest.of(0, 20));
        assertEquals(1, lectures.getTotalElements());
    }

}
