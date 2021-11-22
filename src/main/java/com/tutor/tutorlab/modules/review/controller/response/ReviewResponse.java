package com.tutor.tutorlab.modules.review.controller.response;

import com.tutor.tutorlab.modules.review.vo.Review;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Data;

@Data
public class ReviewResponse {

    public ReviewResponse(Review parent, Review child) {
        this.reviewId = parent.getId();
        this.score = parent.getScore();
        this.content = parent.getContent();
        this.username = parent.getUser().getUsername();
        this.createdAt = LocalDateTimeUtil.getDateTimeToString(parent.getCreatedAt());
        this.child = new ChildReviewResponse(child);
    }

    private Long reviewId;
    private Integer score;
    private String content;
    private String username;
    private String createdAt;
    private ChildReviewResponse child;

    @Data
    private class ChildReviewResponse {

        private Long reviewId;
        private String content;
        private String username;
        private String createdAt;

        public ChildReviewResponse(Review review) {
            if (review != null) {
                this.reviewId = review.getId();
                this.content = review.getContent();
                this.username = review.getUser().getUsername();
                this.createdAt = LocalDateTimeUtil.getDateTimeToString(review.getCreatedAt());
            }
        }
    }
}
