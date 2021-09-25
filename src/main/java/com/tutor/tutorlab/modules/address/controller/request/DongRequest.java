package com.tutor.tutorlab.modules.address.controller.request;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

@Getter
public class DongRequest {

    @NotBlank(message = "검색할 시/도를 입력해주세요.")
    private String state;

    private String siGun;

    private String gu;

    // TODO - CHECK : -Valid
    @AssertTrue(message = "검색할 시/군 혹은 구를 입력해주세요.")
    private boolean isSiGunGuValid() {
        if (StringUtils.isBlank(siGun) && StringUtils.isBlank(gu)) {
            return false;
        }
        return true;
    }
}
