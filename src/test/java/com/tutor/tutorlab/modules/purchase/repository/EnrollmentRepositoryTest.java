package com.tutor.tutorlab.modules.purchase.repository;

import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class EnrollmentRepositoryTest {

    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    TutorRepository tutorRepository;

    @Test
    void findAllWithLectureTutorByTutorId() {

        // given
        Tutor tutor = tutorRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);
        Long tutorId = tutor.getId();

        // when
        List<Enrollment> enrollments = enrollmentRepository.findAllWithLectureTutorByTutorId(tutorId);
        // then
        for (Enrollment enrollment : enrollments) {
            // System.out.println(enrollment);
            assertNotNull(enrollment.getLecture().getTutor());
            assertNotNull(enrollment.getLecture().getTutor().getUser());
        }
    }
}