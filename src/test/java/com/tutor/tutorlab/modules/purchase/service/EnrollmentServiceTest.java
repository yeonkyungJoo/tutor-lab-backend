package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @InjectMocks
    EnrollmentServiceImpl enrollmentService;
    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    TuteeRepository tuteeRepository;
    @Mock
    LectureRepository lectureRepository;
    @Mock
    LecturePriceRepository lecturePriceRepository;

    //@Test
    void createEnrollment() {
        // user(tutee), lectureId, lecturePriceId

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tutee.getId()).thenReturn(1L);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Lecture lecture = Mockito.mock(Lecture.class);
        when(lecture.getId()).thenReturn(1L);
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        LecturePrice lecturePrice = Mockito.mock(LecturePrice.class);
        when(lecturePrice.getId()).thenReturn(1L);
        when(lecturePriceRepository.findByLectureAndId(any(Lecture.class), anyLong())).thenReturn(Optional.of(lecturePrice));

        // 종료/취소 내역 포함해서 조회
        when(enrollmentRepository.findAllByTuteeIdAndLectureId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // when
        User user = Mockito.mock(User.class);
        enrollmentService.createEnrollment(user, 1L, 1L);

        // then
        verify(enrollmentRepository).save(any(Enrollment.class));

        // TODO - CHECK
        // 채팅방 생성
        // 알림 전송
        // 푸시 알림 전송
    }

    @DisplayName("이미 구매 이력이 있는 강의")
    @Test
    void createEnrollment_alreadyEnrolled() {

        Tutee tutee = Mockito.mock(Tutee.class);
        when(tutee.getId()).thenReturn(1L);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Lecture lecture = Mockito.mock(Lecture.class);
        when(lecture.getId()).thenReturn(1L);
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        LecturePrice lecturePrice = Mockito.mock(LecturePrice.class);
        // when(lecturePrice.getId()).thenReturn(1L);
        when(lecturePriceRepository.findByLectureAndId(any(Lecture.class), anyLong())).thenReturn(Optional.of(lecturePrice));

        // 종료/취소 내역 포함해서 조회
        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollmentRepository.findAllByTuteeIdAndLectureId(anyLong(), anyLong())).thenReturn(Optional.of(enrollment));

        // when
        // then
        User user = Mockito.mock(User.class);
        assertThrows(AlreadyExistException.class,
                () -> enrollmentService.createEnrollment(user, 1L, 1L));
    }

    @Test
    void close() {

        // given
        // when
        // then
    }

    @Test
    void deleteEnrollment() {

        // given
        // when
        // then
    }
}