package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.repository.CareerRepository;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private CareerRepository careerRepository;
    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    // @Transactional
    public void init() throws Exception {

    }

    @Test
    @DisplayName("Tutor 등록")
    @WithAccount("yk")
    public void newTutor() throws Exception {

        // Given
        User user = userRepository.findByName("yk");

        // When
        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();
        List<CareerCreateRequest> careers = new ArrayList<>();
        careers.add(careerCreateRequest);

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        List<EducationCreateRequest> educations = new ArrayList<>();
        educations.add(educationCreateRequest);

        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .careers(careers)
                .educations(educations)
                .specialist(false)
                .build();

        String content = objectMapper.writeValueAsString(tutorSignUpRequest);
        // System.out.println(content);
/*
        {
            "subjects":"java,spring",
            "careers":[
                {
                    "companyName":"tutorlab",
                    "duty":"engineer",
                    "startDate":"2007-12-03",
                    "endDate":"2007-12-04",
                    "present":false
                }
            ],
            "educations":[
                {
                    "schoolName":"school",
                    "major":"computer",
                    "entranceDate":"2021-01-01",
                    "graduationDate":"2021-02-01",
                    "score":4.01,
                    "degree":"Bachelor"
                }
            ],
            "specialist":false
        }
*/
        mockMvc.perform(post("/tutors")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        Tutor tutor = tutorRepository.findByUser(user);
        assertNotNull(tutor);

        // Role
        assertEquals(RoleType.ROLE_TUTOR, user.getRole());

        assertEquals(2, tutor.getSubjectList().size());
        assertEquals(1, tutor.getCareers().size());
        assertEquals(1, tutor.getEducations().size());

        Career career = careerRepository.findByTutor(tutor).get(0);
        assertEquals(career, tutor.getCareers().get(0));

        assertEquals("tutorlab", career.getCompanyName());
        assertEquals("engineer", career.getDuty());
        assertEquals(LocalDate.parse("2007-12-03"), career.getStartDate());
        assertEquals(LocalDate.parse("2007-12-04"), career.getEndDate());
        assertEquals(false, career.isPresent());

        Education education = educationRepository.findByTutor(tutor).get(0);
        assertEquals(education, tutor.getEducations().get(0));

        assertEquals("school", education.getSchoolName());
        assertEquals("computer", education.getMajor());
        assertEquals(LocalDate.parse("2021-01-01"), education.getEntranceDate());
        assertEquals(LocalDate.parse("2021-02-01"), education.getGraduationDate());
        assertEquals(4.01, education.getScore());
        assertEquals("Bachelor", education.getDegree());

    }

    // TODO
    @Test
    @DisplayName("Tutor 등록 - Invalid Input")
    public void newTutor_withInvalidInput() throws Exception {

        // Given

        // When

        // Then
    }

    // TODO
    @Test
    @DisplayName("Tutor 등록 - 인증된 사용자 X")
    public void newTutor_withoutAuthenticatedUser() throws Exception {

        // Given

        // When

        // Then
    }

    @Test
    @DisplayName("Tutor 수정")
    @WithAccount("yk")
    public void editTutor() throws Exception {

        // Given
        User user = userRepository.findByName("yk");

        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();

        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorSignUpRequest.addCareerCreateRequest(careerCreateRequest);
        tutorSignUpRequest.addEducationCreateRequest(educationCreateRequest);

        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        assertEquals(RoleType.ROLE_TUTOR, user.getRole());

        // When
        TutorUpdateRequest tutorUpdateRequest = TutorUpdateRequest.builder()
                .subjects("python")
                .specialist(true)
                .build();

        String content = objectMapper.writeValueAsString(tutorUpdateRequest);
        System.out.println(content);
    /*
        {
            "subjects":"python",
            "careers":[],
            "educations":[],
            "specialist":true
        }
    */
        mockMvc.perform(put("/tutors")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        // Tutor tutor = tutorRepository.findByUser(user);
        assertEquals("python", tutor.getSubjects());
        assertEquals(true, tutor.isSpecialist());

        assertEquals(1, tutor.getCareers().size());
        assertEquals(1, tutor.getEducations().size());
        Education education = tutor.getEducations().get(0);
        Career career = tutor.getCareers().get(0);

        assertEquals("tutorlab", career.getCompanyName());
        assertEquals("computer", education.getMajor());
    }

    @Test
    @DisplayName("Tutor 삭제")
    @WithAccount("yk")
    public void removeTutor() throws Exception {

        // Given
        User user = userRepository.findByName("yk");

        CareerCreateRequest careerCreateRequest = CareerCreateRequest.builder()
                .companyName("tutorlab")
                .duty("engineer")
                .startDate("2007-12-03")
                .endDate("2007-12-04")
                .present(false)
                .build();

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();

        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorSignUpRequest.addCareerCreateRequest(careerCreateRequest);
        tutorSignUpRequest.addEducationCreateRequest(educationCreateRequest);

        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);
        assertEquals(RoleType.ROLE_TUTOR, user.getRole());

        Career career = tutor.getCareers().get(0);
        Education education = tutor.getEducations().get(0);

        // When
        mockMvc.perform(delete("/tutors"))
                .andDo(print())
                .andExpect(status().isOk());
        // Then

        // TODO - CHECK
        assertEquals(0, tutor.getCareers().size());
        /*
            expected: <0> but was: <1>
            Expected :0
            Actual   :1
            <Click to see difference>

            org.opentest4j.AssertionFailedError: expected: <0> but was: <1>
                at org.junit.jupiter.api.AssertionUtils.fail(AssertionUtils.java:55)
                at org.junit.jupiter.api.AssertionUtils.failNotEqual(AssertionUtils.java:62)
                at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:150)
                at org.junit.jupiter.api.AssertEquals.assertEquals(AssertEquals.java:145)
                at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:510)
        */
        assertEquals(0, tutor.getEducations().size());

        assertNull(tutorRepository.findByUser(user));
        // TODO - CHECK : tutor는 영속성 컨텍스트에서 언제 사라지는가?
        assertNull(tutorRepository.findById(tutor.getId()));

        // ROLE_TUTEE로 변경
        assertEquals(RoleType.ROLE_TUTEE, user.getRole());

        assertNull(careerRepository.findByTutor(tutor));
        assertNull(careerRepository.findById(career.getId()));

        assertNull(educationRepository.findByTutor(tutor));
        assertNull(educationRepository.findById(education.getId()));

    }

    // TODO
    @Test
    @DisplayName("Tutor 삭제 - 튜터가 아닌 경우")
    public void removeTutor_notTutor() throws Exception {

        // Given

        // When

        // Then
    }
}