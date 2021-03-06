package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.service.ChatService;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.lecture.repository.LecturePriceRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.review.repository.ReviewRepository;
import com.tutor.tutorlab.modules.review.vo.Review;
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

    @Mock
    ChatService chatService;
    @Mock
    ChatroomRepository chatroomRepository;
    @Mock
    ReviewRepository reviewRepository;

    @Mock
    CancellationRepository cancellationRepository;

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

        // ??????/?????? ?????? ???????????? ??????
        when(enrollmentRepository.findAllByTuteeIdAndLectureId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // when
        User user = Mockito.mock(User.class);
        enrollmentService.createEnrollment(user, 1L, 1L);

        // then
        verify(enrollmentRepository).save(any(Enrollment.class));

        // TODO - CHECK
        // ????????? ??????
        // ?????? ??????
        // ?????? ?????? ??????
    }

    @DisplayName("?????? ?????? ????????? ?????? ??????")
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

        // ??????/?????? ?????? ???????????? ??????
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
        // user, lectureId

        // given
        Tutee tutee = Mockito.mock(Tutee.class);
        when(tuteeRepository.findByUser(any(User.class))).thenReturn(tutee);

        Lecture lecture = Mockito.mock(Lecture.class);
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));

        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollmentRepository.findByTuteeAndLectureAndCanceledFalseAndClosedFalse(any(Tutee.class), any(Lecture.class)))
                .thenReturn(Optional.of(enrollment));

        // when
        User user = Mockito.mock(User.class);
        enrollmentService.close(user, 1L);

        // then
        verify(enrollment).close();
        // ????????? ??????
        verify(chatService).deleteChatroom(enrollment);

    }

    // user??? ?????? enrollment??? ???????????? ?????? ??????
    // ????????? ????????? ??????
    @Test
    void deleteEnrollment_noChatroom_and_noReview() {

        // given
        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollment.getId()).thenReturn(1L);
        when(chatroomRepository.findByEnrollment(enrollment))
                .thenReturn(Optional.empty());
        when(reviewRepository.findByEnrollment(enrollment)).thenReturn(null);

        // when
        enrollmentService.deleteEnrollment(enrollment);

        // then
        // ?????? ?????? ??????
        verify(cancellationRepository).deleteByEnrollment(enrollment);
        verify(enrollment).delete();
        // enrollment ???????????? ??????
        // verify(enrollmentRepository).delete(enrollment);
        verify(enrollmentRepository).deleteEnrollmentById(1L);
    }

    @Test
    void deleteEnrollment_withChatroom_and_withReview() {

        // given
        Enrollment enrollment = Mockito.mock(Enrollment.class);
        when(enrollment.getId()).thenReturn(1L);

        Chatroom chatroom = Mockito.mock(Chatroom.class);
        when(chatroomRepository.findByEnrollment(enrollment))
                .thenReturn(Optional.of(chatroom));
        Review review = Mockito.mock(Review.class);
        when(reviewRepository.findByEnrollment(enrollment)).thenReturn(review);

        // when
        enrollmentService.deleteEnrollment(enrollment);

        // then
        verify(chatroomRepository).delete(chatroom);

        verify(review).delete();
        verify(reviewRepository).delete(review);

        verify(cancellationRepository).deleteByEnrollment(enrollment);
        verify(enrollment).delete();

        verify(enrollmentRepository).deleteEnrollmentById(1L);
    }
}