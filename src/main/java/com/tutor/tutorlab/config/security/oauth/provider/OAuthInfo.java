package com.tutor.tutorlab.config.security.oauth.provider;

public interface OAuthInfo {

    public static OAuthType getOAuthType(String provider) {

        switch (provider) {
            case "google":
                return OAuthType.GOOGLE;
            case "naver":
                return OAuthType.NAVER;
            case "kakao":
                return OAuthType.KAKAO;
            default:
                return null;
        }
    }

    public String getProviderId();
    public OAuthType getProvider();
    public String getName();
    public String getEmail();

}
