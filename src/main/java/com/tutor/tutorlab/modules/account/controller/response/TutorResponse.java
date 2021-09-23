package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.account.vo.Tutor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TutorResponse {

    private UserResponse user;
    private String subjects;
    private List<CareerResponse> careers;
    private List<EducationResponse> educations;
    private boolean specialist;

    public TutorResponse(Tutor tutor) {
        this.user = new UserResponse(tutor.getUser());
        this.subjects = tutor.getSubjects();
        this.careers = tutor.getCareers().stream().map(CareerResponse::new).collect(Collectors.toList());
        this.educations = tutor.getEducations().stream().map(EducationResponse::new).collect(Collectors.toList());
        this.specialist = tutor.isSpecialist();
    }
}
