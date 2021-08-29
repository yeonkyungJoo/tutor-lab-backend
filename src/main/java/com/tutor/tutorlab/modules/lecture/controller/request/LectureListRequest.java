package com.tutor.tutorlab.modules.lecture.controller.request;

import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.AssertTrue;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class LectureListRequest {

    private String lectureName;

    // 강의종류
    private List<String> parents; // 종류

    private List<String> subjects; // 언어

    // 수업 방식
    private List<SystemType> systems; // 온/오프라인 / 장소협의가능

    private Boolean isGroup; // 그룹여부

    // TODO 주소

    // 레벨
    private List<DifficultyType> difficulties; // 난이도

    @AssertTrue(message = "수업방식 검색이 중복되었습니다.")
    private boolean isSystemDuplicate() {
        if (CollectionUtils.isEmpty(systems)) {
            return true;
        }
        Set<SystemType> systemTypeSet = systems.stream().collect(Collectors.toSet());
        return systemTypeSet.size() == systems.size();
    }

    @AssertTrue(message = "수업난이도 검색이 중복되었습니다.")
    private boolean isDifficultyDuplicate() {
        if (CollectionUtils.isEmpty(difficulties)) {
            return true;
        }
        Set<DifficultyType> difficultyTypeSet = difficulties.stream().collect(Collectors.toSet());
        return difficultyTypeSet.size() == difficulties.size();
    }

}
