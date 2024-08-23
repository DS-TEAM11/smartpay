package org.shds.smartpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerDTO {
    //접속한 링크의 구매자 memberNo
    private String memberNo;

    //AI 필요한 정보
    private String franchiseCode; //프랜차이즈코드 테이블 참고
    private String franchiseType; //편의점 등
    private String franchiseName; //GS25 등
    private String purchaseItems; //판매 상품명
    private int purchasePrice; //판매 가격
}
