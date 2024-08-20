package org.shds.smartpay.security.oauth.userinfo;

import java.util.Map;
//소셜에서 제공하는 정보를 받기 위한 틀 , 추상 클래스
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별 값 :  카카오 - "id"

    public abstract String getNickname(); // 사용자 닉네임

    public abstract String getEmail(); //사용자 이메일

}
