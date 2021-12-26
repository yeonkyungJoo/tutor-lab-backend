package com.tutor.tutorlab.modules.subject.repository;

import com.tutor.tutorlab.modules.lecture.embeddable.LearningKind;
import com.tutor.tutorlab.modules.lecture.enums.LearningKindType;
import com.tutor.tutorlab.modules.subject.vo.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class SubjectRepositoryTest {

    @Autowired
    SubjectRepository subjectRepository;

    @BeforeEach
    void init() {

        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.IT), "자바"));
        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.IT), "파이썬"));
        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.IT), "C/C++"));
        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.LANGUAGE), "영어"));
        subjectRepository.save(Subject.of(LearningKind.of(LearningKindType.LANGUAGE), "중국어"));
    }

    @Test
    void getLearningKinds() {
        List<LearningKind> learningKinds = subjectRepository.findLearningKinds();
        learningKinds.stream().forEach(
                learningKind -> System.out.println(learningKind)
        );
    }

    @Test
    void getSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        subjects.stream().forEach(
                subject -> System.out.println(subject)
        );
    }

    @Test
    void getSubjectsByLearningKind() {
        List<Subject> subjects = subjectRepository.findAllByLearningKindId(1L);
        subjects.stream().forEach(
                subject -> System.out.println(subject)
        );
    }

}