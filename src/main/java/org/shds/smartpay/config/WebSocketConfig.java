package org.shds.smartpay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //메세지브로커 등록
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/");
    }
            /*
        /topic은 message를 뿌려야 하는 경우 -> 각 판매자, 구매자가 서버에 뿌림이겠네
        /queue는 message 발행한 사람에게 정보를 보내는 경우 -> 판매자 구매자가 서버에게 정보 받음
        / QR생성 -> 판매자 접속 -> 판매자 정보 입력 완료 -> 구매자 카드 선택 -> 결제
        /구매자가 QR 생성 -> /topic 전달(QR생성) -> 서버에서 인식하고 있음(별도 DB 등록 X)
        /판매자가 QR 인식 후 사이트 접속 -> /topic zz전달(접속) -> /topic 전달(정보 입력)
        /서버에서 받은 topic에 담긴 데이터를 통해 추천 로직 -> 카드 추천/카드 선택
        /구매자가 카드 선택 -> /topic 전달(결제)
        /결제 완료 시 서버에서 구매자, 판매자 모두에게 /queue 전달(결제 완료-성공/실패)
         */

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ws -> endpoint(요청이 들어오는 서버 클래스)
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000") //CORS (일단은 interceptor X)
                .withSockJS();
    }
    //실제 경로는 /ws/topic/~
}