package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TuteeUpdateRequest {

    @ApiModelProperty(value = "강의주제", example = "python,java", required = false)
    private String subjects;

    @Builder
    public TuteeUpdateRequest(String subjects) {
        this.subjects = subjects;
    }
}
