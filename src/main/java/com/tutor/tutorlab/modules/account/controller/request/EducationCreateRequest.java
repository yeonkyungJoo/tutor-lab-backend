package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
public class EducationCreateRequest {

    @ApiModelProperty(value = "학교명", example = "school", required = true)
    @NotBlank
    private String schoolName;

    @ApiModelProperty(value = "전공", example = "computer science", required = true)
    @NotBlank
    private String major;

    @ApiModelProperty(value = "입학일자", example = "2021-01-01", required = true)
    @Past @NotBlank
    private String entranceDate;

    @ApiModelProperty(value = "졸업일자", allowEmptyValue = true, required = false)
    private String graduationDate;

    @ApiModelProperty(value = "학점", example = "4.01", required = false)
    private double score;

    @ApiModelProperty(value = "학위", allowEmptyValue = true, required = false)
    private String degree;

    @Builder
    public EducationCreateRequest(String schoolName, String major, String entranceDate, String graduationDate, double score, String degree) {
        this.schoolName = schoolName;
        this.major = major;
        this.entranceDate = entranceDate;
        this.graduationDate = graduationDate;
        this.score = score;
        this.degree = degree;
    }

    @AssertTrue
    public boolean isValidDate() {
        boolean valid = true;

        try {

            LocalDate entranceDate = LocalDate.parse(getEntranceDate());
            LocalDate graduationDate = null;

            if (StringUtils.isNotEmpty(getGraduationDate())) {
                graduationDate = LocalDate.parse(getGraduationDate());
                valid = entranceDate.isBefore(graduationDate);
            }

        } catch (DateTimeParseException e) {
            valid = false;
        }

        return valid;
    }
}
