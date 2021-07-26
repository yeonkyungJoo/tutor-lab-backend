package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Data;

@Data
public class EducationUpdateRequest {

    private String schoolName;
    private String major;
    private String entranceDate;
    private String graduationDate;
    private double score;
    private String degree;
}
