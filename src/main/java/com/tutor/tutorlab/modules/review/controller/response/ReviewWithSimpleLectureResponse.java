package com.tutor.tutorlab.modules.review.controller.response;

import com.tutor.tutorlab.modules.lecture.controller.response.SimpleLectureResponse;
import com.tutor.tutorlab.modules.review.vo.Review;
import lombok.ToString;

@ToString(callSuper = true)
public class ReviewWithSimpleLectureResponse extends ReviewResponse {
    // TODO - 상속 or Composition

    private SimpleLectureResponse lecture;

    public ReviewWithSimpleLectureResponse(Review parent, Review child) {
        super(parent, child);
        lecture = new SimpleLectureResponse(parent.getLecture());
    }

    public SimpleLectureResponse getLecture() {
        return lecture;
    }
}
