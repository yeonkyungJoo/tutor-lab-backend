package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SignUpRequest {

    // TODO - 입력값 체크
    /*
        - 비밀번호 제한
        - 연락처 정규식 체크
        - 이름 확인
        - 닉네임 확인 / 중복 체크
    */

    @ApiModelProperty(value = "아이디", example = "sh@email.com", required = true)
    @NotBlank(message = "이메일 형식의 아이디를 입력해주세요.")
    @Email
    private String username;

    @ApiModelProperty(value = "비밀번호", example = "password", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @ApiModelProperty(value = "비밀번호 확인", example = "password", required = true)
    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

    @ApiModelProperty(value = "이름", example = "sh", required = true)
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @ApiModelProperty(value = "성별", example = "MALE", required = false)
    private String gender;

    @ApiModelProperty(value = "생년월일", example = "2020-01-01", required = false)
    @Size(min = 10, max = 10)
    private String birth;

    @ApiModelProperty(value = "연락처", example = "010-1111-2222", required = false)
    private String phoneNumber;

    @ApiModelProperty(value = "이메일", example = "sh@email.com", required = false)
    @Email
    private String email;

    @ApiModelProperty(value = "닉네임", example = "sh", required = true)
    @NotBlank
    private String nickname;

    @ApiModelProperty(value = "소개글", example = "안녕하세요", required = false)
    private String bio;

    @ApiModelProperty(value = "지역", example = "서울특별시 종로구 효자동", required = false)
    @NotBlank
    private String zone;

    private String image;

    @Builder
    public SignUpRequest(@NotBlank(message = "이메일 형식의 아이디를 입력해주세요.") @Email String username, @NotBlank(message = "비밀번호를 입력해주세요.") String password, @NotBlank(message = "비밀번호 확인을 입력해주세요.") String passwordConfirm, @NotBlank(message = "이름을 입력해주세요.") String name, String gender, @Size(min = 10, max = 10) String birth, String phoneNumber, @Email String email, @NotBlank String nickname, String bio, @NotBlank String zone, String image) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
        this.image = image;
    }

    @AssertTrue
    private boolean isPassword() {
        return getPassword().equals(getPasswordConfirm());
    }
}
