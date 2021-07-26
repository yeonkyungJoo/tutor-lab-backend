package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TutorUpdateRequest {

    private String subjects;
    private List<CareerUpdateRequest> careers;
    private List<EducationUpdateRequest> educations;
    private boolean specialist;

}
