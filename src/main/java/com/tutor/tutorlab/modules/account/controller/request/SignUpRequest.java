package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
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

    @Builder
    public SignUpRequest(@NotBlank(message = "이메일 형식의 아이디를 입력해주세요.") @Email String username, @NotBlank(message = "비밀번호를 입력해주세요.") String password, @NotBlank(message = "비밀번호를 입력해주세요.") String passwordConfirm, @NotBlank String name, String gender, String phoneNumber, @Email String email, String nickname, String bio, String zone) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
    }
}
