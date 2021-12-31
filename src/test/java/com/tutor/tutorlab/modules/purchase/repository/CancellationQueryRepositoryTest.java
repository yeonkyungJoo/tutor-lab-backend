package com.tutor.tutorlab.modules.purchase.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.purchase.controller.response.CancellationResponse;
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
class CancellationQueryRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    TutorRepository tutorRepository;

    private CancellationQueryRepository cancellationQueryRepository;

    @BeforeEach
    void setup() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        cancellationQueryRepository = new CancellationQueryRepository(jpaQueryFactory);
    }

    @Test
    void findCancellationsOfTutor() {

        // given
        Tutor tutor = tutorRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);
        // when
        Page<CancellationResponse> cancellations = cancellationQueryRepository.findCancellationsOfTutor(tutor, Pageable.ofSize(20));
        // then
        for (CancellationResponse cancellation : cancellations) {
            System.out.println(cancellation);
        }
    }
}