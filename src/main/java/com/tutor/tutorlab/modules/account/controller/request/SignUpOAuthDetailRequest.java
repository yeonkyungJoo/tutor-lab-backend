package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class SignUpOAuthDetailRequest {

    private String gender;
    private String phoneNumber;

    @Email
    private String email;
    private String nickname;
    private String bio;

    private String zone;

}
