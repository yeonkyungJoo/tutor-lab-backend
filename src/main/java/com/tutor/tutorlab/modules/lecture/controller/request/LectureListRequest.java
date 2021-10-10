package com.tutor.tutorlab.modules.lecture.controller.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureListRequest {

    private String title;
//    private List<String> subjects;      // 언어
//    private List<SystemType> systems;   // 수업방식 : 온/오프라인 / 장소협의가능
//    private Boolean isGroup;            // 그룹여부
//    private List<DifficultyType> difficulties; // 레벨, 난이드
//
//    // TODO - CHECK : -Duplicate
//    @AssertTrue(message = "수업방식 검색이 중복되었습니다.")
//    private boolean isSystemDuplicate() {
//        if (CollectionUtils.isEmpty(systems)) {
//            return true;
//        }
//        Set<SystemType> systemTypeSet = systems.stream().collect(Collectors.toSet());
//        return systemTypeSet.size() == systems.size();
//    }
//
//    // TODO - CHECK : -Duplicate
//    @AssertTrue(message = "수업난이도 검색이 중복되었습니다.")
//    private boolean isDifficultyDuplicate() {
//        if (CollectionUtils.isEmpty(difficulties)) {
//            return true;
//        }
//        Set<DifficultyType> difficultyTypeSet = difficulties.stream().collect(Collectors.toSet());
//        return difficultyTypeSet.size() == difficulties.size();
//    }

    @Builder(access = AccessLevel.PRIVATE)
    public LectureListRequest(String title) {
        this.title = title;
    }

    public static LectureListRequest of(String title) {
        return LectureListRequest.builder()
                .title(title)
                .build();
    }

}
