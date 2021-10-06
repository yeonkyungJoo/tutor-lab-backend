package com.tutor.tutorlab.modules.review.controller.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TutorReviewCreateRequest {

    @NotBlank
    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    public TutorReviewCreateRequest(@NotBlank String content) {
        this.content = content;
    }

    public static TutorReviewCreateRequest of(String content) {
        return TutorReviewCreateRequest.builder()
                .content(content)
                .build();
    }
}
