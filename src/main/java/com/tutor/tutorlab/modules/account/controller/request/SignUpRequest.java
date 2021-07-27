package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank(message = "이메일 형식의 아이디를 입력해주세요.")
    @Email
    private String username;    // 아이디
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String passwordConfirm;

    @NotBlank
    private String name;
    private String gender;
    private String phoneNumber;
    @Email
    private String email;
    private String nickname;
    private String bio;

    private String zone;

}
