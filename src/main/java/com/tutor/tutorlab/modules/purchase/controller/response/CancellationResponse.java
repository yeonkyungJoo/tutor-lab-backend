package com.tutor.tutorlab.modules.purchase.controller.response;

import com.tutor.tutorlab.modules.lecture.vo.Lecture;
import com.tutor.tutorlab.modules.lecture.vo.LecturePrice;
import com.tutor.tutorlab.modules.purchase.vo.Cancellation;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CancellationResponse {

    private Long cancellationId;
    private String reason;
    private boolean approved;
    // Java 8 date/time type `java.time.LocalDateTime` not supported by default
    // private LocalDateTime createdAt;
    private String createdAt;
    private EnrolledLectureResponse lecture;

    private Long tuteeId;
    private String tuteeName;

    private Long chatroomId;

    @Builder(access = AccessLevel.PUBLIC)
    private CancellationResponse(Cancellation cancellation, Lecture lecture, LecturePrice lecturePrice, Long tuteeId, String tuteeName, Long chatroomId) {
        this.cancellationId = cancellation.getId();
        this.reason = cancellation.getReason();
        this.approved = cancellation.isApproved();
        this.createdAt = LocalDateTimeUtil.getDateTimeToString(cancellation.getCreatedAt());
        this.lecture = new EnrolledLectureResponse(lecture, lecturePrice);
        this.tuteeId = tuteeId;
        this.tuteeName = tuteeName;
        this.chatroomId = chatroomId;
    }
}
