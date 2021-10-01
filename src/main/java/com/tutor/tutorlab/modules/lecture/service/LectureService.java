package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureUpdateRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LectureService {

    Lecture getLecture(Long lectureId);
    LectureResponse getLectureResponse(Long lectureId);

    // TODO - CHECK
    // List<LectureResponse> getLectureResponses(LectureListRequest lectureListRequest);
    Page<LectureResponse> getLectureResponses(String zone, Integer page);

    Lecture createLecture(User user, LectureCreateRequest lectureCreateRequest);

    void updateLecture(User user, Long lectureId, LectureUpdateRequest lectureUpdateRequest);

    void deleteLecture(User user, Long lectureId);
}
