package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TutorSignUpRequest {

    private String subject;
    private List<CreateCareerRequest> careers;
    private List<CreateEducationRequest> educations;
    private boolean specialist;

    @Data
    class CreateCareerRequest {

        private String companyName;
        private String duty;    // 직급
        private String startDate;
        private String endDate;
        private boolean present;
    }

    @Data
    class CreateEducationRequest {

        private String schoolName;
        private String major;
        private String entranceDate;
        private String graduationDate;
        private double score;
        private String degree;
    }

}
