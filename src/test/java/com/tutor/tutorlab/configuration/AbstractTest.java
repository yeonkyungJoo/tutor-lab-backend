package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.TutorlabApplication;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = TutorlabApplication.class)
public abstract class AbstractTest {

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    protected MockMvc mockMvc;

    @BeforeEach
    private void setUp(WebApplicationContext webAppContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true), springSecurityFilterChain)
                .build();

        addDummy();
    }

    private void addDummy() {
        User user = User.builder()
                .username("doqndnffo@gmail.com")
                .password("1")
                .name("우성환")
                .gender("M")
                .phoneNumber("1")
                .email("test")
                .bio("test")
                .zone("test")
                .role("test")
                .provider("dfs")
                .providerId("fsd")
                .build();

        Lecture lecture = Lecture.builder()
                .user(user)
                .title("test")
                .subTitle("test")
                .content("test")
                .totalTime(10)
                .pertimeCost(1000L)
                .totalCost(10000L)
                .difficulty(DifficultyType.ADVANCED)
                .isGroup(true)
                .groupNumber(2)
                .system(SystemType.ONLINE)
                .build();

        userRepository.save(user);
        lectureRepository.save(lecture);
    }

}
