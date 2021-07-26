package com.tutor.tutorlab.config.security.oauth.provider.kakao;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;

public class KakaoInfo implements OAuthInfo {

    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public OAuthType getProvider() {
        return OAuthType.KAKAO;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }
}
