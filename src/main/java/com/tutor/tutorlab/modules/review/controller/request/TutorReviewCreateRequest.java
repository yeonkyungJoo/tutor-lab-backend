package com.tutor.tutorlab.modules.review.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class TutorReviewCreateRequest {

    @NotBlank
    private String content;

    @Builder
    public TutorReviewCreateRequest(@NotBlank String content) {
        this.content = content;
    }
}
