package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.TutorlabApplication;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureSubjectRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = TutorlabApplication.class)
public abstract class AbstractTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TutorRepository tutorRepository;

    protected MockMvc mockMvc;

    @BeforeEach
    private void setUp(WebApplicationContext webAppContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true), springSecurityFilterChain)
                .build();

        addDummy();
    }

    @Transactional
    public void addDummy() {
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
        user.setRole(RoleType.ROLE_TUTOR);
        userRepository.save(user);

        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects("subjects")
                .specialist(true)
                .build();

        tutorRepository.save(tutor);

        Lecture lecture = Lecture.builder()
                .tutor(tutor)
                .title("test")
                .subTitle("test")
                .introduce("소개소개소개")
                .content("test")
                .difficultyType(DifficultyType.BEGINNER)
                .systemTypes(Arrays.asList(SystemType.OFFLINE, SystemType.ONLINE))
                .lecturePrices(new ArrayList<>())
                .lectureSubjects(new ArrayList<>())
                .build();

        LecturePrice lecturePrice = LecturePrice.builder()
                .isGroup(true)
                .groupNumber(3)
                .pertimeLecture(3)
                .pertimeCost(10000L)
                .totalTime(10)
                .totalCost(300000L)
                .build();

        LectureSubject subject = LectureSubject.builder()
                .parent("개발")
                .enSubject("java")
                .krSubject("자바")
                .build();

        lecture.addPrice(lecturePrice);

        lecture.addSubject(subject);

        lectureRepository.save(lecture);
    }
}
