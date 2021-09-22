package com.tutor.tutorlab.modules.lecture.controller.request;

import com.tutor.tutorlab.modules.lecture.enums.DifficultyType;
import com.tutor.tutorlab.modules.lecture.enums.SystemType;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LectureCreateRequest {

    @GroupSequence({OrderFirst.class, OrderSecond.class})
    public interface Order {}
    public interface OrderFirst {}
    public interface OrderSecond {}

    @NotBlank(message = "강의 소개 메인 이미지를 입력해주세요.", groups = OrderFirst.class)
    private String thumbnailUrl;

    @Length(min = 1, max = 40, message = "제목을 {min}자 ~ {max}자 이내로 입력해주세요.", groups = OrderFirst.class)
    @NotBlank(message = "강의 타이틀을 입력해주세요.", groups = OrderFirst.class)
    private String title;

    @Length(min = 1, max = 25, message = "강의 소제목을 {min}자 ~ {max}자 이내로 입력해주세요.", groups = OrderFirst.class)
    @NotBlank(message = "강의 소제목을 입력해주세요.", groups = OrderFirst.class)
    private String subTitle;

    @Length(min = 1, max = 200, message = "내 소개를 {min}자 ~ {max}자 이내로 입력해주세요.", groups = OrderFirst.class)
    @NotBlank(message = "내 소개를 입력해주세요.", groups = OrderFirst.class)
    private String introduce;

    @NotNull(message = "난이도를 입력해주세요.", groups = OrderFirst.class)
    private DifficultyType difficulty;

    @NotBlank(message = "강의 상세내용을 입력해주세요.", groups = OrderFirst.class)
    private String content;

    @NotNull(message = "강의방식1을 입력해주세요.", groups = OrderFirst.class)
    private List<SystemType> systems;

    @Valid
    @Length(min = 1, max = 5, message = "강의방식2는 최소 {min}개 ~ 최대 {max}개만 선택할 수 있습니다.")
    @NotNull(message = "강의방식2를 입력해주세요.")
    private List<LecturePriceCreateRequest> lecturePrices;

    @Valid
    @Length(min = 1, message = "강의종류를 최소 1개 입력해주세요.")
    @NotNull(message = "강의종류를 입력해주세요.")
    private List<LectureSubjectCreateRequest> subjects;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class LectureSubjectCreateRequest {

        @NotBlank(message = "강의 종류를 입력해주세요.")
        private String parent;

        @NotBlank(message = "언어를 입력해주세요.")
        private String krSubject;
    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor
    public static class LecturePriceCreateRequest {

        @NotNull(message = "그룹여부를 선택해주세요.", groups = OrderFirst.class)
        private Boolean isGroup;

        private Integer groupNumber;

        @NotNull(message = "시간당 수강료를 입력해주세요.", groups = OrderFirst.class)
        private Long pertimeCost;

        @NotNull(message = "1회당 강의 시간을 입력해주세요.", groups = OrderFirst.class)
        private Integer pertimeLecture;

        @NotNull(message = "총 강의 횟수를 입력해주세요.", groups = OrderFirst.class)
        private Integer totalTime;

        @NotNull(message = "최종 수강료를 입력해주세요.", groups = OrderFirst.class)
        private Long totalCost;

        @AssertTrue(message = "그룹 수업 인원수를 입력해주세요.", groups = OrderSecond.class)
        private boolean isGroupNumberValid() {
            if (Boolean.TRUE.equals(isGroup)) {
                return !Objects.isNull(groupNumber) && groupNumber > 0;
            }
            return true;
        }
    }

}
