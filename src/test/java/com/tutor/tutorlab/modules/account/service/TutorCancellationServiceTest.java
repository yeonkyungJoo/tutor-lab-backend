package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.UnauthorizedException;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.service.ChatService;
import com.tutor.tutorlab.modules.purchase.repository.CancellationQueryRepository;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorCancellationServiceTest {

    @InjectMocks
    TutorCancellationService tutorCancellationService;
    @Mock
    TutorRepository tutorRepository;
    @Mock
    CancellationRepository cancellationRepository;
    @Mock
    CancellationQueryRepository cancellationQueryRepository;

    @Mock
    ChatroomRepository chatroomRepository;
    @Mock
    @SpyBean
    ChatService chatService;

    @DisplayName("튜터가 아닌 경우")
    @Test
    void getCancellationResponses_notTutor() {
        // UnauthorizedException

        // given
        User user = Mockito.mock(User.class);
        when(tutorRepository.findByUser(user)).thenReturn(null);

        // when
        // then
        assertThrows(UnauthorizedException.class,
                () -> tutorCancellationService.getCancellationResponses(user, 20));

    }

    // 강의 환불 내역
    @Test
    void getCancellationResponses() {
        // user

        // given
        User user = Mockito.mock(User.class);
        Tutor tutor = Tutor.of(user);
        when(tutorRepository.findByUser(user)).thenReturn(tutor);

        // when
        tutorCancellationService.getCancellationResponses(user, 20);

        // then
        verify(cancellationQueryRepository).findCancellationsOfTutor(any(Tutor.class), any(PageRequest.class));
    }

    @Test
    void approve_notTutor() {
        // UnauthorizedException

        // given
        User user = Mockito.mock(User.class);
        when(tutorRepository.findByUser(user)).thenReturn(null);

        // when
        // then
        assertThrows(UnauthorizedException.class,
                () -> tutorCancellationService.approve(user, any(Long.class)));
    }

    @DisplayName("환불/취소 승인")
    @Test
    void approve() {
        // user, cancellationId

        // given
        User user = Mockito.mock(User.class);
        Tutor tutor = Tutor.of(user);
        when(tutorRepository.findByUser(user)).thenReturn(tutor);

        Enrollment enrollment = Mockito.mock(Enrollment.class);
        Cancellation cancellation = Cancellation.of(enrollment, "reason");
        when(cancellationRepository.findById(1L)).thenReturn(Optional.of(cancellation));

        // when
        tutorCancellationService.approve(user, 1L);

        // then
        verify(enrollment).cancel();
        // 채팅방 삭제
        verify(chatService).deleteChatroom(any(Enrollment.class));
        // verify(chatroomRepository).deleteByEnrollment(any(Enrollment.class));

        // TODO - 환불
    }
}