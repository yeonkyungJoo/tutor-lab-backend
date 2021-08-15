package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.TutorlabApplication;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import com.tutor.tutorlab.modules.subject.repository.SubjectRepository;
import com.tutor.tutorlab.modules.subject.vo.Subject;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private SubjectRepository subjectRepository;

    protected MockMvc mockMvc;

    @BeforeEach
    private void setUp(WebApplicationContext webAppContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true), springSecurityFilterChain)
                .build();

        addSubject();
        addDummy();
    }

    @Transactional
    public void addSubject() {
        List<String> parents = Arrays.asList("개발", "프로그래밍언어", "프레임워크", "etc");
        List<MockSubject> subjects = Arrays.asList(
                MockSubject.of("웹개발"),
                MockSubject.of("백엔드"),
                MockSubject.of("프론트엔드"),
                MockSubject.of("게임개발"),
                MockSubject.of("모바일개발"),
                MockSubject.of("정보/보안"));

        parents.forEach(parent -> {
            subjects.forEach(subject -> {
                Subject entity = Subject.builder()
                        .parent(parent)
                        .subject(subject.getSubject())
                        .learningKind("coding")
                        .build();
                subjectRepository.save(entity);
            });
        });
    }

    @Transactional
    public void addDummy() {
        /*User user = User.builder()
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
*/
        Tutor tutor = tutorRepository.findByUser(userRepository.findById(3L).get());

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

        LectureSubject lectureSubject = LectureSubject.builder()
                .parent("개발")
                .enSubject("java")
                .krSubject("자바")
                .build();

        lecture.addPrice(lecturePrice);

        lecture.addSubject(lectureSubject);

        lectureRepository.save(lecture);
    }

    @RequiredArgsConstructor(staticName = "of")
    @Value
    static class MockSubject {
        private final String subject;
    }
}
