package com.tutor.tutorlab.modules.account.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateRequest {

    @ApiModelProperty(value = "연락처", example = "010-1111-2222", required = false)
    private String phoneNumber;

    @ApiModelProperty(value = "이메일", example = "email@email.com", required = false)
    private String email;

    @ApiModelProperty(value = "닉네임", example = "nickname", required = false)
    private String nickname;

    @ApiModelProperty(value = "성별", example = "MALE", required = false)
    private String bio;

    // TODO - CHECK : 지역 입력 형식 확인
    @ApiModelProperty(value = "지역", example = "서울특별시 서초구", required = false)
    private String zone;

    @Builder
    public UserUpdateRequest(String phoneNumber, String email, String nickname, String bio, String zone) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
    }
}
