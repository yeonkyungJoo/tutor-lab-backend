package com.tutor.tutorlab.modules.lecture.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EnrollmentRequest {

    @NotNull
    private Long lectureId;
}
