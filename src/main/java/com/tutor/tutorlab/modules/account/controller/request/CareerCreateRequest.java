package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
public class CareerCreateRequest {

    @ApiModelProperty(value = "회사명", example = "tutorlab", required = true)
    @NotBlank
    private String companyName;

    @ApiModelProperty(value = "직급", example = "engineer", required = true)
    @NotBlank
    private String duty;

    @ApiModelProperty(value = "입사일자", example = "2007-12-03", required = true)
    @NotBlank
    private String startDate;

    @ApiModelProperty(value = "퇴사일자", example = "2007-12-10", allowEmptyValue = true, required = false)
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

    @AssertTrue
    private boolean isEndDate() {
        boolean valid = true;

        // - if present is true, endDate must be blank
        // - if present is false, endDate must not be blank
        if ((isPresent() && StringUtils.isNotEmpty(getEndDate())) ||
                (!isPresent() && StringUtils.isEmpty(getEndDate()))) {
            valid = false;
            return valid;
        }

        try {

            LocalDate startDate = LocalDate.parse(getStartDate());
            LocalDate endDate = null;

            if (StringUtils.isNotEmpty(getEndDate())) {
                endDate = LocalDate.parse(getEndDate());
                // - startDate < endDate
                valid = startDate.isBefore(endDate);
            }

        } catch (DateTimeParseException e) {
            valid = false;
        }

        return valid;
    }
}
