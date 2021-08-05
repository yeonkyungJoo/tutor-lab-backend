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

    private String email;
    private String nickname;
    private String bio;
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
