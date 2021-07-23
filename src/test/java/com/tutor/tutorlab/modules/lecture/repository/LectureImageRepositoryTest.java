package com.tutor.tutorlab.modules.lecture.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LectureImageRepositoryTest {

    @Autowired
    private LectureImageRepository lectureImageRepository;

    // TODO 커스텀 메소드 생성시 테스트 케이스 추가.
}
