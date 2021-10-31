package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TutorUpdateRequest {

    @ApiModelProperty(value = "경력", required = false)
    private List<CareerUpdateRequest> careers = new ArrayList<>();

    @ApiModelProperty(value = "교육", required = false)
    private List<EducationUpdateRequest> educations = new ArrayList<>();

//    @ApiModelProperty(value = "강의주제", example = "Database", required = false)
//    private String subjects;
//
//    @ApiModelProperty(value = "전문성", example="true", required = false)
//    private boolean specialist;

    @Builder(access = AccessLevel.PRIVATE)
    private TutorUpdateRequest(List<CareerUpdateRequest> careers, List<EducationUpdateRequest> educations) {
        this.careers = careers;
        this.educations = educations;
    }

    public static TutorUpdateRequest of(List<CareerUpdateRequest> careers, List<EducationUpdateRequest> educations) {
        return TutorUpdateRequest.builder()
                .careers(careers)
                .educations(educations)
                .build();
    }
}
