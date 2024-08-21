package org.shds.smartpay.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CardBenefits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //카드 코드
    private String cardCode;
    //혜택 타입 (할인, 적립, 캐시백)
    private String benefitType;
    //프랜차이즈코드 (테이블 참고)
    private String franchiseCode;
    //혜택 시작 금액
    private int spendRange_start;
    //혜택 종료 금액
    private int spendRange_end;
    //할인 비율
    private double discountRate;
    //최대 할인 금액
    private int maxDiscount;
    //적립 비율
    private double pointRate;
    //최대 적립 금액
    private int maxPoint;
    //상세 내용, 주의사항
    private String caution;

}