package com.tutor.tutorlab.modules.purchase.controller.response;


import com.tutor.tutorlab.modules.lecture.controller.response.SimpleLectureResponse;
import com.tutor.tutorlab.modules.purchase.vo.Enrollment;
import lombok.ToString;

@ToString(callSuper = true)
public class EnrollmentWithSimpleLectureResponse extends EnrollmentResponse {
    // TODO - CHECK : 상속 or Composition
    private SimpleLectureResponse lecture;

    public EnrollmentWithSimpleLectureResponse(Enrollment enrollment) {
        super(enrollment);
        lecture = new SimpleLectureResponse(enrollment.getLecture());
    }

    public SimpleLectureResponse getLecture() {
        return lecture;
    }
}
