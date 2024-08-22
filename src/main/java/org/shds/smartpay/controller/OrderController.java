package org.shds.smartpay.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    @MessageMapping("/order-complet/{orderId}")
    @SendTo("/sub/order-complete/{orderId}")
    public String order(@DestinationVariable String orderId, String message) {

        System.out.println("주문번호: " + orderId);
        System.out.println("메세지 도착: " + message);

        return message;
    }
}
