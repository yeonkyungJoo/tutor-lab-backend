package com.tutor.tutorlab.modules.lecture.service;

import com.tutor.tutorlab.modules.account.repository.TutorRepository;
import com.tutor.tutorlab.modules.account.vo.Tutor;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.lecture.controller.request.AddLectureRequest;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstruct;
import com.tutor.tutorlab.modules.lecture.mapstruct.LectureMapstructUtil;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepository;
import com.tutor.tutorlab.modules.lecture.repository.LectureRepositorySupport;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final TutorRepository tutorRepository;
    private final LectureRepositorySupport lectureRepositorySupport;

    private final LectureMapstructUtil lectureMapstructUtil;


    @Override
    public LectureResponse getLecture(Long id) {
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 id에 맞는 강의가 없습니다."));
        return new LectureResponse(lecture);
    }

    @Transactional
    @Override
    public LectureResponse addLecture(AddLectureRequest addLectureRequest, User user) {
        Tutor tutor = Optional.ofNullable(tutorRepository.findByUser(user)).orElseThrow(() -> new RuntimeException("해당 유저는 튜터가 아닙니다."));

        // TODO 유효성 -> 해당 유저의 강의 갯수 제한?

        // TODO 강의 -> 유저 address 정보 넣어줘야함.

        Lecture lecture = buildLecture(addLectureRequest, tutor);
        for (AddLectureRequest.AddLecturePriceRequest lecturePriceRequest : addLectureRequest.getLecturePrices()) {
            lecture.addPrice(buildLecturePrice(lecturePriceRequest));
        }

        for (AddLectureRequest.AddLectureSubjectRequest subjectRequest : addLectureRequest.getSubjects()) {
            lecture.addSubject(buildLectureSubject(subjectRequest));
        }

        Lecture savedLecture = lectureRepository.save(lecture);
        return lectureMapstructUtil.getLectureResponse(savedLecture);
    }

    @Override
    public List<LectureResponse> getLectures(LectureListRequest lectureListRequest) {
        List<LectureResponse> lectures = lectureRepositorySupport.findLecturesBySearch(lectureListRequest).stream()
                .map(lecture -> new LectureResponse(lecture))
                .collect(Collectors.toList());
        return lectures;
    }

//    private LectureResponse getLectureResponse(Lecture lecture) {
//        List<LectureResponse.LecturePriceResponse> prices = lectureMapstruct.lecturePriceListToLecturePriceResponseList(lecture.getLecturePrices());
//        List<LectureResponse.LectureSubjectResponse> subjects = lectureMapstruct.lectureSubjectListToLectureSubjectResponseList(lecture.getLectureSubjects());
//        List<LectureResponse.SystemTypeResponse> systemTypes = lectureMapstruct.systemTypeListToSystemTypeResponseList(lecture.getSystemTypes().stream().map(systemType -> SystemType.find(systemType.getType())).collect(Collectors.toList()));
//
//        return lectureMapstruct.lectureToLectureResponse(lecture, prices, systemTypes, subjects);
//    }

    private LectureSubject buildLectureSubject(AddLectureRequest.AddLectureSubjectRequest subjectRequest) {
        return LectureSubject.builder()
                .parent(subjectRequest.getParent())
//                .enSubject(subjectRequest.getEnSubject())
                .krSubject(subjectRequest.getKrSubject())
                .build();
    }

    private LecturePrice buildLecturePrice(AddLectureRequest.AddLecturePriceRequest lecturePriceRequest) {
        LecturePrice lecturePrice = LecturePrice.builder()
                .isGroup(lecturePriceRequest.getIsGroup())
                .groupNumber(lecturePriceRequest.getGroupNumber())
                .pertimeLecture(lecturePriceRequest.getPertimeLecture())
                .pertimeCost(lecturePriceRequest.getPertimeCost())
                .totalCost(lecturePriceRequest.getTotalCost())
                .totalTime(lecturePriceRequest.getTotalTime())
                .build();
        return lecturePrice;
    }

    private Lecture buildLecture(AddLectureRequest addLectureRequest, Tutor tutor) {
        return Lecture.builder()
                .tutor(tutor)
                .thumbnail(addLectureRequest.getThumbnailUrl())
                .title(addLectureRequest.getTitle())
                .subTitle(addLectureRequest.getSubTitle())
                .introduce(addLectureRequest.getIntroduce())
                .content(addLectureRequest.getContent())
                .difficultyType(addLectureRequest.getDifficulty())
                .systemTypes(addLectureRequest.getSystems())
                .lecturePrices(new ArrayList<>())
                .lectureSubjects(new ArrayList<>())
                .build();
    }
}
