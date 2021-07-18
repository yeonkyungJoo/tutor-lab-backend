package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorSignUpRequest extends SignUpRequest {

    // career
    private String companyName;
    private String duty;    // 직급
    private String startDate;
    private String endDate;
    private boolean present;

    // education
    private String schoolName;
    private String major;
    private String entranceDate;
    private String graduationDate;
    private double score;
    private String degree;

}
