package com.tutor.tutorlab.modules.lecture.repository;

import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 강의등록_테스트() throws Exception {
        User user = User.builder()
                .username("doqndnffo@gmail.com")
                .password("1")
                .name("우성환")
                .gender("M")
                .phoneNumber("1")
                .email("test")
                .bio("test")
                .zone("test")
                .role(RoleType.ROLE_TUTEE)
                .provider(null)
                .providerId(null)
                .build();
        userRepository.save(user);

        Lecture lecture = Lecture.builder()
                .user(user)
                .title("test")
                .subTitle("test")
                .content("test")
                .totalTime(10)
                .pertimeCost(1000L)
                .totalCost(10000L)
                .difficultyType(DifficultyType.ADVANCED)
                .isGroup(true)
                .groupNumber(2)
                .systemType(SystemType.ONLINE)
                .build();
        Lecture savedLecture = lectureRepository.save(lecture);

        assertThat(savedLecture).extracting("difficultyType").isEqualTo(lecture.getDifficultyType());
        assertThat(savedLecture).extracting("systemType").isEqualTo(lecture.getSystemType());
        assertThat(savedLecture).extracting("user").isEqualTo(lecture.getUser());
        assertThat(savedLecture).extracting("title").isEqualTo(lecture.getTitle());
        assertThat(savedLecture).extracting("subTitle").isEqualTo(lecture.getSubTitle());
        assertThat(savedLecture).extracting("content").isEqualTo(lecture.getContent());
        assertThat(savedLecture).extracting("totalTime").isEqualTo(lecture.getTotalTime());
        assertThat(savedLecture).extracting("pertimeCost").isEqualTo(lecture.getPertimeCost());
        assertThat(savedLecture).extracting("totalCost").isEqualTo(lecture.getTotalCost());
        assertThat(savedLecture).extracting("isGroup").isEqualTo(lecture.getIsGroup());
        assertThat(savedLecture).extracting("groupNumber").isEqualTo(lecture.getGroupNumber());
    }
}
