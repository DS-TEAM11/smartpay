package org.shds.smartpay.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private String franchiseName;
    private String franchiseCode;
    private String memberNo;
    private String cardImage;
    private String cardName;
}
