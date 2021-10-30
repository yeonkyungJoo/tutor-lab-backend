package com.tutor.tutorlab.modules.review.controller.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TutorReviewUpdateRequest {

    @NotBlank
    private String content;

    @Builder(access = AccessLevel.PRIVATE)
    private TutorReviewUpdateRequest(@NotBlank String content) {
        this.content = content;
    }

    public static TutorReviewUpdateRequest of(String content) {
        return TutorReviewUpdateRequest.builder()
                .content(content)
                .build();
    }
}
