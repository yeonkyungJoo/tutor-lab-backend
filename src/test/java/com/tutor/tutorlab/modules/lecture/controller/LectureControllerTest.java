package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.lecture.common.LectureBuilder;
import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.utils.JsonUtil;
import com.tutor.tutorlab.utils.MultiValueConverter;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LectureControllerTest extends AbstractTest {
    private final String BASE_URL = "/lectures";

    @Test
    void 강의한건조회_테스트() throws Exception{
        int id = 1;

        mockMvc.perform(get(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lecturePrices").exists())
                .andExpect(jsonPath("$.systemTypes").exists());
    }

    @Test
    void 강의등록_테스트() throws Exception {
        AddLectureRequest.AddLecturePriceRequest price1 = LectureBuilder.getAddLecturePriceRequest(true, 3, 1000L, 3, 3000L, 10);
        AddLectureRequest.AddLecturePriceRequest price2 = LectureBuilder.getAddLecturePriceRequest(false, 3, 1000L, 3, 30000L, 10);

        AddLectureRequest.AddLectureSubjectRequest subject1 = LectureBuilder.getAddLectureSubjectRequest("개발", "자바");
        AddLectureRequest.AddLectureSubjectRequest subject2 = LectureBuilder.getAddLectureSubjectRequest("개발", "자바스크립트");

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

        mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.toJson(param)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 강의목록검색_테스트() throws Exception {
        LectureListRequest request = LectureBuilder.getLectureListRequest(
                Arrays.asList("개발", "프로그래밍언어"),
                Arrays.asList("자바", "백엔드", "프론트엔드"),
                Arrays.asList(DifficultyType.BEGINNER),
                Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE),
                true);

        mockMvc.perform(get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .params(MultiValueConverter.convert(request)))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.result[0].systemTypes[0].type",
//                        is(in(request.getSystems().stream().map(SystemType::getType).collect(Collectors.toList())))))
//                .andExpect(jsonPath("$.result[*].systemTypes[*].type"
//                        , containsInAnyOrder(request.getSystems().stream().map(SystemType::getType).collect(Collectors.toList()))))
                .andExpect(jsonPath("$.result[*].lecturePrices[*].isGroup"
                        , hasItem(request.getIsGroup())))
//                .andExpect(jsonPath("$.result[*].systemTypes[*].type"
//                        , hasItems(request.getSystems().stream().map(SystemType::getType).collect(Collectors.toList()))))

//                .andExpect(jsonPath("$.result.difficult"))
        ;
    }
}
