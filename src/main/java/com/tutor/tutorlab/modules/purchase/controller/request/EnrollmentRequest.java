package com.tutor.tutorlab.modules.purchase.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EnrollmentRequest {

    @NotNull
    private Long lectureId;
}