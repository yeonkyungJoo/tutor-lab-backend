package com.tutor.tutorlab.modules.purchase.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.purchase.controller.response.EnrollmentWithSimpleLectureResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class EnrollmentQueryRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    TuteeRepository tuteeRepository;

    private JPAQueryFactory jpaQueryFactory;
    private EnrollmentQueryRepository enrollmentQueryRepository;

    @BeforeEach
    void setup() {
        jpaQueryFactory = new JPAQueryFactory(em);
        enrollmentQueryRepository = new EnrollmentQueryRepository(jpaQueryFactory);
    }

    @Test
    void findUnreviewedEnrollments() {

        // given
        assertNotNull(em);
        assertNotNull(jpaQueryFactory);
        assertNotNull(enrollmentQueryRepository);

        // when
        // then
        tuteeRepository.findAll().forEach(tutee -> {
            Page<EnrollmentWithSimpleLectureResponse> responses = enrollmentQueryRepository.findEnrollments(tutee, false, Pageable.ofSize(20));
            responses.forEach(System.out::println);
        });
    }
}