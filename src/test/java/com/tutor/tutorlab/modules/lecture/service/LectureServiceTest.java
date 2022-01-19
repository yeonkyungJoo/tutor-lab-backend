package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureUpdateRequest;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.repository.PickRepository;
import com.tutor.tutorlab.modules.purchase.service.EnrollmentService;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @InjectMocks
    LectureServiceImpl lectureService;
    @Mock
    LectureRepository lectureRepository;

    @Mock
    TutorRepository tutorRepository;
    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    EnrollmentService enrollmentService;
    @Mock
    PickRepository pickRepository;

    @Test
    void createLecture() {
        // user(tutor), lectureCreateRequest

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(any(User.class))).thenReturn(tutor);

        // when
        User user = Mockito.mock(User.class);
        LectureCreateRequest lectureCreateRequest = Mockito.mock(LectureCreateRequest.class);
        lectureService.createLecture(user, lectureCreateRequest);

        // then
        verify(lectureRepository).save(any(Lecture.class));
    }

    @DisplayName("수강 등록된 강의는 수정 불가")
    @Test
    void updateLecture_alreadyEnrolled() {
        // user(tutor), lectureId, lectureUpdateRequest

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(any(User.class))).thenReturn(tutor);

        Lecture lecture = Mockito.mock(Lecture.class);
        // when(lectureRepository.findById(any(Long.class)))
        when(lectureRepository.findByTutorAndId(any(Tutor.class), any(Long.class)))
                .thenReturn(Optional.of(lecture));

        when(enrollmentRepository.countAllByLectureId(any(Long.class))).thenReturn(2);
        // when
        // then
        User user = Mockito.mock(User.class);
        assertThrows(RuntimeException.class,
                () -> lectureService.updateLecture(user, 1L, Mockito.mock(LectureUpdateRequest.class)));
    }

    @Test
    void updateLecture() {
        // user(tutor), lectureId, lectureUpdateRequest

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(any(User.class))).thenReturn(tutor);

        Lecture lecture = Mockito.mock(Lecture.class);
        // when(lectureRepository.findById(any(Long.class)))
        when(lectureRepository.findByTutorAndId(any(Tutor.class), any(Long.class)))
                .thenReturn(Optional.of(lecture));

        when(enrollmentRepository.countAllByLectureId(any(Long.class))).thenReturn(0);

        // when
        User user = Mockito.mock(User.class);
        LectureUpdateRequest lectureUpdateRequest = Mockito.mock(LectureUpdateRequest.class);
        lectureService.updateLecture(user, 1L, lectureUpdateRequest);
        // then
        verify(lecture).update(lectureUpdateRequest);
    }

    @Test
    void deleteLecture() {
        // user, lectureId

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(any(User.class))).thenReturn(tutor);

        Lecture lecture = Mockito.mock(Lecture.class);
        // when(lectureRepository.findById(any(Long.class)))
        when(lectureRepository.findByTutorAndId(any(Tutor.class), any(Long.class)))
                .thenReturn(Optional.of(lecture));

        Enrollment closedEnrollment = Mockito.mock(Enrollment.class);
        when(closedEnrollment.isClosed()).thenReturn(true);
        // when(closedEnrollment.isCanceled()).thenReturn(false);
        Enrollment canceledEnrollment = Mockito.mock(Enrollment.class);
        // when(canceledEnrollment.isClosed()).thenReturn(false);
        when(canceledEnrollment.isCanceled()).thenReturn(true);

        List<Enrollment> enrollments = Arrays.asList(closedEnrollment, canceledEnrollment);
        when(enrollmentRepository.findAllByLectureId(anyLong())).thenReturn(enrollments);

        // when
        User user = Mockito.mock(User.class);
        lectureService.deleteLecture(user, 1L);

        // then
        verify(enrollmentService, atLeast(enrollments.size())).deleteEnrollment(any(Enrollment.class));
        // pick
        verify(pickRepository).deleteByLecture(lecture);
        verify(lectureRepository).delete(lecture);
    }

    @DisplayName("진행 중인 강의가 있는 경우 삭제 불가")
    @Test
    void deleteLecture_alreadyEnrolled() {
        // user, lectureId

        // given
        Tutor tutor = Mockito.mock(Tutor.class);
        when(tutorRepository.findByUser(any(User.class))).thenReturn(tutor);

        Lecture lecture = Mockito.mock(Lecture.class);
        // when(lectureRepository.findById(any(Long.class)))
        when(lectureRepository.findByTutorAndId(any(Tutor.class), any(Long.class)))
                .thenReturn(Optional.of(lecture));

        Enrollment closedEnrollment = Mockito.mock(Enrollment.class);
        when(closedEnrollment.isClosed()).thenReturn(true);
        // when(closedEnrollment.isCanceled()).thenReturn(false);
        Enrollment canceledEnrollment = Mockito.mock(Enrollment.class);
        // when(canceledEnrollment.isClosed()).thenReturn(false);
        when(canceledEnrollment.isCanceled()).thenReturn(true);
        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollment.isClosed()).thenReturn(false);
        when(enrollment.isCanceled()).thenReturn(false);

        when(enrollmentRepository.findAllByLectureId(anyLong())).thenReturn(
                Arrays.asList(closedEnrollment, canceledEnrollment, enrollment)
        );
        // when
        // then
        User user = Mockito.mock(User.class);
        assertThrows(RuntimeException.class,
                () -> lectureService.deleteLecture(user, 1L)
        );
    }

}
