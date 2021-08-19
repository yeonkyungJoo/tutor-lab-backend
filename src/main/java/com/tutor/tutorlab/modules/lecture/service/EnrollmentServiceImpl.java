package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.EnrollmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final UserRepository userRepository;

    @Override
    public void enroll(User user, EnrollmentRequest enrollmentRequest) {

        // 구매 프로세스
        // 성공 시
        // 튜티 확인

    }
}
