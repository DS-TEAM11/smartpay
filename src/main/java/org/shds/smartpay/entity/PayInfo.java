package org.shds.smartpay.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "pay_info")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PayInfo extends BaseEntity {

    @Id
    private String orderNo;

    private String product;

    private Integer price;

    private String cardNo;

    private String cardCode;

    private Boolean isAi;

    private String payDate;

    private Integer savePrice;

    private Integer saveType;

    private String franchiseCode;
    private String franchiseName;
    private String memberNo;
}
