package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.chat.repository.ChatroomRepository;
import com.tutor.tutorlab.modules.chat.vo.Chatroom;
import com.tutor.tutorlab.modules.purchase.controller.request.EnrollmentRequest;
import com.tutor.tutorlab.modules.purchase.repository.CancellationRepository;
import com.tutor.tutorlab.modules.purchase.repository.EnrollmentRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CancellationRepository cancellationRepository;
    private final ChatroomRepository chatroomRepository;

    private final TuteeRepository tuteeRepository;
    private final LectureRepository lectureRepository;

    @Override
    public void enroll(Tutee tutee, EnrollmentRequest enrollmentRequest) {

        // TODO - 구매 프로세스

        Lecture lecture = lectureRepository.findById(enrollmentRequest.getLectureId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));

        // TODO - 구매 중복 X 체크 (UNIQUE)
        // 성공 시
        Enrollment enrollment = Enrollment.builder()
                .lecture(lecture)
                .tutee(tutee)
                .build();

        // 수강 시 채팅방 자동 생성
        Chatroom chatroom = Chatroom.builder()
                .enrollment(enrollment)
                .tutee(tutee)
                .tutor(lecture.getTutor())
                .build();
        enrollment.setChatroom(chatroom);
        // TODO - CHECK
        tutee.addEnrollment(enrollment);
    }

    @Override
    public void cancel(Tutee tutee, Long lectureId) {

        // TODO - 환불 프로세스

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 강의입니다."));
        Enrollment enrollment = enrollmentRepository.findByTuteeAndLecture(tutee, lecture);

        // TODO - Entity Listener 활용해 변경
        Cancellation cancellation = Cancellation.builder()
                .tutee(tutee)
                .lecture(lecture)
                .enrolledAt(enrollment.getCreatedAt())
                .build();
        cancellationRepository.save(cancellation);
        // 수강 취소 시 채팅방 자동 삭제 - cascade
        enrollmentRepository.delete(enrollment);
    }

    @Override
    public void close(Tutor tutor, Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 수강 내역이 없습니다."));

        // TODO - CHECK
        if (enrollment.getLecture().getTutor() != tutor) {

        }
        enrollment.close();
        // 수강 종료 시 채팅방 삭제
        chatroomRepository.deleteByEnrollment(enrollment);
    }
}
