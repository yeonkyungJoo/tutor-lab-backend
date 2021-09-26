package com.tutor.tutorlab.modules.lecture.controller;

import com.tutor.tutorlab.configuration.AbstractRequestTest;
import com.tutor.tutorlab.modules.lecture.common.LectureBuilder;
import com.tutor.tutorlab.modules.lecture.controller.request.LectureListRequest;
import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import com.tutor.tutorlab.utils.MultiValueConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LectureListRequestTest extends AbstractRequestTest {
// TODO - CHECK
    @ParameterizedTest
    @MethodSource("generateClearRequest")
    void 완벽한파라미터_테스트(List<String> parents, List<String> subjects, List<SystemType> systemTypes, List<DifficultyType> difficultyTypes, Boolean isGroup) {
        LectureListRequest request = LectureBuilder.getLectureListRequest(parents, subjects, difficultyTypes, systemTypes, isGroup);
        Set<ConstraintViolation<LectureListRequest>> validate = validator.validate(request);
        assertTrue(validate.size() == 0);
    }

    @ParameterizedTest
    @MethodSource("generateDuplicateRequest")
    void 중복파라미터_테스트(List<String> parents, List<String> subjects, List<SystemType> systemTypes, List<DifficultyType> difficultyTypes, Boolean isGroup) {
        LectureListRequest request = LectureBuilder.getLectureListRequest(parents, subjects, difficultyTypes, systemTypes, isGroup);
        Set<ConstraintViolation<LectureListRequest>> validate = validator.validate(request);
        assertTrue(validate.size() > 0);
        validate.forEach(validation -> {
            System.out.println(validation.getConstraintDescriptor().getMessageTemplate());
            assertFalse(Boolean.getBoolean(validation.getInvalidValue().toString()));
        });
    }

    static Stream<Arguments> generateClearRequest() {
        List<String> parents = Arrays.asList("개발", "프로그래밍언어");
        List<String> subjects = Arrays.asList("자바", "백엔드", "프론트");
        List<SystemType> systemTypes = Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE, SystemType.NEGOTIABLE);
        List<DifficultyType> difficultyTypes = Arrays.asList(DifficultyType.BEGINNER, DifficultyType.ADVANCED, DifficultyType.BASIC, DifficultyType.INTERMEDIATE);

        return Stream.of(
                Arguments.of(parents, subjects, systemTypes, difficultyTypes, true),
                Arguments.of(null, null, null, null, null)
        );
    }

    static Stream<Arguments> generateDuplicateRequest() {
        List<SystemType> systemTypes = Arrays.asList(SystemType.ONLINE, SystemType.OFFLINE, SystemType.NEGOTIABLE, SystemType.NEGOTIABLE);
        List<DifficultyType> difficultyTypes = Arrays.asList(DifficultyType.BEGINNER, DifficultyType.ADVANCED, DifficultyType.BASIC, DifficultyType.INTERMEDIATE, DifficultyType.INTERMEDIATE);

        return Stream.of(
                Arguments.of(null, null, systemTypes, null, true),
                Arguments.of(null, null, null, difficultyTypes, true)
        );
    }

}
