package org.shds.smartpay.security.oauth.userinfo;

import java.util.Map;
//카카오에서 제공하는 응답 로그인 정보를 받기위한 클래스
//getter를 통해서 닉네임,이메일,소셜id를 가져올 수 있음
public class KakaoOAuth2UserInfo extends OAuth2UserInfo{
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickname() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null || profile == null) {
            return null;
        }

        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        //Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        if (account == null) {
            return null;
        }

        return (String) account.get("email");
    }
}
