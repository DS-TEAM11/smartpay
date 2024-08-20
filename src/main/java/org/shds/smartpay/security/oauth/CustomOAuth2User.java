package org.shds.smartpay.security.oauth;

import lombok.Getter;
import org.shds.smartpay.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * DefaultOAuth2User를 상속하고, email과 role 필드를 추가로 가진다.
 * OAuth로 가입한 회원에게 추후 휴대폰 번호를 받기 위해 미리 게스트,임시 이메일을 지정해두고
 * role이 게스트라면 전화번호를 입력받도록 보내주기 위한 OAuth2User extends 클래스이다.
 * 하지만 당분간 따로 폰 번호를 받지 않을 것이기 때문에 일단은 최초부터 role=User, email=이메일
 * 로 설정해서 바로 가입 시킬 예정이다.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private String email;
    private MemberRole role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, MemberRole role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }

}
