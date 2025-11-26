package com.example.biddora_backend.common.handlers;

import com.example.biddora_backend.bid.dto.BidDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketConnectionHandler extends TextWebSocketHandler {

    private final Map<Long, List<WebSocketSession>> sessionsByProduct = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public SocketConnectionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println(session.getId() + " Connected");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long productId = objectMapper.readTree(message.getPayload()).get("productId").asLong();

        // Register session for productId
        List<WebSocketSession> sessions = sessionsByProduct
                .computeIfAbsent(productId, k -> Collections.synchronizedList(new ArrayList<>()));

        if (!sessions.contains(session)) {
            sessions.add(session);
        }

        System.out.println("Session " + session.getId() + " subscribed to product " + productId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        for (List<WebSocketSession> sessions : sessionsByProduct.values()) {
            sessions.remove(session);
        }
        System.out.println(session.getId() + " Disconnected");
    }

    public void sendBidToProduct(BidDto bidDto) throws IOException {
        List<WebSocketSession> sessions = sessionsByProduct.getOrDefault(bidDto.getProductId(), List.of());
        String json = objectMapper.writeValueAsString(bidDto);

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(json));
            }
        }
    }
}
