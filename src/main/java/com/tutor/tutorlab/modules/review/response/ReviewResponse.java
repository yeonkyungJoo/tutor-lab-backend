package com.tutor.tutorlab.modules.review.response;

import com.tutor.tutorlab.modules.review.vo.Review;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Data;

@Data
public class ReviewResponse {

    public ReviewResponse(Review review) {
        this.score = review.getScore();
        this.content = review.getContent();
        this.username = review.getUser().getUsername();
        this.createdAt = LocalDateTimeUtil.getDateTimeToString(review.getCreatedAt());
    }

    private Integer score;
    private String content;
    private String username;
    private String createdAt;
}