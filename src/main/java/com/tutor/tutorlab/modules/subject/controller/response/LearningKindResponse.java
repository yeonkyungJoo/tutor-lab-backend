package com.tutor.tutorlab.modules.subject.controller.response;

import com.tutor.tutorlab.modules.lecture.embeddable.LearningKind;
import lombok.Data;

@Data
public class LearningKindResponse {

    private Long learningKindId;
    private String learningKind;

//    public LearningKindResponse(Long learningKindId, String learningKind) {
//        this.learningKindId = learningKindId;
//        this.learningKind = learningKind;
//    }

    public LearningKindResponse(LearningKind learningKind) {
        this.learningKindId = learningKind.getLearningKindId();
        this.learningKind = learningKind.getLearningKind();
    }
}
