package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.config.init.TestDataBuilder;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.CareerUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;

// @ExtendWith({SpringExtension.class})
// @SpringBootTest(classes = TutorlabApplication.class)
public abstract class AbstractTest {

//    @Autowired
//    private FilterChainProxy springSecurityFilterChain;

//    protected MockMvc mockMvc;
//
//    @BeforeEach
//    private void setUp(WebApplicationContext webAppContext) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
//                .addFilters(new CharacterEncodingFilter("UTF-8", true))
//                .build();
//    }

    protected static final String NAME = "yk";
    protected static final String USERNAME = "yk@email.com";

    protected final SignUpRequest signUpRequest = getSignUpRequest(NAME);
    protected final TutorSignUpRequest tutorSignUpRequest = getTutorSignUpRequest();
    protected final CareerCreateRequest careerCreateRequest = getCareerCreateRequest();
    protected final CareerUpdateRequest careerUpdateRequest = getCareerUpdateRequest();

    private SignUpRequest getSignUpRequest(String name) {
        return SignUpRequest.of(
                name + "@email.com",
                "password",
                "password",
                name,
                "FEMALE",
                null,
                null,
                null,
                name,
                null,
                "서울특별시 강남구 삼성동",
                null
        );
    }

    private TutorSignUpRequest getTutorSignUpRequest() {
        return TutorSignUpRequest.of(
                "java,spring",
                false
        );
    }

    private CareerCreateRequest getCareerCreateRequest() {
        return CareerCreateRequest.of(
                "tutorlab",
                "engineer",
                "2007-12-03",
                "2007-12-04",
                false
        );
    }

    private CareerUpdateRequest getCareerUpdateRequest() {
        return CareerUpdateRequest.of(
                "tutorlab2",
                "engineer",
                "2007-12-03",
                null,
                true
        );
    }
}
