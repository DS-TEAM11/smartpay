package org.shds.smartpay.websocket;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("Connected: {}", connectedHeaders.toString());
        log.info("###Session: {}", session.toString());
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        log.info("Received frame: Headers={}, Payload={}", headers.toString(), payload.toString());
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("WebSocket Error", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Transport Error", exception);
    }
}
