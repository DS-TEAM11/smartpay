package org.shds.smartpay.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardDTO {

    @Id
    @Column(length = 16)  // 카드번호는 16자리로 제한
    private String cardNo;

    private String cardNick;  // 카드 별칭

    private int isCredit;  // 카드사

    private String cardPwd;  // 카드 비밀번호 (2자리)

    private String validPeriod;  // 유효기간 (MM/YY)

    private String regUser;  // 등록 사용자

    private String cardCode;  // 카드 코드

    private String memberNo;  // 회원 번호

    private String cardImage; //카드이미지



}
