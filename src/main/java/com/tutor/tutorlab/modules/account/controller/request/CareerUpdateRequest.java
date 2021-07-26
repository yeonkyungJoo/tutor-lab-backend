package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CareerUpdateRequest {

    private String companyName;
    private String duty;    // 직급
    private String startDate;
    private String endDate;
    private boolean present;

    @Builder
    public CareerUpdateRequest(String companyName, String duty, String startDate, String endDate, boolean present) {
        this.companyName = companyName;
        this.duty = duty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.present = present;
    }
}
