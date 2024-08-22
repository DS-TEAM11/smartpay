package org.shds.smartpay.websocket;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.shds.smartpay.dto.SellerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessageSendingOperations template;

    // 채팅 리스트 반환
    @GetMapping("/ws")
    public ResponseEntity<String> getChatMessages(@RequestParam SellerDTO sellerDto) {

//        template.convertAndSend("/topic/sellinfo", sellerDto);//TODO:이게 뭔가요?
        System.out.println("웹소켓 접속함");
        System.out.println(sellerDto);
        return ResponseEntity.ok().body("sellerDto.toString()");
    }

    @MessageMapping("/sellinfo")
    @SendTo("/topic/sellinfo")
    public ResponseEntity<Void> receiveMessage(@RequestBody JSONObject message) {
        System.out.println("###############################################################################");
        System.out.println(message.toString());
        System.out.println("###############################################################################");
        // 메세지를 sellinfo 구독자에게 전송
        template.convertAndSend("/queue/sellinfo", message.toString());
        return ResponseEntity.ok().build();
    }

    //구매자는 판매자한테 보낼 필요 x? ㅇㅇㅇ 구매자 <-> 서버, 판매자 <-> 서버만
    // 판매자가 정보 입력 완료 -> 서버에서 인식 -> 서버가 구매자한테 전달
    // 구매자가 결제 완료 -> 서버에서 인식 -> 서버가 판매자한테 전달
    //위 두개만 웹소켓에서 왔다갔다 하면 될 것 같은데
}
