package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CareerCreateRequest {

    private String companyName;
    private String duty;        // 직급
    private String startDate;   // ex) "2007-12-03"
    private String endDate;
    private boolean present;

    @Builder
    public CareerCreateRequest(String companyName, String duty, String startDate, String endDate, boolean present) {
        this.companyName = companyName;
        this.duty = duty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.present = present;
    }
}
