package org.shds.smartpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayInfoDTO {
    private String orderNo;
    private String product;
    private Integer price;
    private String cardNo;
    private String cardCode;
    private boolean getIsAi;
    private String payDate;
    private Integer savePrice;
    private Integer saveType;
    private String franchiseCode;
    private String franchiseName;
    private String memberNo;
    private String cardImage;
}
