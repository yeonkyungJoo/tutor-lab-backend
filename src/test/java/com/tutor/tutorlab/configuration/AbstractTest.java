package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.modules.account.controller.request.*;
import com.tutor.tutorlab.modules.account.enums.EducationLevelType;
import com.tutor.tutorlab.modules.account.repository.*;
import com.tutor.tutorlab.modules.account.service.*;
import com.tutor.tutorlab.modules.address.vo.Address;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureUpdateRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.LearningKindType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureSearchRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureSubjectRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.notification.repository.NotificationRepository;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
import com.tutor.tutorlab.modules.purchase.controller.request.CancellationCreateRequest;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.service.CancellationService;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.purchase.service.PickService;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TuteeReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewCreateRequest;
import com.tutor.tutorlab.modules.review.controller.request.TutorReviewUpdateRequest;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
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


    @Autowired
    protected LoginService loginService;

    @Autowired
    protected UserService userService;
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TuteeService tuteeService;
    @Autowired
    protected TuteeRepository tuteeRepository;

    @Autowired
    protected CareerService careerService;
    @Autowired
    protected CareerRepository careerRepository;
    @Autowired
    protected EducationService educationService;
    @Autowired
    protected EducationRepository educationRepository;
    @Autowired
    protected TutorService tutorService;
    @Autowired
    protected TutorRepository tutorRepository;

    @Autowired
    protected LectureService lectureService;
    @Autowired
    protected LectureRepository lectureRepository;
    @Autowired
    protected LectureSearchRepository lectureSearchRepository;
    @Autowired
    protected LectureSubjectRepository lectureSubjectRepository;
    @Autowired
    protected LecturePriceRepository lecturePriceRepository;

    @Autowired
    protected PickService pickService;
    @Autowired
    protected PickRepository pickRepository;

    @Autowired
    protected EnrollmentService enrollmentService;
    @Autowired
    protected EnrollmentRepository enrollmentRepository;
    @Autowired
    protected CancellationService cancellationService;
    @Autowired
    protected CancellationRepository cancellationRepository;

    @Autowired
    protected ChatroomRepository chatroomRepository;

    @Autowired
    protected NotificationService notificationService;
    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected ReviewService reviewService;
    @Autowired
    protected ReviewRepository reviewRepository;


    protected static final String NAME = "yk";
    protected static final String NICKNAME = NAME;
    protected static final String USERNAME = NAME + "@email.com";
    protected static final String EMAIL = USERNAME;

    protected final SignUpRequest signUpRequest = getSignUpRequest(NAME, NICKNAME);
    protected final UserUpdateRequest userUpdateRequest = getUserUpdateRequest(EMAIL, NICKNAME);

    protected TutorSignUpRequest tutorSignUpRequest = getTutorSignUpRequest();
    protected final CareerCreateRequest careerCreateRequest = getCareerCreateRequest();
    protected final CareerUpdateRequest careerUpdateRequest = getCareerUpdateRequest();
    protected final EducationCreateRequest educationCreateRequest = getEducationCreateRequest();
    protected final EducationUpdateRequest educationUpdateRequest = getEducationUpdateRequest();

    protected final TutorUpdateRequest tutorUpdateRequest = getTutorUpdateRequest();
    protected final TuteeUpdateRequest tuteeUpdateRequest = getTuteeUpdateRequest();

    protected final LoginRequest loginRequest = getLoginRequest(USERNAME);

    protected final Map<String, String> userInfo = getUserInfo(NAME, USERNAME);
    protected final SignUpOAuthDetailRequest signUpOAuthDetailRequest = getSignUpOAuthDetailRequest(NICKNAME);

    protected final LectureCreateRequest lectureCreateRequest = getLectureCreateRequest();
    protected final LectureUpdateRequest.LecturePriceUpdateRequest lecturePriceUpdateRequest = getLecturePriceUpdateRequest();
    protected final LectureUpdateRequest.LectureSubjectUpdateRequest lectureSubjectUpdateRequest = getLectureSubjectUpdateRequest();
    protected final LectureUpdateRequest lectureUpdateRequest = getLectureUpdateRequest();

    protected final TuteeReviewCreateRequest tuteeReviewCreateRequest = getTuteeReviewCreateRequest();
    protected final TuteeReviewUpdateRequest tuteeReviewUpdateRequest = getTuteeReviewUpdateRequest();

    protected final TutorReviewCreateRequest tutorReviewCreateRequest = getTutorReviewCreateRequest();
    protected final TutorReviewUpdateRequest tutorReviewUpdateRequest = getTutorReviewUpdateRequest();

    // TODO - ??????
    protected final CancellationCreateRequest cancellationCreateRequest = getCancellationCreateRequest();

    public static CancellationCreateRequest getCancellationCreateRequest() {
        return CancellationCreateRequest.of("?????? ????????????");
    }

//    LectureCreateRequest lectureCreateRequest1 = LectureCreateRequest.of(
//            "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
//            "??????1",
//            "?????????1",
//            "??????1",
//            DifficultyType.BASIC,
//            "<p>??????1</p>",
//            Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE),
//            Arrays.asList(LectureCreateRequest.LecturePriceCreateRequest.of(
//                    false, null, 1000L, 1, 10, 10000L
//            )),
//            Arrays.asList(LectureCreateRequest.LectureSubjectCreateRequest.of(
//                    LearningKindType.IT, "??????")
//            )
//    );
//
//    LectureCreateRequest lectureCreateRequest2 = LectureCreateRequest.of(
//            "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
//            "??????2",
//            "?????????2",
//            "??????2",
//            DifficultyType.BEGINNER,
//            "<p>??????2</p>",
//            Arrays.asList(SystemType.ONLINE),
//            Arrays.asList(LectureCreateRequest.LecturePriceCreateRequest.of(
//                    true, 5, 1000L, 2, 10, 20000L
//            )),
//            Arrays.asList(LectureCreateRequest.LectureSubjectCreateRequest.of(
//                    LearningKindType.IT, "?????????")
//            )
//    );
//
//    LectureCreateRequest lectureCreateRequest3 = LectureCreateRequest.of(
//            "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
//            "??????3",
//            "?????????3",
//            "??????3",
//            DifficultyType.INTERMEDIATE,
//            "<p>??????3</p>",
//            Arrays.asList(SystemType.OFFLINE),
//            Arrays.asList(LectureCreateRequest.LecturePriceCreateRequest.of(
//                    true, 10, 1000L, 3, 10, 30000L
//            )),
//            Arrays.asList(LectureCreateRequest.LectureSubjectCreateRequest.of(
//                    LearningKindType.IT, "??????")
//            )
//    );
//
//    LectureCreateRequest lectureCreateRequest4 = LectureCreateRequest.of(
//            "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
//            "??????4",
//            "?????????4",
//            "??????4",
//            DifficultyType.ADVANCED,
//            "<p>??????4</p>",
//            Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE),
//            Arrays.asList(LectureCreateRequest.LecturePriceCreateRequest.of(
//                    false, null, 1000L, 4, 10, 40000L
//            )),
//            Arrays.asList(LectureCreateRequest.LectureSubjectCreateRequest.of(
//                    LearningKindType.IT, "?????????")
//            )
//    );


    protected Address getAddress(String state, String siGun, String gu, String dongMyunLi) {
        return Address.of(state, siGun, gu, dongMyunLi);
    }

    public static UserUpdateRequest getUserUpdateRequest(String email, String nickname) {
        return UserUpdateRequest.of(
                "FEMALE",
                null,
                "010-1234-5678",
                email,
                nickname,
                null,
                "??????????????? ????????? ?????????",
                null
        );
    }

    public static TutorUpdateRequest getTutorUpdateRequest() {
        return TutorUpdateRequest.of(
                Arrays.asList(getCareerUpdateRequest()),
                Arrays.asList(getEducationUpdateRequest())
        );
    }

    public static TutorSignUpRequest getTutorSignUpRequest() {
        return TutorSignUpRequest.of(
                Arrays.asList(getCareerCreateRequest()),
                Arrays.asList(getEducationCreateRequest())
        );
    }

    public static TuteeUpdateRequest getTuteeUpdateRequest() {
        return TuteeUpdateRequest.of("java,spring");
    }

    public static SignUpOAuthDetailRequest getSignUpOAuthDetailRequest(String nickname) {
        return SignUpOAuthDetailRequest.of(
                "FEMALE",
                null,
                "010-1234-5678",
                null,
                nickname,
                "hello",
                "??????????????? ????????? ?????????",
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

    public static SignUpRequest getSignUpRequest(String name, String nickname) {
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
                "??????????????? ????????? ?????????",
                null
        );
    }

    public static CareerCreateRequest getCareerCreateRequest() {
        return CareerCreateRequest.of(
                "engineer",
                "tutorlab",
                "",
                ""
        );
    }

    public static CareerUpdateRequest getCareerUpdateRequest() {
        return CareerUpdateRequest.of(
                "engineer",
                "tutorlab2",
                "designer",
                "computer"
        );
    }

    public static EducationCreateRequest getEducationCreateRequest() {
        return EducationCreateRequest.of(
                EducationLevelType.UNIVERSITY,
                "school",
                "computer",
                ""
        );
    }

    public static EducationUpdateRequest getEducationUpdateRequest() {
        return EducationUpdateRequest.of(
                EducationLevelType.UNIVERSITY,
                "school",
                "computer science",
                "design"
        );
    }

    private LoginRequest getLoginRequest(String username) {
        return LoginRequest.of(
                username, "password"
        );
    }

    public static LectureCreateRequest.LecturePriceCreateRequest getLecturePriceCreateRequest() {
        return LectureCreateRequest.LecturePriceCreateRequest.of(
                true, 3, 1000L, 3, 10, 3000L
        );
    }

    public static LectureCreateRequest.LectureSubjectCreateRequest getLectureSubjectCreateRequest() {
        return LectureCreateRequest.LectureSubjectCreateRequest.of(LearningKindType.IT, "??????");
    }

    public static LectureCreateRequest getLectureCreateRequest() {
        return LectureCreateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "??????",
                "?????????",
                "??????",
                DifficultyType.BEGINNER,
                "<p>??????</p>",
                Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE),
                Arrays.asList(getLecturePriceCreateRequest()),
                Arrays.asList(getLectureSubjectCreateRequest())
        );
    }

    public static LectureUpdateRequest.LecturePriceUpdateRequest getLecturePriceUpdateRequest() {
        return LectureUpdateRequest.LecturePriceUpdateRequest.of(
                false, 3, 1000L, 3, 10, 30000L
        );
    }

    public static LectureUpdateRequest.LectureSubjectUpdateRequest getLectureSubjectUpdateRequest() {
        return LectureUpdateRequest.LectureSubjectUpdateRequest.of(LearningKindType.IT, "??????????????????");
    }

    public static LectureUpdateRequest getLectureUpdateRequest() {
        return LectureUpdateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "????????????",
                "???????????????",
                "????????????",
                DifficultyType.INTERMEDIATE,
                "<p>????????????</p>",
                Arrays.asList(SystemType.OFFLINE),
                Arrays.asList(getLecturePriceUpdateRequest()),
                Arrays.asList(getLectureSubjectUpdateRequest())
        );
    }

    public static TuteeReviewCreateRequest getTuteeReviewCreateRequest() {
        return TuteeReviewCreateRequest.of(
                5, "?????????"
        );
    }

    public static TuteeReviewUpdateRequest getTuteeReviewUpdateRequest() {
        return TuteeReviewUpdateRequest.of(
                3, "????????????"
        );
    }

    public static TutorReviewCreateRequest getTutorReviewCreateRequest() {
        return TutorReviewCreateRequest.of("???????????????");
    }

    public static TutorReviewUpdateRequest getTutorReviewUpdateRequest() {
        return TutorReviewUpdateRequest.of("?????? ???????????????");
    }
}
