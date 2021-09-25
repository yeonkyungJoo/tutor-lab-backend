package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.config.response.ErrorCode;
import com.tutor.tutorlab.modules.account.controller.request.*;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.LoginService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@MockMvcTest
class TutorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TutorService tutorService;
    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    LoginService loginService;

    @Autowired
    CareerRepository careerRepository;
    @Autowired
    EducationRepository educationRepository;

    @Autowired
    ObjectMapper objectMapper;

    private List<CareerCreateRequest> careers = new ArrayList<>();
    private List<EducationCreateRequest> educations = new ArrayList<>();

    // @BeforeEach
    void init() {

        careers = new ArrayList<>();
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        careers.add(careerCreateRequest);

        educations = new ArrayList<>();
        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        educations.add(educationCreateRequest);
    }

    @WithAccount("yk")
    @Test
    void newTutor() throws Exception {

        // Given
        // When
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .careers(careers)
                .educations(educations)
                .specialist(false)
                .build();

        String content = objectMapper.writeValueAsString(tutorSignUpRequest);
        // System.out.println(content);
        mockMvc.perform(post("/tutors")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals(RoleType.TUTOR, user.getRole());
        Tutor tutor = tutorRepository.findByUser(user);
        assertNotNull(tutor);

        List<Career> careers = careerRepository.findByTutor(tutor);
        assertEquals(1, careers.size());
        List<Education> educations = educationRepository.findByTutor(tutor);
        assertEquals(1, educations.size());
    }

    @Test
    @DisplayName("Tutor 등록 - Invalid Input")
    public void newTutor_withInvalidInput() throws Exception {

        // Given
        // When
//        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
//                .companyName("tutorlab")
//                .duty("engineer")
//                .startDate("2007-12-03")
//                .endDate("")
//                .present(false)
//                .build();
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .startDate("2007-12-03")
                .endDate("")
                .present(true)
                .build();
        careers.add(careerCreateRequest);

        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .careers(careers)
                .specialist(false)
                .build();

        mockMvc.perform(post("/tutors")
                .content(objectMapper.writeValueAsString(tutorSignUpRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Invalid Input"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("Tutor 등록 - 인증된 사용자 X")
    public void newTutor_withoutAuthenticatedUser() throws Exception {

        // Given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("yk@email.com")
                .password("password")
                .passwordConfirm("password")
                .name("yk")
                .gender("FEMALE")
                .build();
        User user = loginService.signUp(signUpRequest);
        loginService.verifyEmail(user.getUsername(), user.getEmailVerifyToken());

        // When
        // Then
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();

        mockMvc.perform(post("/tutors")
                .content(objectMapper.writeValueAsString(tutorSignUpRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.getCode()));
    }

    @WithAccount("yk")
    @Test
    void Tutor_수정() throws Exception {

        // Given
        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorService.createTutor(user, tutorSignUpRequest);

        // When
        TutorUpdateRequest tutorUpdateRequest = TutorUpdateRequest.builder()
                .subjects("python")
                .specialist(true)
                .build();

        mockMvc.perform(put("/tutors")
                .content(objectMapper.writeValueAsString(tutorUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals(RoleType.TUTOR, user.getRole());
        Tutor tutor = tutorRepository.findByUser(user);
        assertEquals("python", tutor.getSubjects());
        assertTrue(tutor.isSpecialist());
    }

    // TODO - Tutor 삭제 시 연관 엔티티 전체 삭제
    @WithAccount("yk")
    @Test
    void Tutor_탈퇴() throws Exception {

        // Given
        careers = new ArrayList<>();
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        careers.add(careerCreateRequest);

        educations = new ArrayList<>();
        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        educations.add(educationCreateRequest);

        User user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .careers(careers)
                .educations(educations)
                .subjects("java,spring")
                .specialist(false)
                .build();
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        List<Long> careerIds = careerRepository.findByTutor(tutor).stream()
                .map(career -> career.getId()).collect(Collectors.toList());
        List<Long> educationIds = educationRepository.findByTutor(tutor).stream()
                .map(education -> education.getId()).collect(Collectors.toList());

        // When
        mockMvc.perform(delete("/tutors"))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        user = userRepository.findByUsername("yk@email.com").orElse(null);
        assertEquals(RoleType.TUTEE, user.getRole());

        // tutor
        assertNull(tutorRepository.findByUser(user));
        // career
        for (Long careerId : careerIds) {
            assertFalse(careerRepository.findById(careerId).isPresent());
        }
        // education
        for (Long educationId : educationIds) {
            assertFalse(educationRepository.findById(educationId).isPresent());
        }
        // chatroom
        // message
        // lecture - lecturePrice, lectureSubject
        // enrollment, pick, review


    }

    // TODO - Tutor 삭제 시 연관 엔티티 전체 삭제
    @WithAccount("yk")
    @Test
    @DisplayName("Tutor 탈퇴 - 튜터가 아닌 경우")
    public void quitTutor_notTutor() throws Exception {

        // Given

        // When

        // Then
    }

}