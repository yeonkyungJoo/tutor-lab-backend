package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;

import java.util.List;

public interface LectureService {
    LectureResponse getLecture(long id) throws Exception;

    LectureResponse addLecture(AddLectureRequest addLectureRequest, User user) throws Exception;

    List<LectureResponse> getLectures(LectureListRequest lectureListRequest);
}
