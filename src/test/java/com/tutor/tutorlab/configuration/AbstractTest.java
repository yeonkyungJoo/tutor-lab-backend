package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.config.init.TestDataBuilder;
import com.tutor.tutorlab.modules.account.controller.request.*;
import com.tutor.tutorlab.modules.address.vo.Address;

import java.util.HashMap;
import java.util.Map;

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
    protected static final String NICKNAME = NAME;
    protected static final String USERNAME = NAME + "@email.com";
    protected static final String EMAIL = USERNAME;

    protected final SignUpRequest signUpRequest = getSignUpRequest(NAME, NICKNAME);
    protected final UserUpdateRequest userUpdateRequest = getUserUpdateRequest(EMAIL, NICKNAME);

    protected TutorSignUpRequest tutorSignUpRequest = getTutorSignUpRequest(false);
    protected final CareerCreateRequest careerCreateRequest = getCareerCreateRequest();
    protected final CareerUpdateRequest careerUpdateRequest = getCareerUpdateRequest();
    protected final EducationCreateRequest educationCreateRequest = getEducationCreateRequest();
    protected final EducationUpdateRequest educationUpdateRequest = getEducationUpdateRequest();

    protected final TutorUpdateRequest tutorUpdateRequest = getTutorUpdateRequest();
    protected final TuteeUpdateRequest tuteeUpdateRequest = getTuteeUpdateRequest();

    protected final LoginRequest loginRequest = getLoginRequest(USERNAME);

    protected final Map<String, String> userInfo = getUserInfo(NAME, USERNAME);
    protected final SignUpOAuthDetailRequest signUpOAuthDetailRequest = getSignUpOAuthDetailRequest(NICKNAME);


    protected Address getAddress(String state, String siGun, String gu, String dongMyunLi) {
        return Address.of(state, siGun, gu, dongMyunLi);
    }

    private UserUpdateRequest getUserUpdateRequest(String email, String nickname) {
        return UserUpdateRequest.of(
                "FEMALE",
                null,
                "010-1234-5678",
                email,
                nickname,
                null,
                "서울특별시 강남구 삼성동",
                null
        );
    }

    private TutorUpdateRequest getTutorUpdateRequest() {
        return TutorUpdateRequest.of("python", true);
    }

    public static TutorSignUpRequest getTutorSignUpRequest(boolean include) {
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.of(
                "java,spring",
                false
        );
        if (include) {
            tutorSignUpRequest.addCareerCreateRequest(getCareerCreateRequest());
            tutorSignUpRequest.addEducationCreateRequest(getEducationCreateRequest());
        }
        return tutorSignUpRequest;
    }

//    private TutorSignUpRequest getTutorSignUpRequest() {
//        return TutorSignUpRequest.of(
//                "java,spring",
//                false
//        );
//    }

    private TuteeUpdateRequest getTuteeUpdateRequest() {
        return TuteeUpdateRequest.of("java,spring");
    }

    private SignUpOAuthDetailRequest getSignUpOAuthDetailRequest(String nickname) {
        return SignUpOAuthDetailRequest.of(
                "FEMALE",
                null,
                "010-1234-5678",
                null,
                nickname,
                "hello",
                "서울특별시 강남구 삼성동",
                null
        );
    }

    private Map<String, String> getUserInfo(String name, String username) {

        Map<String, String> userInfo = new HashMap<>();

        userInfo.put("id", "1234567890");
        userInfo.put("name", name);
        userInfo.put("email", username);
        return userInfo;
    }

    private SignUpRequest getSignUpRequest(String name, String nickname) {
        return SignUpRequest.of(
                name + "@email.com",
                "password",
                "password",
                name,
                "FEMALE",
                null,
                null,
                null,
                nickname,
                null,
                "서울특별시 강남구 삼성동",
                null
        );
    }

    public static CareerCreateRequest getCareerCreateRequest() {
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

    public static EducationCreateRequest getEducationCreateRequest() {
        return EducationCreateRequest.of(
                "school",
                "computer",
                "2021-01-01",
                "2021-02-01",
                4.01,
                "Bachelor"
        );
    }

    private EducationUpdateRequest getEducationUpdateRequest() {
        return EducationUpdateRequest.of(
                "school",
                "computer science",
                "2021-01-01",
                "2021-09-01",
                4.10,
                "Master"
        );
    }

    private LoginRequest getLoginRequest(String username) {
        return LoginRequest.of(
                username, "password"
        );
    }
}
