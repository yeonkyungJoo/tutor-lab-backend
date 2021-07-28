package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateRequest {

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
