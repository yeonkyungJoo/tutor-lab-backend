package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TuteeUpdateRequest {

    @ApiModelProperty(value = "강의주제", example = "python,java", required = false)
    private String subjects;

    @Builder(access = AccessLevel.PRIVATE)
    private TuteeUpdateRequest(String subjects) {
        this.subjects = subjects;
    }

    public static TuteeUpdateRequest of(String subjects) {
        return TuteeUpdateRequest.builder()
                .subjects(subjects)
                .build();
    }
}
