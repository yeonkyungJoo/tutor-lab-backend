package com.tutor.tutorlab.modules.purchase.controller.response;

import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.Data;

@Data
public class EnrollmentResponse {

    private String tutee;
    private String lectureTitle;
    private boolean closed;

    // TODO - 쿼리
    public EnrollmentResponse(Enrollment enrollment) {
        this.tutee = enrollment.getTutee().getUser().getUsername();
        this.lectureTitle = enrollment.getLecture().getTitle();
        this.closed = enrollment.isClosed();
    }
}
