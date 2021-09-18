package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class SignUpOAuthDetailRequest {

    @ApiModelProperty(value = "성별", example = "MALE", required = false)
    private String gender;

    @ApiModelProperty(value = "연락처", example = "010-1234-5678", required = false)
    private String phoneNumber;

    @ApiModelProperty(value = "이메일", example = "sj@email.com", required = false)
    @Email
    private String email;

    @ApiModelProperty(value = "닉네임", example = "sj", required = false)
    private String nickname;

    @ApiModelProperty(value = "소개글", example = "안녕하세요", required = false)
    private String bio;

    @ApiModelProperty(value = "지역", example = "서울특별시 강남구", required = false)
    private String zone;

    private String image;

    @Builder
    public SignUpOAuthDetailRequest(String gender, String phoneNumber, @Email String email, String nickname, String bio, String zone, String image) {
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
        this.image = image;
    }
}
