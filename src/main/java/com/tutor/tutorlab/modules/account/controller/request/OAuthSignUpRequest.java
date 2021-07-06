package com.tutor.tutorlab.modules.account.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthSignUpRequest {

    private String provider;
    private String providerId;

}
