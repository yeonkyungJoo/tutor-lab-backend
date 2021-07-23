package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.lecture.repository.LectureImageRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureSubjectRepository lectureSubjectRepository;
    private final LectureImageRepository lectureImageRepository;

}
