package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@Data
@NoArgsConstructor
public class CareerCreateRequest {

    // TODO - validation
    // - startDate < endDate
    // - if present is true, endDate must not be blank

    @ApiModelProperty(value = "회사명", example = "tutorlab", required = true)
    @NotBlank
    private String companyName;

    @ApiModelProperty(value = "직급", example = "engineer", required = true)
    @NotBlank
    private String duty;

    @ApiModelProperty(value = "입사일자", example = "2007-12-03", required = true)
    @Past @NotBlank
    private String startDate;

    @ApiModelProperty(value = "퇴사일자", example = "2007-12-10", required = false)
    private String endDate;

    @ApiModelProperty(value = "재직 여부", example = "false", required = true)
    @NotNull
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
