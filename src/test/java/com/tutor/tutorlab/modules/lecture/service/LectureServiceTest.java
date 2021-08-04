package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.lecture.common.LectureBuilder;
import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(LectureBuilder.class)
class LectureServiceTest extends AbstractTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureBuilder lectureBuilder;

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
        AddLectureRequest.AddLecturePriceRequest price1 = lectureBuilder.getAddLecturePriceRequest(true, 3, 1000L, 3, 3000L, 10);
        AddLectureRequest.AddLecturePriceRequest price2 = lectureBuilder.getAddLecturePriceRequest(false, 3, 1000L, 3, 30000L, 10);

        AddLectureRequest.AddLectureSubjectRequest subject1 = lectureBuilder.getAddLectureSubjectRequest("개발", "java", "자바");
        AddLectureRequest.AddLectureSubjectRequest subject2 = lectureBuilder.getAddLectureSubjectRequest("개발", "javascript", "자바스크립트");

        AddLectureRequest param = AddLectureRequest.builder()
                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
                .title("제목입니다.")
                .subTitle("소제목입니다.")
                .introduce("제 소개입니다.")
                .difficulty(DifficultyType.BEGINNER)
                .content("<p>본문입니다.아라어라어라ㅣㄴㅇ</p>")
                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
                .lecturePrices(Arrays.asList(price1, price2))
                .subjects(Arrays.asList(subject1, subject2))
                .build();

        LectureResponse lectureResponse = lectureService.addLecture(param);
        assertThat(lectureResponse.getId()).isNotNull();

        assertThat(lectureResponse).extracting("thumbnail").isEqualTo(param.getThumbnailUrl());
        assertThat(lectureResponse).extracting("title").isEqualTo(param.getTitle());
        assertThat(lectureResponse).extracting("subTitle").isEqualTo(param.getSubTitle());
        assertThat(lectureResponse).extracting("introduce").isEqualTo(param.getIntroduce());
        assertThat(lectureResponse).extracting("difficultyType").isEqualTo(param.getDifficulty().getType());
        assertThat(lectureResponse).extracting("difficultyName").isEqualTo(param.getDifficulty().getName());
        assertThat(lectureResponse).extracting("content").isEqualTo(param.getContent());
        assertThat(lectureResponse.getLecturePrices()).hasSize(param.getLecturePrices().size());
        assertThat(lectureResponse.getSubjects()).hasSize(param.getSubjects().size());
    }

}