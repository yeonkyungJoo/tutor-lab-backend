package com.tutor.tutorlab.modules.purchase.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.purchase.controller.request.EnrollmentRequest;

public interface EnrollmentService {

    void enroll(User user, EnrollmentRequest enrollmentRequest);

    void cancel(User user, Long lectureId);
}
