package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.controller.request.TuteeUpdateRequest;
import com.tutor.tutorlab.modules.account.controller.response.TuteeResponse;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TuteeServiceTest {

    @InjectMocks
    TuteeService tuteeService;

    @Mock
    TuteeRepository tuteeRepository;
    @Mock
    PickRepository pickRepository;
    @Mock
    EnrollmentRepository enrollmentRepository;
    @Mock
    UserRepository userRepository;

    @Test
    void getTuteeResponse() {
        // tuteeId

        // given
        User user = User.of("user1@email.com", "password", "user1", null, null,
                null, "user1@email.com", "user1", null, null, null, RoleType.TUTEE, null, null);
        Tutee tutee = Tutee.of(user);
        when(tuteeRepository.findById(1L)).thenReturn(Optional.of(tutee));

        // when
        TuteeResponse response = tuteeService.getTuteeResponse(1L);
        // then
        assertAll(
                () -> assertThat(response).extracting("user").extracting("email").isEqualTo("user1@email.com"),
                () -> assertThat(response).extracting("subjects").isEqualTo(tutee.getSubjects())
        );
    }

    @Test
    void updateTutee_notExist() {
        // 로그인 된 상태이므로
        // UnauthorizedException 발생

        // given
        User user = Mockito.mock(User.class);
        when(tuteeRepository.findByUser(user)).thenReturn(null);
            //.thenReturn(Optional.empty());
        // when
        // then
        assertThrows(UnauthorizedException.class,
                () -> tuteeService.updateTutee(user, any(TuteeUpdateRequest.class))
        );
    }

    @Test
    void updateTutee() {
        // user, TuteeUpdateRequest

        // given
        User user = Mockito.mock(User.class);
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tuteeRepository.findByUser(user)).thenReturn(tutee);

        // when
        TuteeUpdateRequest tuteeUpdateRequest = TuteeUpdateRequest.of("subjects");
        tuteeService.updateTutee(user, tuteeUpdateRequest);
        // then
        verify(tutee).update(tuteeUpdateRequest);
        // verify(tutee, atLeastOnce()).setSubjects(anyString());
    }

    // TODO - 순서 고려
    @Test
    void deleteTutee() {
        // 탈퇴
        // user

        // given
        User user = Mockito.mock(User.class);
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tuteeRepository.findByUser(user)).thenReturn(tutee);

        // when
        tuteeService.deleteTutee(user);

        // then

        // pick 삭제
        verify(pickRepository).deleteByTutee(tutee);

        // review 삭제

        // cancellation 삭제

        // enrollment 삭제
        // verify(enrollmentRepository).deleteByTutee(tutee);

        // chatroom 삭제

        verify(tuteeRepository).delete(tutee);

        // TODO - 배치 / 일괄 삭제
        // verify(userRepository).delete(user);
    }
}