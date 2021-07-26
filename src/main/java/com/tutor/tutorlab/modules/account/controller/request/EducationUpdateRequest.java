package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EducationUpdateRequest {

    private String schoolName;
    private String major;
    private String entranceDate;
    private String graduationDate;
    private double score;
    private String degree;

    @Builder
    public EducationUpdateRequest(String schoolName, String major, String entranceDate, String graduationDate, double score, String degree) {
        this.schoolName = schoolName;
        this.major = major;
        this.entranceDate = entranceDate;
        this.graduationDate = graduationDate;
        this.score = score;
        this.degree = degree;
    }
}