package org.shds.smartpay.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{
    @Id
    private String memberNo;

    private String email;
    private String password;
    private String name;
    private String phone;

    private boolean fromSocial;
    private String regUser;
    private String payPwd;

    private String refreshToken;
    private String socialId;

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    private MemberRole role;     // 역할 필드 추가

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = MemberRole.USER;
    }

    // 결제 비밀번호 설정 메소드
    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void paypwdEncode(PasswordEncoder passwordEncoder) {
        this.payPwd = passwordEncoder.encode(this.payPwd);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

}