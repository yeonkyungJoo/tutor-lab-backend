package com.tutor.tutorlab.modules.account.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tutor.tutorlab.modules.account.controller.response.TuteeLectureResponse;
import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TutorQueryRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    TutorRepository tutorRepository;

    @Autowired
    TuteeRepository tuteeRepository;

    private TutorQueryRepository tutorQueryRepository;

    @BeforeEach
    void setup() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        tutorQueryRepository = new TutorQueryRepository(jpaQueryFactory);

        assertNotNull(em);
        assertNotNull(jpaQueryFactory);
        assertNotNull(tutorQueryRepository);
    }

    @Test
    void findTuteesOfTutor() {

        // given
        Tutor tutor = tutorRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);
        // when
        // then
        Page<TuteeSimpleResponse> result = tutorQueryRepository.findTuteesOfTutor(tutor, false, Pageable.ofSize(20));
        result.forEach(System.out::println);
    }

    @Test
    void findTuteeLecturesOfTutor() {

        // given
        Tutor tutor = tutorRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new);
        Long tuteeId = tuteeRepository.findAll().stream().findFirst()
                .orElseThrow(RuntimeException::new).getId();
        // when
        // then
        Page<TuteeLectureResponse> result = tutorQueryRepository.findTuteeLecturesOfTutor(tutor, false, tuteeId, Pageable.ofSize(20));
        result.forEach(System.out::println);
    }
}