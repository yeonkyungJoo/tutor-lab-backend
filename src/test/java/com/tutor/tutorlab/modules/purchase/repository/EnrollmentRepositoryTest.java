package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class EnrollmentRepositoryTest {

    @Autowired
    EnrollmentRepository enrollmentRepository;

    // 기존 데이터로 테스트
    @Test
    void findAllWithLectureTutor() {
        List<Enrollment> enrollments = enrollmentRepository.findAllWithLectureTutorByTutorId(1L);
        System.out.println(enrollments.size());
    }
}