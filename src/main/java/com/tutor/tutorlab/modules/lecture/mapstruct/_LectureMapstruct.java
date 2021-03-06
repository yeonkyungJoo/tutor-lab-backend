package com.tutor.tutorlab.modules.lecture.mapstruct;

import com.tutor.tutorlab.config.mapstruct.MapstructConfig;
import com.tutor.tutorlab.modules.lecture.controller.response.LectureResponse;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.lecture.vo.LectureSubject;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;

@Mapper(config = MapstructConfig.class, imports = {DifficultyType.class, SystemType.class})
public interface _LectureMapstruct {

//    @Mappings({
////            @Mapping(target = "difficultyType", expression = "java(lecture.getDifficultyType().getType())"),
////            @Mapping(target = "difficultyName", expression = "java(lecture.getDifficultyType().getName())"),
//            @Mapping(target = "systemTypes", source = "systemTypes"),
//            @Mapping(target = "lecturePrices", source = "lecturePrices"),
//            @Mapping(target = "lectureSubjects", source = "subjects")
//    })
//    LectureResponse lectureToLectureResponse(Lecture lecture,
//                                             List<LectureResponse.LecturePriceResponse> lecturePrices,
//                                             List<LectureResponse.SystemTypeResponse> systemTypes,
//                                             List<LectureResponse.LectureSubjectResponse> subjects);
//
//    @IterableMapping(elementTargetType = LectureResponse.class)
//    List<LectureResponse> lectureListToLectureResponseList(List<Lecture> lectures);
//
//    @Mappings({})
//    LectureResponse.SystemTypeResponse systemTypeToSystemTypeResponse(SystemType systemType);
//
//    @IterableMapping(elementTargetType = LectureResponse.SystemTypeResponse.class)
//    List<LectureResponse.SystemTypeResponse> systemTypeListToSystemTypeResponseList(List<SystemType> systemTypes);
//
//    @Mappings({})
//    LectureResponse.LecturePriceResponse lecturePriceToLecturePriceResponse(LecturePrice lecturePrice);
//
//    @IterableMapping(elementTargetType = LectureResponse.LecturePriceResponse.class)
//    List<LectureResponse.LecturePriceResponse> lecturePriceListToLecturePriceResponseList(List<LecturePrice> lecturePrices);
//
//    @Mappings({})
//    LectureResponse.LectureSubjectResponse lectureSubjectToLectureSubjectResponse(LectureSubject lectureSubject);
//
//    @IterableMapping(elementTargetType = LectureResponse.LectureSubjectResponse.class)
//    List<LectureResponse.LectureSubjectResponse> lectureSubjectListToLectureSubjectResponseList(List<LectureSubject> lectureSubjects);
}
