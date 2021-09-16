package com.tutor.tutorlab.modules.review.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class TuteeReviewCreateRequest {

    @Min(0) @Max(5)
    @NotNull
    private Integer score;

    @NotBlank
    private String content;

    @Builder
    public TuteeReviewCreateRequest(@Min(0) @Max(5) @NotNull Integer score, @NotBlank String content) {
        this.score = score;
        this.content = content;
    }
}
