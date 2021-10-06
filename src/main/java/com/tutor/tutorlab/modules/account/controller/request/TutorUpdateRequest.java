package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder(access = AccessLevel.PRIVATE)
    public TutorUpdateRequest(String subjects, boolean specialist) {
        this.subjects = subjects;
        this.specialist = specialist;
    }

    public static TutorUpdateRequest of(String subjects, boolean specialist) {
        return TutorUpdateRequest.builder()
                .subjects(subjects)
                .specialist(specialist)
                .build();
    }

}
