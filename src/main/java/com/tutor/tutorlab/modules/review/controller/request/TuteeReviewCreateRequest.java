package com.tutor.tutorlab.modules.review.controller.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TuteeReviewCreateRequest {

    @Min(0) @Max(5)
    @NotNull
    private Integer score;

    @NotBlank
    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    public TuteeReviewCreateRequest(@Min(0) @Max(5) @NotNull Integer score, @NotBlank String content) {
        this.score = score;
        this.content = content;
    }

    public static TuteeReviewCreateRequest of(Integer score, String content) {
        return TuteeReviewCreateRequest.builder()
                .score(score)
                .content(content)
                .build();
    }
}
