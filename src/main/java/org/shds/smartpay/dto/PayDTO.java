package org.shds.smartpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayDTO { //카드사에 보낼 정보
    private String product;
    private Integer price;
    private String cardNo;
    private String payDate;
    private String franchiseCode;
    private String franchiseName;
    private String requestName;
}
