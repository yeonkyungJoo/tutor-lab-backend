package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.controller.response.TuteeSimpleResponse;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class TutorQueryRepositoryIntegrationTest {

    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    TutorQueryRepository tutorQueryRepository;

    @Test
    void findTuteesOfTutor() {

        Tutor tutor = tutorRepository.findById(1L).orElse(null);

        Page<TuteeSimpleResponse> tuteeSimpleResponses = tutorQueryRepository.findTuteesOfTutor(tutor, true, PageRequest.of(0, 10, Sort.by("id").ascending()));
        tuteeSimpleResponses.stream().forEach(System.out::println);
    }

//    @Test
//    void findTuteeLecturesOfTutor() {
//
//        Tutor tutor = tutorRepository.findById(1L).orElse(null);
//        tutorQueryRepository.findTuteeLecturesOfTutor(tutor, 1L, PageRequest.of(0, 10, Sort.by("id").ascending()));
//
//    }


}