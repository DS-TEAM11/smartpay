package org.shds.smartpay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyStaticDTO {
    private String franchiseCode; // 조건문으로 매칭 시켜야함;;
    private Double thisMonth; // 이번 달의 결제 총액
    private Double lastMonth; // 지난 달의 결제 총액
    private Double thisMonthTotal; // 이번 달의 전체 총액
    private Double lastMonthTotal; // 지난 달의 전체 총액
}
