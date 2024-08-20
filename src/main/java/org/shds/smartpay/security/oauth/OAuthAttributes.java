package org.shds.smartpay.security.oauth;

import lombok.Builder;
import lombok.Getter;
import org.shds.smartpay.entity.Member;
import org.shds.smartpay.entity.MemberRole;
import org.shds.smartpay.security.oauth.userinfo.KakaoOAuth2UserInfo;
import org.shds.smartpay.security.oauth.userinfo.OAuth2UserInfo;
import org.shds.smartpay.security.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.UUID;

/**
 * 소셜 타입별로 데이터를 받는 데이터를 분기 처리하는 DTO 역할 클래스
 * 일단은 카카오가 제공하는 응답 Json의 형태에 적응할 수 있도록 지정한다.
 */
@Getter
public class OAuthAttributes {
    private String nameAttributeKey; // OAuth2 로그인 진행 시 키가 되는 필드 값, PK와 같은 의미
    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Builder //DTO 빌더
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     *OAuthAttributes 객체 반환
     * 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 / attributes : OAuth 서비스의 유저 정보들
     * 소셜별 of 메소드( ofKaKao) 들은 각각 소셜 로그인 API에서 제공하는
     * 회원의 식별값(id), attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {
        // 소셜 종류를 늘리고 싶으면 여기에서 매개변수로 소셜이름을 받아서 메서드 분기하면됨
        return ofKakao(userNameAttributeName, attributes);
    }

    //카카오의 경우 key와 info를 설정해서 OAuthAttributes를 생성해서 리턴한다. ( 즉. 카카오의 정보를 알맞게 parsing해서 DTO에 담는 역할)
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, email 을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     * 추후 CustomOAuth2UserService에서 Member 객체를 만들어서 DB에 저장하기 위한 메서드
     */
    public Member toEntity( OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .memberNo(UUID.randomUUID().toString())
                .socialId(oauth2UserInfo.getId())
                .email(oauth2UserInfo.getEmail())
                //.email(UUID.randomUUID() + "@socialUser.com") // 요건 이메일 안받는 경우에 이렇게 하면됨
                .name(oauth2UserInfo.getNickname())
                .regUser("Developer Test")
                //.imageUrl(oauth2UserInfo.getImageUrl()) 일단 이미지 url은 사용안함
                .password(passwordEncoder().encode(PasswordUtil.generateRandomPassword()))  // 소셜 회원에게 비밀번호가 필요없긴해서 일단 안쓰지만 필요하면 이렇게 쓸거임
                //.role(MemberRole.GUEST) // 일단 소셜 도중이므로 GUEST로 넣어줌
                .role(MemberRole.USER)
                .fromSocial(true)
                .build();
    }

}

