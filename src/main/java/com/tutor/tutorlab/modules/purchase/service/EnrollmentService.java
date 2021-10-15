package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.springframework.data.domain.Page;

public interface EnrollmentService {

    // Page<Lecture> getLecturesOfTutee(User user, Integer page);
    Page<LectureResponse> getLectureResponsesOfTutee(User user, Integer page);

    // 강의 수강
    Enrollment createEnrollment(User user, Long lectureId, Long lecturePriceId);

    // 수강 취소
    Cancellation cancel(User user, Long lectureId);

//    void close(User user, Long lectureId, Long enrollmentId);
    void close(User user, Long lectureId);

    void deleteEnrollment(Enrollment enrollment);
}
