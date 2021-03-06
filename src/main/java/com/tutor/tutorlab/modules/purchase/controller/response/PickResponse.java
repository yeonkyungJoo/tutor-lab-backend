package com.tutor.tutorlab.modules.purchase.controller.response;

import com.tutor.tutorlab.modules.purchase.vo.Pick;
import lombok.Data;

@Data
public class PickResponse {

    private String lectureTitle;

    // TODO - 쿼리
    public PickResponse(Pick pick) {
        this.lectureTitle = pick.getLecture().getTitle();
    }
}
