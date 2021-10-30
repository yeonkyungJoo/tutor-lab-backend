package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class EducationUpdateRequest {

    @ApiModelProperty(value = "학교명", example = "school", required = true)
    @NotBlank
    private String schoolName;

    @ApiModelProperty(value = "전공", example = "business", required = true)
    @NotBlank
    private String major;

    @ApiModelProperty(value = "입학일자", example = "2021-01-01", required = false)
    @NotBlank
    private String entranceDate;

    @ApiModelProperty(value = "졸업일자", example = "2021-09-01", required = false)
    private String graduationDate;

    @ApiModelProperty(value = "학점", allowEmptyValue = true, required = false)
    private double score;

    @ApiModelProperty(value = "학위", example = "Master", required = false)
    private String degree;

    @Builder(access = AccessLevel.PRIVATE)
    private EducationUpdateRequest(String schoolName, String major, String entranceDate, String graduationDate, double score, String degree) {
        this.schoolName = schoolName;
        this.major = major;
        this.entranceDate = entranceDate;
        this.graduationDate = graduationDate;
        this.score = score;
        this.degree = degree;
    }

    public static EducationUpdateRequest of(String schoolName, String major, String entranceDate, String graduationDate, double score, String degree) {
        return EducationUpdateRequest.builder()
                .schoolName(schoolName)
                .major(major)
                .entranceDate(entranceDate)
                .graduationDate(graduationDate)
                .score(score)
                .degree(degree)
                .build();
    }

//    @AssertTrue
//    private boolean isGraduationDate() {
//        boolean valid = true;
//
//        try {
//
//            LocalDate entranceDate = LocalDate.parse(getEntranceDate());
//            LocalDate graduationDate = null;
//
//            if (StringUtils.isNotEmpty(getGraduationDate())) {
//                graduationDate = LocalDate.parse(getGraduationDate());
//                valid = entranceDate.isBefore(graduationDate);
//            }
//
//        } catch (DateTimeParseException e) {
//            valid = false;
//        }
//
//        return valid;
//    }
}
