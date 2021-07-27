package com.tutor.tutorlab.modules.account.controller.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TutorUpdateRequest {

    private String subjects;
    private List<CareerUpdateRequest> careers = new ArrayList<>();
    private List<EducationUpdateRequest> educations = new ArrayList<>();
    private boolean specialist;

    @Builder
    public TutorUpdateRequest(String subjects, List<CareerUpdateRequest> careers, List<EducationUpdateRequest> educations, boolean specialist) {
        this.subjects = subjects;

        if (careers != null) {
            this.careers.addAll(careers);
        }
        if (educations != null) {
            this.educations.addAll(educations);
        }

        this.specialist = specialist;
    }

    @Builder
    public TutorUpdateRequest(String subjects, boolean specialist) {
        this.subjects = subjects;
        this.specialist = specialist;
    }

    public void addCareerUpdateRequest(CareerUpdateRequest careerUpdateRequest) {
        this.careers.add(careerUpdateRequest);
    }

    public void addEducationUpdateRequest(EducationUpdateRequest educationUpdateRequest) {
        this.educations.add(educationUpdateRequest);
    }
}
