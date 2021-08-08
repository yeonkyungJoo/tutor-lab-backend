package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;

public interface LectureService {
    LectureResponse getLecture(long id) throws Exception;

    LectureResponse addLecture(AddLectureRequest addLectureRequest) throws Exception;
}
