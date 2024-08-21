package org.shds.smartpay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Card extends BaseEntity {

    @Id
    @Column(length = 16)  // 카드번호는 16자리로 제한
    private String cardNo;

    private String cardNick;  // 카드 별칭

    private int isCredit;  // 카드사

    private String cardPwd;  // 카드 비밀번호 (2자리)

    private String validPeriod;  // 유효기간 (MM/YY)

    private String regUser;  // 등록 사용자

    private String cardCode;  // 카드 코드

    private int memberNo;  // 회원 번호
}
