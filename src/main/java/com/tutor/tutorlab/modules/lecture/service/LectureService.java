package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstruct;
import com.tutor.tutorlab.modules.lecture.repository.LectureImageRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureSubjectRepository;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureMapstruct lectureMapstruct;

    public LectureResponse getLecture(long id) {
        Optional<Lecture> lectureOptional = lectureRepository.findById(id);
        Lecture lecture = lectureOptional.orElseThrow(() -> new RuntimeException("해당 id에 맞는 강의가 없습니다."));
        LectureResponse lectureResponse = lectureMapstruct.lectureToLectureResponse(lecture);

        return lectureResponse;
    }
}
