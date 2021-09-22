package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureCreateRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureUpdateRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;

import java.util.List;

public interface LectureService {

    LectureResponse getLecture(Long lectureId);
    // TODO - CHECK
    List<LectureResponse> getLectures(LectureListRequest lectureListRequest);

    LectureResponse createLecture(User user, LectureCreateRequest lectureCreateRequest);

    void updateLecture(User user, Long lectureId, LectureUpdateRequest lectureUpdateRequest);

    void deleteLecture(User user, Long lectureId);
}
