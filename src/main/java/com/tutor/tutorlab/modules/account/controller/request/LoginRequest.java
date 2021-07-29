package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "이메일 형식의 아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public LoginRequest(@NotBlank(message = "이메일 형식의 아이디를 입력해주세요.") String username, @NotBlank(message = "비밀번호를 입력해주세요.") String password) {
        this.username = username;
        this.password = password;
    }
}
