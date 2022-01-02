package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.EducationResponse;
import com.tutor.tutorlab.modules.account.enums.EducationLevelType;
import com.tutor.tutorlab.modules.account.repository.EducationRepository;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EducationServiceTest {

    @Mock
    EducationRepository educationRepository;
    @Mock
    TutorRepository tutorRepository;
    @InjectMocks
    EducationService educationService;

    private User user;
    private Tutor tutor;
    private Education education;

    @BeforeEach
    void setup() {

        assertNotNull(educationRepository);
        assertNotNull(tutorRepository);
        assertNotNull(educationService);

        user = Mockito.mock(User.class);
        tutor = Tutor.of(user);
    }

    @Test
    void getEducationResponse() {

        // given
        education = Education.of(tutor, EducationLevelType.MIDDLE, "schoolName", "major", "others");
        tutor.addEducation(education);

        when(tutorRepository.findByUser(user)).thenReturn(tutor);
        when(educationRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(education));

        // when
        EducationResponse response = educationService.getEducationResponse(user, 1L);
        // then
        assertAll(
                () -> assertThat(response).extracting("educationLevel").isEqualTo(education.getEducationLevel()),
                () -> assertThat(response).extracting("schoolName").isEqualTo(education.getSchoolName()),
                () -> assertThat(response).extracting("major").isEqualTo(education.getMajor()),
                () -> assertThat(response).extracting("others").isEqualTo(education.getOthers())
        );
    }

    @Test
    void createEducation() {
        // user, educationCreateRequest

        // given
        when(tutorRepository.findByUser(user)).thenReturn(tutor);
        when(educationRepository.save(any(Education.class))).then(AdditionalAnswers.returnsFirstArg());

        // when
        EducationCreateRequest educationCreateRequest = EducationCreateRequest.of(
                EducationLevelType.UNIVERSITY,
                "schoolName",
                "major",
                "others"
        );
        Education response = educationService.createEducation(user, educationCreateRequest);

        // then
        assertAll(
                () -> assertThat(response).extracting("tutor").isEqualTo(tutor),
                () -> assertThat(response).extracting("educationLevel").isEqualTo(educationCreateRequest.getEducationLevel()),
                () -> assertThat(response).extracting("schoolName").isEqualTo(educationCreateRequest.getSchoolName()),
                () -> assertThat(response).extracting("major").isEqualTo(educationCreateRequest.getMajor()),
                () -> assertThat(response).extracting("others").isEqualTo(educationCreateRequest.getOthers()),

                () -> assertThat(tutor.getEducations().contains(response)).isTrue()
        );

    }

    @Test
    void updateEducation() {
        // user, educationId, educationUpdateRequest

        // given
        education = Mockito.mock(Education.class);

        when(tutorRepository.findByUser(user)).thenReturn(tutor);
        when(educationRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(education));

        // when
        EducationUpdateRequest educationUpdateRequest = EducationUpdateRequest.of(
                EducationLevelType.ELEMENTARY,
                "schoolName2",
                "major2",
                "others2"
        );
        educationService.updateEducation(user, 1L, educationUpdateRequest);

        // then
        verify(education).update(educationUpdateRequest);
        // verify(education, atLeastOnce()).setEducationLevel(educationUpdateRequest.getEducationLevel());
    }

    @Test
    void deleteEducation() {
        // user, educationId

        // given
        education = Education.of(tutor, EducationLevelType.MIDDLE, "schoolName", "major", "others");
        tutor.addEducation(education);

        when(tutorRepository.findByUser(user)).thenReturn(tutor);
        when(educationRepository.findByTutorAndId(tutor, 1L)).thenReturn(Optional.of(education));

        // when
        educationService.deleteEducation(user, 1L);

        // then
        assertThat(education).extracting("tutor").isNull();
        assertThat(tutor.getEducations().contains(education)).isFalse();
        verify(educationRepository, atLeastOnce()).delete(education);
    }
}