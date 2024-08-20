package org.shds.smartpay.dto;

import jakarta.persistence.Id;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardInfoDTO {

    @Id
    private String cardCode;

    private String cardName;
    private String cardCompany;
    private String cardCategory;
    private String cardBenefit1;
    private String cardBenefit2;
    private String cardBenefit3;
    private String cardImg;
    private String cardLink;
    private String isCredit;

}
