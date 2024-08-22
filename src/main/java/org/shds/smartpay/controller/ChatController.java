package org.shds.smartpay.controller;

import lombok.RequiredArgsConstructor;
import org.shds.smartpay.dto.ChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;

    // 채팅 리스트 반환
    @GetMapping("/chat/{id}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long id) {

        // 임시 리스트 형식 구현, DB로 저장 안할거임
        ChatMessage test = new ChatMessage(1L, "test", "test");

        return ResponseEntity.ok().body(List.of(test));
    }

    // 메세지 sub/pub, /pub 생략된 모습, client에서는 /pub/message로 요청
    @MessageMapping("message")
    public ResponseEntity<Void> receiveMessage(@Payload ChatMessage chat) {

        // 메세지를 topic 구독자에게 전송
        template.convertAndSend("/sub/chatroom/1", chat);

        return ResponseEntity.ok().build();
    }
}
