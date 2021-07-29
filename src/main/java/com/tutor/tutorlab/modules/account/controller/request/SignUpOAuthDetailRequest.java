package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class SignUpOAuthDetailRequest {

    private String gender;
    private String phoneNumber;

    @Email
    private String email;
    private String nickname;
    private String bio;

    private String zone;

    @Builder
    public SignUpOAuthDetailRequest(String gender, String phoneNumber, @Email String email, String nickname, String bio, String zone) {
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
    }
}
