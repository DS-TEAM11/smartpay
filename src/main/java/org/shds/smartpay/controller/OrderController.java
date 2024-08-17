//package org.shds.smartpay.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class OrderController {
//
//    @MessageMapping("/order-complete-message/{storeId}") // /pub/이 빠진거랑 같음
//    @SendTo("/sub/order-complete/{storeId}")
//    public String message(@DestinationVariable long storeId, String message) {
//
//        System.out.println("가맹점 번호: " + storeId);
//        System.out.println("메세지 도착: " + message);
//
//        return message;
//    }
//}
