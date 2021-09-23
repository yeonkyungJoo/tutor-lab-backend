package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.account.vo.Career;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Data;

@Data
public class CareerResponse {

    private String companyName;
    private String duty;
    private String startDate;
    private String endDate;
    private boolean present;

    public CareerResponse(Career career) {
        this.companyName = career.getCompanyName();
        this.duty = career.getDuty();
        this.startDate = LocalDateTimeUtil.getDateToString(career.getStartDate());
        this.endDate = LocalDateTimeUtil.getDateToString(career.getEndDate());
        this.present = career.isPresent();
    }
}
