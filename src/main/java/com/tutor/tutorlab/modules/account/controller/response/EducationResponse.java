package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.account.enums.EducationLevelType;
import com.tutor.tutorlab.modules.account.vo.Education;
import lombok.Data;

@Data
public class EducationResponse {

    private EducationLevelType educationLevel;
    private String schoolName;
    private String major;
    private String others;

    public EducationResponse(Education education) {
        this.educationLevel = education.getEducationLevel();
        this.schoolName = education.getSchoolName();
        this.major = education.getMajor();
        this.others = education.getOthers();
    }
}
