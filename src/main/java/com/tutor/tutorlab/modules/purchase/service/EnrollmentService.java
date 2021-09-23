package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.springframework.data.domain.Page;

public interface EnrollmentService {

    Page<Lecture> getLecturesOfTutee(User user, Integer page);

    // 강의 수강
    void enroll(User user, Long lectureId);

    // 수강 취소
    void cancel(User user, Long lectureId);

    void close(User user, Long lectureId, Long enrollmentId);
}
