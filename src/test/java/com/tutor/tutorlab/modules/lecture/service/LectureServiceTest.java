//package com.tutor.tutorlab.modules.lecture.service;
//
//import com.tutor.tutorlab.configuration.AbstractTest;
//import com.tutor.tutorlab.modules.account.repository.UserRepository;
//import com.tutor.tutorlab.modules.account.vo.User;
//import com.tutor.tutorlab.modules.lecture.common.LectureBuilder;
//import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
//import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
//import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
//import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
//import com.tutor.tutorlab.modules.lecture.enums.SystemType;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Import;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@Import(LectureBuilder.class)
//class LectureServiceTest extends AbstractTest {
//
//    @Autowired
//    private LectureService lectureService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void 강의조회_테스트() throws Exception {
//        // given
//        long id = 1L;
//
//        // when
//        LectureResponse lecture = lectureService.getLecture(id);
//
//        //then
//        assertThat(lecture).extracting("id").isEqualTo(id);
//    }
//
//    @Test
//    void 강의등록_테스트() throws Exception {
//        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("테스트 실패, 유저 없음."));
//
//        LectureCreateRequest.AddLecturePriceRequest price1 = LectureBuilder.getAddLecturePriceRequest(true, 3, 1000L, 3, 3000L, 10);
//        LectureCreateRequest.AddLecturePriceRequest price2 = LectureBuilder.getAddLecturePriceRequest(false, 3, 1000L, 3, 30000L, 10);
//
//        LectureCreateRequest.AddLectureSubjectRequest subject1 = LectureBuilder.getAddLectureSubjectRequest("개발", "java", "자바");
//        LectureCreateRequest.AddLectureSubjectRequest subject2 = LectureBuilder.getAddLectureSubjectRequest("개발", "javascript", "자바스크립트");
//
//        LectureCreateRequest param = LectureCreateRequest.builder()
//                .thumbnailUrl("https://tutorlab.s3.ap-northeast-2.amazonaws.com/2bb34d85-dfa5-4b0e-bc1d-094537af475c")
//                .title("제목입니다.")
//                .subTitle("소제목입니다.")
//                .introduce("제 소개입니다.")
//                .difficulty(DifficultyType.BEGINNER)
//                .content("<p>본문입니다.아라어라어라ㅣㄴㅇ</p>")
//                .systems(Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE))
//                .lecturePrices(Arrays.asList(price1, price2))
//                .subjects(Arrays.asList(subject1, subject2))
//                .build();
//
//        LectureResponse lectureResponse = lectureService.addLecture(param, user);
//        assertThat(lectureResponse.getId()).isNotNull();
//
//        assertThat(lectureResponse).extracting("thumbnail").isEqualTo(param.getThumbnailUrl());
//        assertThat(lectureResponse).extracting("title").isEqualTo(param.getTitle());
//        assertThat(lectureResponse).extracting("subTitle").isEqualTo(param.getSubTitle());
//        assertThat(lectureResponse).extracting("introduce").isEqualTo(param.getIntroduce());
//        assertThat(lectureResponse).extracting("difficultyType").isEqualTo(param.getDifficulty().getType());
//        assertThat(lectureResponse).extracting("difficultyName").isEqualTo(param.getDifficulty().getName());
//        assertThat(lectureResponse).extracting("content").isEqualTo(param.getContent());
//        assertThat(lectureResponse.getLecturePrices()).hasSize(param.getLecturePrices().size());
//        assertThat(lectureResponse.getLectureSubjects()).hasSize(param.getSubjects().size());
//    }
//
//    @Test
//    void 강의목록검색_테스트() throws Exception {
//        LectureListRequest request = LectureBuilder.getLectureListRequest(
//                Arrays.asList("개발", "프로그래밍언어"),
////                Arrays.asList("자바", "백엔드", "프론트엔드"),
////                Arrays.asList(DifficultyType.BEGINNER),
//                Collections.emptyList(),
//                Collections.emptyList(),
//                Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE),
//                true);
//
//        List<LectureResponse> lectures = lectureService.getLectures(request);
//
//        assertThat(lectures).isNotEmpty();
//
//        Set<Long> idSet = lectures.stream().map(lecture -> lecture.getId()).collect(Collectors.toSet());
//
//        assertEquals(idSet.size(), lectures.size());
//        lectures.forEach(lecture -> {
//            lecture.getLectureSubjects().forEach(subject -> {
//                assertThat(subject.getParent()).isIn(request.getParents());
//                assertThat(subject.getKrSubject()).isIn(request.getSubjects());
//            });
//            assertThat(DifficultyType.find(lecture.getDifficultyType())).isIn(request.getDifficulties());
//            lecture.getSystemTypes().forEach(systemType -> {
//                assertThat(SystemType.find(systemType.getType())).isIn(request.getSystems());
//            });
//            lecture.getSystemTypes().forEach(systemType -> assertThat(systemType.getType()).isIn(request.getSystems().stream().map(system -> system.getType()).collect(Collectors.toList())));
//            lecture.getLecturePrices().forEach(lecturePrice -> assertThat(lecturePrice.getIsGroup()).isEqualTo(request.getIsGroup()));
//        });
//
//    }
//
//}