package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TuteeUpdateRequest {

    private String subjects;

    @Builder
    public TuteeUpdateRequest(String subjects) {
        this.subjects = subjects;
    }
}
