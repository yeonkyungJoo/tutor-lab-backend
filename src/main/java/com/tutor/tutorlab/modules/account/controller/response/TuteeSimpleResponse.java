package com.tutor.tutorlab.modules.account.controller.response;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class TuteeSimpleResponse {

    private Long tuteeId;
    private Long userId;
    private String name;
    // private List<Long> lectureIds;

    @Builder
    private TuteeSimpleResponse(Long tuteeId, Long userId, String name) {
        this.tuteeId = tuteeId;
        this.userId = userId;
        this.name = name;
    }
}
