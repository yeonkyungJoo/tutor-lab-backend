package com.tutor.tutorlab.modules.lecture.controller.request;

import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureListRequest {

    private String lectureName;
    private List<String> parents;       // 강의종류
    private List<String> subjects;      // 언어
    private List<SystemType> systems;   // 수업방식 : 온/오프라인 / 장소협의가능
    private Boolean isGroup;            // 그룹여부
    private List<DifficultyType> difficulties; // 레벨, 난이드

    // TODO - CHECK : -Duplicate
    @AssertTrue(message = "수업방식 검색이 중복되었습니다.")
    private boolean isSystemDuplicate() {
        if (CollectionUtils.isEmpty(systems)) {
            return true;
        }
        Set<SystemType> systemTypeSet = systems.stream().collect(Collectors.toSet());
        return systemTypeSet.size() == systems.size();
    }

    // TODO - CHECK : -Duplicate
    @AssertTrue(message = "수업난이도 검색이 중복되었습니다.")
    private boolean isDifficultyDuplicate() {
        if (CollectionUtils.isEmpty(difficulties)) {
            return true;
        }
        Set<DifficultyType> difficultyTypeSet = difficulties.stream().collect(Collectors.toSet());
        return difficultyTypeSet.size() == difficulties.size();
    }

    @Builder(access = AccessLevel.PRIVATE)
    public LectureListRequest(String lectureName, List<String> parents, List<String> subjects, List<SystemType> systems, Boolean isGroup, List<DifficultyType> difficulties) {
        this.lectureName = lectureName;
        this.parents = parents;
        this.subjects = subjects;
        this.systems = systems;
        this.isGroup = isGroup;
        this.difficulties = difficulties;
    }

    public static LectureListRequest of(String lectureName, List<String> parents, List<String> subjects, List<SystemType> systems, Boolean isGroup, List<DifficultyType> difficulties) {
        return LectureListRequest.builder()
                .lectureName(lectureName)
                .parents(parents)
                .subjects(subjects)
                .systems(systems)
                .isGroup(isGroup)
                .difficulties(difficulties)
                .build();
    }

}
