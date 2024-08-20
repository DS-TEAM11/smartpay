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

    @Column(name="product")
    private String product;

    @Column(name = "price")
    private Integer price;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name ="card_code")
    private Integer cardCode;

    @Column(name = "is_ai")
    private Boolean isAi;

    @Column(name = "pay_date")
    private String payDate;

    @Column(name = "save_price")
    private Integer savePrice;

    @Column(name = "save_type")
    private Integer saveType;

    @Column(name = "franchise_no")
    private Integer franchiseNo;

    @Column(name = "member_no")
        private String memberNo;
}
