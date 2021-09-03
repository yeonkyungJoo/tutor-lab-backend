package com.tutor.tutorlab.config.security.oauth.provider.kakao;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;

import java.util.LinkedHashMap;
import java.util.Map;

public class KakaoInfo implements OAuthInfo {

    private KakaoResponse userInfo;

    public KakaoInfo(KakaoResponse userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String getProviderId() {
        return Long.toString(userInfo.getId());
    }

    @Override
    public OAuthType getProvider() {
        return OAuthType.KAKAO;
    }

    @Override
    public String getName() {
        return userInfo.getKakao_account().getProfile().getNickname();
    }

    @Override
    public String getEmail() {
        return userInfo.getKakao_account().getEmail();
    }
}
