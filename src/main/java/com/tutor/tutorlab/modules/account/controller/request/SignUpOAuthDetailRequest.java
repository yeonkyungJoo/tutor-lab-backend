package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpOAuthDetailRequest {

    @ApiModelProperty(value = "성별", example = "MALE", required = false)
    private String gender;

    @ApiModelProperty(value = "생년월일", example = "2020-01-01", required = false)
    @Size(min = 10, max = 10)
    private String birth;

    @ApiModelProperty(value = "연락처", example = "010-1234-5678", required = false)
    private String phoneNumber;

    @ApiModelProperty(value = "이메일", example = "sj@email.com", required = false)
    @Email
    private String email;

    @ApiModelProperty(value = "닉네임", example = "sj", required = true)
    @NotBlank
    private String nickname;

    @ApiModelProperty(value = "소개글", example = "안녕하세요", required = false)
    private String bio;

    @ApiModelProperty(value = "지역", example = "서울특별시 종로구 효자동", required = false)
    @NotBlank
    private String zone;

    private String image;

    @Builder(access = AccessLevel.PRIVATE)
    public SignUpOAuthDetailRequest(String gender, @Size(min = 10, max = 10) String birth, String phoneNumber, @Email String email, @NotBlank String nickname, String bio, @NotBlank String zone, String image) {
        this.gender = gender;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
        this.image = image;
    }

    public static SignUpOAuthDetailRequest of(String gender, String birth, String phoneNumber, String email, String nickname, String bio, String zone, String image) {
        return SignUpOAuthDetailRequest.builder()
                .gender(gender)
                .birth(birth)
                .phoneNumber(phoneNumber)
                .email(email)
                .nickname(nickname)
                .bio(bio)
                .zone(zone)
                .image(image)
                .build();
    }
}
