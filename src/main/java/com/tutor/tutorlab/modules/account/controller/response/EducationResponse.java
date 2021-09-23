package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.account.vo.Education;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Data;

@Data
public class EducationResponse {

    private String schoolName;
    private String major;
    private String entranceDate;
    private String graduationDate;
    private double score;
    private String degree;

    public EducationResponse(Education education) {
        this.schoolName = education.getSchoolName();
        this.major = education.getMajor();
        this.entranceDate = LocalDateTimeUtil.getDateToString(education.getEntranceDate());
        this.graduationDate = LocalDateTimeUtil.getDateToString(education.getGraduationDate());
        this.score = education.getScore();
        this.degree = education.getDegree();
    }
}
