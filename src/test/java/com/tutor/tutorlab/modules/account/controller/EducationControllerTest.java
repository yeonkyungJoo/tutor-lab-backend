package com.tutor.tutorlab.modules.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.MockMvcTest;
import com.tutor.tutorlab.WithAccount;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.service.EducationService;
import com.tutor.tutorlab.modules.account.service.TutorService;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
class EducationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private TutorService tutorService;
    @Autowired
    private EducationService educationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Education 등록")
    @WithAccount("yk")
    public void newEducation() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        // TODO - CHECK : user의 RoleType이 ROLE_TUTOR가 아니므로 실패했어야 하는 테스트
        /*
        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorRepository.save(tutor);
        */

        // tutorService 테스트
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

        // When
        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();

        mockMvc.perform(post("/educations")
                .content(objectMapper.writeValueAsString(educationCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        // Then
        assertEquals(RoleType.ROLE_TUTOR, user.getRole());

        List<Education> educations = tutor.getEducations();
        assertEquals(1, educations.size());

        Education education = educations.get(0);
        assertEquals("school", education.getSchoolName());
        assertEquals("computer", education.getMajor());
        assertEquals(LocalDate.parse("2021-01-01"), education.getEntranceDate());
        assertEquals(LocalDate.parse("2021-02-01"), education.getGraduationDate());
        assertEquals(4.01, education.getScore());
        assertEquals("Bachelor", education.getDegree());

    }

    @Test
    @DisplayName("Education 수정")
    @WithAccount("yk")
    public void editEducation() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        // TODO - CHECK : user의 RoleType이 ROLE_TUTOR가 아니므로 실패했어야 하는 테스트
        /*
        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorRepository.save(tutor);

        Education education = Education.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate(LocalDate.parse("2021-01-01"))
                .graduationDate(LocalDate.parse("2021-02-01"))
                .score(4.01)
                .degree("Bachelor")
                .build();
        educationRepository.save(education);
        tutor.addEducation(education);
        */

        // tutorService, educationService 테스트
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        Education education = educationService.createEducation(user, educationCreateRequest);

        // When
        Long educationId = education.getId();
        EducationUpdateRequest educationUpdateRequest = EducationUpdateRequest.builder()
                .schoolName("school")
                .major("computer science")
                .entranceDate("2021-01-01")
                .graduationDate("2021-09-01")
                .score(4.10)
                .degree("Master")
                .build();

        mockMvc.perform(put("/educations/" + educationId)
                .content(objectMapper.writeValueAsString(educationUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        assertEquals(RoleType.ROLE_TUTOR, user.getRole());

        List<Education> educations = tutor.getEducations();
        assertEquals(1, educations.size());

        assertEquals("school", education.getSchoolName());
        assertEquals("computer science", education.getMajor());
        assertEquals(LocalDate.parse("2021-01-01"), education.getEntranceDate());
        assertEquals(LocalDate.parse("2021-09-01"), education.getGraduationDate());
        assertEquals(4.10, education.getScore());
        assertEquals("Master", education.getDegree());

    }

    @Test
    @DisplayName("Education 삭제")
    @WithAccount("yk")
    public void removeEducation() throws Exception {

        // Given
        User user = userRepository.findByName("yk");
        // TODO - CHECK : user의 RoleType이 ROLE_TUTOR가 아니므로 실패했어야 하는 테스트
        /*
        Tutor tutor = Tutor.builder()
                .user(user)
                .subjects("java,spring")
                .specialist(false)
                .build();
        tutorRepository.save(tutor);

        Education education = Education.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate(LocalDate.parse("2021-01-01"))
                .graduationDate(LocalDate.parse("2021-02-01"))
                .score(4.01)
                .degree("Bachelor")
                .build();
        educationRepository.save(education);
        tutor.addEducation(education);
        */

        // tutorService, educationService 테스트
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.builder()
                .subjects("java,spring")
                .specialist(false)
                .build();
        Tutor tutor = tutorService.createTutor(user, tutorSignUpRequest);

        EducationCreateRequest educationCreateRequest = EducationCreateRequest.builder()
                .schoolName("school")
                .major("computer")
                .entranceDate("2021-01-01")
                .graduationDate("2021-02-01")
                .score(4.01)
                .degree("Bachelor")
                .build();
        Education education = educationService.createEducation(user, educationCreateRequest);

        // When
        Long educationId = education.getId();
        mockMvc.perform(delete("/educations/" + educationId))
                .andDo(print())
                .andExpect(status().isOk());

        // Then
        assertEquals(0, tutor.getEducations().size());
        assertEquals(null, education.getTutor());
    }


    // TODO
    @Test
    @DisplayName("Education 등록 - Invalid Input")
    public void newEducation_withInvalidInput() throws Exception {

        // Given

        // When

        // Then
    }

    // TODO
    @Test
    @DisplayName("Education 등록 - 인증된 사용자 X")
    public void newEducation_withoutAuthenticatedUser() throws Exception {

        // Given

        // When

        // Then
    }

    // TODO
    @Test
    @DisplayName("Education 등록 - 튜터가 아닌 경우")
    public void newEducation_notTutor() throws Exception {

        // Given

        // When

        // Then
    }
}