package org.shds.smartpay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryDTO {
    private Integer payNo;
    private String orderNo;
    private String product;
    private Integer price;
    private String cardNo;
    private Integer cardCode;
    private boolean getIsAi;
    private Integer gptState;
    private Integer approval;
    private String payDate;
    private LocalDateTime regDate;
    private Integer savePrice;
    private Integer saveType;
    private Integer franchiseNo;
    private String memberNo;
}
