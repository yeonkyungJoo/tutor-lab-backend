package com.tutor.tutorlab.modules.review.repository;

import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
class ReviewQueryRepositoryTest {

    @Autowired
    ReviewQueryRepository reviewQueryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LectureRepository lectureRepository;

}