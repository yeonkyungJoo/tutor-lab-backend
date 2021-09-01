package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.request.EnrollmentRequest;

public interface EnrollmentService {

    // 강의 수강
    void enroll(Tutee tutee, EnrollmentRequest enrollmentRequest);

    // 수강 취소
    void cancel(Tutee tutee, Long lectureId);

    void close(Tutor tutor, Long enrollmentId);
}
