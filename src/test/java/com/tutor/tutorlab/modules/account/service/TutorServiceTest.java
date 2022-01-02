package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.modules.account.controller.request.CareerCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.EducationCreateRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorSignUpRequest;
import com.tutor.tutorlab.modules.account.controller.request.TutorUpdateRequest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    TutorService tutorService;

    @Mock
    UserRepository userRepository;
    @Mock
    TutorRepository tutorRepository;

    @Test
    void createTutor_alreadyTutor() {

        // given
        String email = "user@email.com";
        User user = User.of(email, null, null, null, null, null,
                email, null, null, null, null, RoleType.TUTOR, null, null);
        when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));

        // when
        // then
        assertThrows(AlreadyExistException.class,
                () -> tutorService.createTutor(user, any(TutorSignUpRequest.class)));
    }

    @Test
    void createTutor() {

        // given
        String email = "user@email.com";
        User user = User.of(email, null, null, null, null, null,
                email, null, null, null, null, RoleType.TUTEE, null, null);
        when(userRepository.findByUsername(email)).thenReturn(Optional.of(user));

        // when
        CareerCreateRequest careerCreateRequest1 = Mockito.mock(CareerCreateRequest.class);
        CareerCreateRequest careerCreateRequest2 = Mockito.mock(CareerCreateRequest.class);
        EducationCreateRequest educationCreateRequest1 = Mockito.mock(EducationCreateRequest.class);
        EducationCreateRequest educationCreateRequest2 = Mockito.mock(EducationCreateRequest.class);
        TutorSignUpRequest tutorSignUpRequest = TutorSignUpRequest.of(
                Arrays.asList(careerCreateRequest1, careerCreateRequest2),
                Arrays.asList(educationCreateRequest1, educationCreateRequest2)
        );
        tutorService.createTutor(user, tutorSignUpRequest);

        // then
        verify(careerCreateRequest1).getCompanyName();
        verify(careerCreateRequest2).getCompanyName();
        verify(educationCreateRequest1).getSchoolName();
        verify(educationCreateRequest2).getSchoolName();

        verify(tutorRepository).save(any(Tutor.class));
    }

    @Test
    void updateTutor() {

        // given
        User user = Mockito.mock(User.class);
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(user))
                .thenReturn(tutor);
        // when
        tutorService.updateTutor(user, any(TutorUpdateRequest.class));
        // then
    }

    @Test
    void deleteTutor() {

        // given
        // when
        // then
    }
}