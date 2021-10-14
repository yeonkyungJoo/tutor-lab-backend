package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.account.vo.Tutee;
import lombok.Data;

@Data
public class TuteeResponse {

    private UserResponse user;
    private String subjects;

    // TODO - 쿼리
    public TuteeResponse(Tutee tutee) {
        this.user = new UserResponse(tutee.getUser());
        this.subjects = tutee.getSubjects();
    }
}
