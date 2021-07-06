package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;    // 아이디

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String passwordConfirm;

    private String email;

    @NotBlank
    private String name;
    private String gender;
}
