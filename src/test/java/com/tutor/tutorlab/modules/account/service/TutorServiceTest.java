package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.modules.account.controller.request.*;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Spy
    EnrollmentRepository enrollmentRepository;
    @Spy
    LectureRepository lectureRepository;
    @Spy
    LectureService lectureService;

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
        // user, tutorSignUpRequest

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

    // TODO - 도메인 로직 테스트
    @Test
    void updateTutor() {
        // user, tutorUpdateRequest

        // given
        User user = Mockito.mock(User.class);
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(user)).thenReturn(tutor);

        // when
        CareerUpdateRequest careerUpdateRequest = Mockito.mock(CareerUpdateRequest.class);
        EducationUpdateRequest educationUpdateRequest = Mockito.mock(EducationUpdateRequest.class);
        TutorUpdateRequest tutorUpdateRequest = TutorUpdateRequest.of(
                Arrays.asList(careerUpdateRequest),
                Arrays.asList(educationUpdateRequest)
        );
        tutorService.updateTutor(user, tutorUpdateRequest);

        // then
        verify(tutor).updateCareers(tutorUpdateRequest.getCareers());
        verify(tutor).updateEducations(tutorUpdateRequest.getEducations());

    }

    @Test
    void deleteTutor() {
        // user

        // given
        User user = Mockito.mock(User.class);
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(user)).thenReturn(tutor);

        // TODO - 수강 중인 강의 확인 및 삭제 TEST
        // when
        tutorService.deleteTutor(user);

        // then
        verify(tutor).quit();
        verify(tutorRepository).delete(tutor);
    }
}