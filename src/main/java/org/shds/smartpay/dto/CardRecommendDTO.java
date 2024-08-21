package org.shds.smartpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardRecommendDTO {
    private String recommendCard;
    private int maximumBenefits;
    private String benefitType;
    private String explanation;
    private String detailExplanation;
    private String caution;
}