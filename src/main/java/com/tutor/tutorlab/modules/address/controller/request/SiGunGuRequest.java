package com.tutor.tutorlab.modules.address.controller.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SiGunGuRequest {
    @NotBlank(message = "시/도를 입력해주세요.")
    private String state;
}
