package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateRequest {

    @ApiModelProperty(value = "연락처", example = "010-1111-2222", required = false)
    private String phoneNumber;

    @ApiModelProperty(value = "이메일", example = "email@email.com", required = false)
    @Email
    private String email;

    @ApiModelProperty(value = "닉네임", example = "nickname", required = false)
    private String nickname;

    @ApiModelProperty(value = "성별", example = "MALE", required = false)
    private String bio;

    // TODO - CHECK : 지역 입력 형식 확인
    @ApiModelProperty(value = "지역", example = "서울특별시 서초구", required = false)
    private String zone;

    private String image;

    @Builder(access = AccessLevel.PRIVATE)
    public UserUpdateRequest(String phoneNumber, String email, String nickname, String bio, String zone, String image) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
        this.image = image;
    }

    public static UserUpdateRequest of(String phoneNumber, String email, String nickname, String bio, String zone, String image) {
        return UserUpdateRequest.builder()
                .phoneNumber(phoneNumber)
                .email(email)
                .nickname(nickname)
                .bio(bio)
                .zone(zone)
                .image(image)
                .build();
    }

}
