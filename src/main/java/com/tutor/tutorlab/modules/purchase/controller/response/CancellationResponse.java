package com.tutor.tutorlab.modules.purchase.controller.response;

import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
public class CancellationResponse {

    private Long cancellationId;
    private String reason;
    private boolean approved;

    private EnrolledLectureResponse lecture;

    private Long tuteeId;
    private String tuteeName;

    @Builder(access = AccessLevel.PUBLIC)
    private CancellationResponse(Cancellation cancellation, Lecture lecture, LecturePrice lecturePrice, Long tuteeId, String tuteeName) {
        this.cancellationId = cancellation.getId();
        this.reason = cancellation.getReason();
        this.approved = cancellation.isApproved();
        this.lecture = new EnrolledLectureResponse(lecture, lecturePrice);
        this.tuteeId = tuteeId;
        this.tuteeName = tuteeName;
    }
}
