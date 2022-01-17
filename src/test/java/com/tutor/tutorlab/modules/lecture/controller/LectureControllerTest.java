package com.tutor.tutorlab.modules.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutor.tutorlab.config.controllerAdvice.RestControllerExceptionAdvice;
import com.tutor.tutorlab.configuration.AbstractTest;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureUpdateRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.service.LectureService;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.review.controller.response.ReviewResponse;
import com.tutor.tutorlab.modules.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LectureControllerTest {

    private final static String BASE_URL = "/lectures";

    @InjectMocks
    LectureController lectureController;
    @Mock
    LectureService lectureService;
    @Mock
    ReviewService reviewService;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private Tutor tutor;
    private Lecture lecture1;
    private Lecture lecture2;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(lectureController)
                .setControllerAdvice(RestControllerExceptionAdvice.class)
                .build();

        user = User.of(
                "user@email.com",
                "password",
                null, null, null, null,
                "user@email.com", "user", null,
                "서울특별시 강남구 청담동", null, RoleType.TUTOR, null, null);
        // System.out.println(AddressUtils.convertStringToEmbeddableAddress("서울특별시 강남구 청담동"));
        tutor = Tutor.of(user);

        lecture1 = Lecture.of(
                tutor,
                "title1",
                "subTitle1",
                "introduce1",
                "content1",
                DifficultyType.ADVANCED,
                Arrays.asList(SystemType.OFFLINE, SystemType.ONLINE),
                "thumbnail1"
        );

        lecture2 = Lecture.of(
                tutor,
                "title2",
                "subTitle2",
                "introduce2",
                "content2",
                DifficultyType.BEGINNER,
                Arrays.asList(SystemType.ONLINE),
                "thumbnail2"
        );
    }

    // TODO - CHECK / 파라미터 테스트
    @Test
    void getLectures() throws Exception {

        // given
        Page<LectureResponse> lectures =
                new PageImpl<>(Arrays.asList(new LectureResponse(lecture1), new LectureResponse(lecture2)), Pageable.ofSize(20), 2);
        doReturn(lectures)
                .when(lectureService).getLectureResponses(anyString(), any(LectureListRequest.class), anyInt());
        // when
        // then
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("zone", "서울특별시 강남구 청담동");
        params.add("title", "title1");
        params.add("subjects", "java,python");
        params.add("systemType", "OFFLINE");
        params.add("isGroup", "true");
        params.add("difficultyTypes", "BASIC,ADVANCED");
        params.add("page", "1");
        mockMvc.perform(get(BASE_URL)
                .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lectures)));
    }

    @Test
    void getLectures_with_noZone() throws Exception {

        // given
        Page<LectureResponse> lectures =
                new PageImpl<>(Arrays.asList(new LectureResponse(lecture1), new LectureResponse(lecture2)), Pageable.ofSize(20), 2);

//        LectureListRequest lectureListRequest =
//                LectureListRequest.of("title1", Arrays.asList("java", "python"), SystemType.OFFLINE, true, Arrays.asList(DifficultyType.BASIC, DifficultyType.ADVANCED));
//        doReturn(lectures)
//                .when(lectureService).getLectureResponses(null, lectureListRequest, 1);
        doReturn(lectures)
                .when(lectureService).getLectureResponses(any(), any(LectureListRequest.class), anyInt());
        // when
        // then
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("title", "title1");
        params.add("subjects", "java,python");
        params.add("systemType", "OFFLINE");
        params.add("isGroup", "true");
        params.add("difficultyTypes", "BASIC,ADVANCED");
        params.add("page", "1");
        mockMvc.perform(get(BASE_URL)
                .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lectures)));
    }

    @Test
    void getLecture() throws Exception {

        // given
        LectureResponse response = new LectureResponse(lecture1);
        doReturn(response)
                .when(lectureService).getLectureResponse(1L);

        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    // TODO - validation 테스트
    @Test
    void newLecture() throws Exception {

        // given
        doReturn(lecture1)
                .when(lectureService).createLecture(any(User.class), any(LectureCreateRequest.class));

        // when
        // then
        LectureCreateRequest lectureCreateRequest = AbstractTest.getLectureCreateRequest();
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lectureCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void editLecture() throws Exception {

        // given
        doNothing()
                .when(lectureService).updateLecture(any(User.class), anyLong(), any(LectureUpdateRequest.class));

        // when
        // then
        LectureUpdateRequest lectureUpdateRequest = AbstractTest.getLectureUpdateRequest();
        mockMvc.perform(put(BASE_URL + "/{lecture_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lectureUpdateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteLecture() throws Exception {

        // given
        doNothing()
                .when(lectureService).deleteLecture(any(User.class), anyLong());

        // when
        // then
        mockMvc.perform(delete(BASE_URL + "/{lecture_id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    void getReviewsOfLecture() throws Exception {

        // given
        Page<ReviewResponse> reviews = Page.empty();
        doReturn(reviews)
                .when(reviewService).getReviewResponsesOfLecture(anyLong(), anyInt());
        // when
        // then
        mockMvc.perform(get(BASE_URL + "/{lecture_id}/reviews", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reviews)));
    }

}