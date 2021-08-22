package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class TutorSignUpRequest {

    @ApiModelProperty(value = "강의주제", example = "spring", required = false)
    private String subjects;

    @Valid
    @ApiModelProperty(value = "경력", required = false)
    private List<CareerCreateRequest> careers = new ArrayList<>();

    @Valid
    @ApiModelProperty(value = "교육", required = false)
    private List<EducationCreateRequest> educations = new ArrayList<>();

    @ApiModelProperty(value = "전문성", example="false", required = false)
    private boolean specialist;

    @Builder
    public TutorSignUpRequest(String subjects, List<CareerCreateRequest> careers, List<EducationCreateRequest> educations, boolean specialist) {
        this.subjects = subjects;
        if (careers != null) {
            this.careers.addAll(careers);
        }
        if (educations != null) {
            this.educations.addAll(educations);
        }
        this.specialist = specialist;
    }

    @Builder
    public TutorSignUpRequest(String subjects, boolean specialist) {
        this.subjects = subjects;
        this.specialist = specialist;
    }

    public void addCareerCreateRequest(CareerCreateRequest careerCreateRequest) {
        this.careers.add(careerCreateRequest);
    }

    public void addEducationCreateRequest(EducationCreateRequest educationCreateRequest) {
        this.educations.add(educationCreateRequest);
    }
}
