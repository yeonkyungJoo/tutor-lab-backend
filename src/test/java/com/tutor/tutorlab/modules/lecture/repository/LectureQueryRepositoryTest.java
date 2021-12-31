package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureReviewQueryDto;
import com.tutor.tutorlab.modules.lecture.repository.dto.LectureTutorQueryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Test
    void findLectureReviewQueryDtoMap() {

        // given
        List<Long> lectureIds = lectureRepository.findAll().stream().map(BaseEntity::getId).collect(Collectors.toList());

        // when, then
        Map<Long, LectureReviewQueryDto> lectureReviewQueryDtoMap
                = lectureQueryRepository.findLectureReviewQueryDtoMap(lectureIds);
        lectureReviewQueryDtoMap.values().forEach(System.out::println);
    }

    @Test
    void findLectureTutorQueryDtoMap() {

        // given
        List<Long> lectureIds = lectureRepository.findAll().stream().map(BaseEntity::getId).collect(Collectors.toList());

        // when, then
        Map<Long, LectureTutorQueryDto> lectureTutorQueryDtoMap
                = lectureQueryRepository.findLectureTutorQueryDtoMap(lectureIds);
        lectureTutorQueryDtoMap.values().forEach(System.out::println);
    }
}