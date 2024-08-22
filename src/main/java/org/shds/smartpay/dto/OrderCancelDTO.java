package org.shds.smartpay.dto;

import lombok.Data;

@Data
public class OrderCancelDTO {

    private String orderId;
    private String email;
    private int totalPrice;
}
