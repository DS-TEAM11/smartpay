package org.shds.smartpay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class CardInfo {

    @Id
    private String cardCode;

    //카드 이름
    private String cardName;
    //카드사
    private String cardCompany;
    //카드 카테고리
    private String cardCategory;
    //카드혜택1
    private String cardBenefit1;
    //카드혜택2
    private String cardBenefit2;
    //카드혜택3
    private String cardBenefit3;
    //카드이미지
    private String cardImg;
    //카드가입주소
    private String cardLink;
    //신용카드(0:다른카드. 1:신용카드)
    private int isCredit;

}
