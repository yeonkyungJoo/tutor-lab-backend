package com.tutor.tutorlab.config.security.oauth.provider.google;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;

import java.util.Map;

public class GoogleInfo implements OAuthInfo {

    private Map<String, String> userInfo;

    public GoogleInfo(Map<String, String> userInfo) {
        this.userInfo = userInfo;
    }

    /*
    {
        id=109497631191479413556,
        email=dev.yk2021@gmail.com,
        verified_email=true,
        name=yk dev,
        given_name=yk,
        family_name=dev,
        picture=https://lh3.googleusercontent.com/a/AATXAJwH1OzOPyWFMo8BgTGs_JYle0po58A7PxaRj-0X=s96-c,
        locale=ko
    }
    */
    @Override
    public String getProviderId() {
        return userInfo.get("id");
    }

    @Override
    public OAuthType getProvider() {
        return OAuthType.GOOGLE;
    }

    @Override
    public String getName() {
        return userInfo.get("name");
    }

    @Override
    public String getEmail() {
        return userInfo.get("email");
    }
}
