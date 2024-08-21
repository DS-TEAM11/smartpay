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
    //구매자 정보 포함해서 보내기
    private String email;
    //order_no QR 생성 시
    private String orderNo;

    //AI 필요한 정보
    private String franchiseCode; //프랜차이즈코드 테이블 참고
    private String franchiseType; //편의점 등
    private String franchiseName; //GS25 등
    private String purchaseItems; //판매 상품명
    private int purchasePrice; //판매 가격
}
