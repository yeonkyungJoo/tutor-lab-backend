package com.tutor.tutorlab.configuration;

import com.tutor.tutorlab.modules.account.controller.request.*;
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
import com.tutor.tutorlab.modules.lecture.repository.LectureRepositorySupport;
import com.tutor.tutorlab.modules.lecture.repository.LectureSubjectRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.notification.repository.NotificationRepository;
import com.tutor.tutorlab.modules.notification.service.NotificationService;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
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
    protected LectureRepositorySupport lectureRepositorySupport;
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

    protected final LectureCreateRequest lectureCreateRequest = getLectureCreateRequest();
    protected final LectureUpdateRequest.LecturePriceUpdateRequest lecturePriceUpdateRequest = getLecturePriceUpdateRequest();
    protected final LectureUpdateRequest.LectureSubjectUpdateRequest lectureSubjectUpdateRequest = getLectureSubjectUpdateRequest();
    protected final LectureUpdateRequest lectureUpdateRequest = getLectureUpdateRequest();

    protected final TuteeReviewCreateRequest tuteeReviewCreateRequest = getTuteeReviewCreateRequest();
    protected final TuteeReviewUpdateRequest tuteeReviewUpdateRequest = getTuteeReviewUpdateRequest();

    protected final TutorReviewCreateRequest tutorReviewCreateRequest = getTutorReviewCreateRequest();
    protected final TutorReviewUpdateRequest tutorReviewUpdateRequest = getTutorReviewUpdateRequest();

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

    protected SignUpRequest getSignUpRequest(String name, String nickname) {
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

    public static LectureCreateRequest.LecturePriceCreateRequest getLecturePriceCreateRequest() {
        return LectureCreateRequest.LecturePriceCreateRequest.of(
                true, null, 1000L, 3, 10, 3000L
        );
    }

    public static LectureCreateRequest.LectureSubjectCreateRequest getLectureSubjectCreateRequest() {
        return LectureCreateRequest.LectureSubjectCreateRequest.of(LearningKindType.IT, "자바");
    }

    public static LectureCreateRequest getLectureCreateRequest() {
        return LectureCreateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "제목",
                "소제목",
                "소개",
                DifficultyType.BEGINNER,
                "<p>본문</p>",
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
        return LectureUpdateRequest.LectureSubjectUpdateRequest.of(LearningKindType.IT, "자바스크립트");
    }

    public static LectureUpdateRequest getLectureUpdateRequest() {
        return LectureUpdateRequest.of(
                "https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c",
                "제목수정",
                "소제목수정",
                "소개수정",
                DifficultyType.INTERMEDIATE,
                "<p>본문수정</p>",
                Arrays.asList(SystemType.OFFLINE),
                Arrays.asList(getLecturePriceUpdateRequest()),
                Arrays.asList(getLectureSubjectUpdateRequest())
        );
    }

    private TuteeReviewCreateRequest getTuteeReviewCreateRequest() {
        return TuteeReviewCreateRequest.of(
                5, "좋아요"
        );
    }

    private TuteeReviewUpdateRequest getTuteeReviewUpdateRequest() {
        return TuteeReviewUpdateRequest.of(
                3, "별로에요"
        );
    }

    private TutorReviewCreateRequest getTutorReviewCreateRequest() {
        return TutorReviewCreateRequest.of("감사합니다");
    }

    private TutorReviewUpdateRequest getTutorReviewUpdateRequest() {
        return TutorReviewUpdateRequest.of("리뷰 감사합니다");
    }
}
