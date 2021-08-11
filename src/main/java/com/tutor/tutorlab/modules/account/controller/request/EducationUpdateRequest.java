package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class EducationUpdateRequest {

    // TODO - CHECK : Validation

    @ApiModelProperty(value = "학교명", example = "school", required = true)
    @NotBlank
    private String schoolName;

    @ApiModelProperty(value = "전공", example = "business", required = true)
    @NotBlank
    private String major;

    @ApiModelProperty(value = "입학일자", example = "2021-01-01", required = true)
    @NotBlank
    private String entranceDate;

    @ApiModelProperty(value = "졸업일자", example = "2021-09-01", required = false)
    @NotBlank
    private String graduationDate;

    @ApiModelProperty(value = "학점", allowEmptyValue = true, required = false)
    private double score;

    @ApiModelProperty(value = "학위", example = "Master", required = false)
    private String degree;

    @Builder
    public EducationUpdateRequest(String schoolName, String major, String entranceDate, String graduationDate, double score, String degree) {
        this.schoolName = schoolName;
        this.major = major;
        this.entranceDate = entranceDate;
        this.graduationDate = graduationDate;
        this.score = score;
        this.degree = degree;
    }
}
