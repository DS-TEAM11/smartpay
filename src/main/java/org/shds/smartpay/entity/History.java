package org.shds.smartpay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pay_history")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payNo;

    private String orderNo;

    private String product;

    private Integer price;

    private String cardNo;
    private String cardCode;

    private boolean isAi;
    private Integer gptState;

    private Integer approval;

    private String payDate;

    private Integer savePrice;
    private Integer saveType;
    private String franchiseCode;
    private String memberNo;

}
