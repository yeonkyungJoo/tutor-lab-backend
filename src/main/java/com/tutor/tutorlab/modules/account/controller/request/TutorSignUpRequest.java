package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class TutorSignUpRequest {

    private String subjects;
    private List<CareerCreateRequest> careers = new ArrayList<>();
    private List<EducationCreateRequest> educations = new ArrayList<>();
    private boolean specialist;

    @Builder
    public TutorSignUpRequest(String subjects, List<CareerCreateRequest> careers, List<EducationCreateRequest> educations, boolean specialist) {
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
    public TutorSignUpRequest(String subjects, boolean specialist) {
        this.subjects = subjects;
        this.specialist = specialist;
    }
}
