package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TutorUpdateRequest {

    @ApiModelProperty(value = "강의주제", example = "Database", required = false)
    private String subjects;

/*
    @ApiModelProperty(value = "경력", required = false)
    private List<CareerUpdateRequest> careers = new ArrayList<>();

    @ApiModelProperty(value = "교육", required = false)
    private List<EducationUpdateRequest> educations = new ArrayList<>();
*/

    @ApiModelProperty(value = "전문성", example="true", required = false)
    private boolean specialist;

    @Builder
    public TutorUpdateRequest(String subjects, List<CareerUpdateRequest> careers, List<EducationUpdateRequest> educations, boolean specialist) {
        this.subjects = subjects;
        this.specialist = specialist;
    }

    @Builder
    public TutorUpdateRequest(String subjects, boolean specialist) {
        this.subjects = subjects;
        this.specialist = specialist;
    }

}
