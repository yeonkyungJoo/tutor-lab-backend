package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.controller.request.CancellationCreateRequest;
import com.tutor.tutorlab.modules.purchase.controller.response.CancellationResponse;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancellationServiceTest {

    @InjectMocks
    CancellationServiceImpl cancellationService;
    @Mock
    CancellationRepository cancellationRepository;

    @Mock
    TuteeRepository tuteeRepository;
    @Mock
    LectureRepository lectureRepository;
    @Mock
    EnrollmentRepository enrollmentRepository;

    @DisplayName("환불 요청")
    @Test
    void cancel() {
        // user(tutee), lectureId, cancellationCreateRequest

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Lecture lecture = Mockito.mock(Lecture.class);
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        // 해당 튜티가 수강 중인 강의인지 확인
        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollmentRepository.findByTuteeAndLectureAndCanceledFalseAndClosedFalse(tutee, lecture))
                .thenReturn(Optional.of(enrollment));

        // when
        User user = Mockito.mock(User.class);
        cancellationService.cancel(user, 1L, Mockito.mock(CancellationCreateRequest.class));

        // then
        verify(cancellationRepository).save(any(Cancellation.class));
    }

}