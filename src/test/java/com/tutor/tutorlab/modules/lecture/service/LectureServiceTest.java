package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LectureServiceTest extends AbstractTest {

    @Autowired
    private LectureService lectureService;

    @Test
    void 강의조회_테스트() throws Exception {
        // given
        long id = 1L;

        // when
        LectureResponse lecture = lectureService.getLecture(id);

        //then
        assertThat(lecture).extracting("id").isEqualTo(id);
    }

    @Test
    void 강의등록_테스트() throws Exception {

    }
}