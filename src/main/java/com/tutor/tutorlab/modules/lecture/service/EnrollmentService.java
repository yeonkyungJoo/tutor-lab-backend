package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.EnrollmentRequest;

public interface EnrollmentService {

    public void enroll(User user, EnrollmentRequest enrollmentRequest);
}
