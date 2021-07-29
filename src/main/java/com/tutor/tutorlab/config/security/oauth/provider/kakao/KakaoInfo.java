package com.tutor.tutorlab.config.security.oauth.provider.kakao;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;

import java.util.LinkedHashMap;
import java.util.Map;

public class KakaoInfo implements OAuthInfo {

    private KakaoResponse userInfo;

    /*
        {
            id=1825918761,
            connected_at=2021-07-28T21:58:30Z,
            properties={
                nickname=dev.yk
            },
            kakao_account={
                profile_nickname_needs_agreement=false,
                profile={
                    nickname=dev.yk
                },
                has_email=true,
                email_needs_agreement=false,
                is_email_valid=true,
                is_email_verified=true,
                email=dev.yk2021@gmail.com
            }
        }
    */

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
