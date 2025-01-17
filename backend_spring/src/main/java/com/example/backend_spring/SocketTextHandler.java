package com.example.backend_spring;

import jakarta.mail.Message;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// deprecated

//public class SocketTextHandler extends TextWebSocketHandler {
//
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        // 클라이언트가 연결되었을 때 세션을 저장
//        sessions.add(session);
//        System.out.println("New connection: " + session.getId());
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // 클라이언트로부터 메시지 수신 시
//        System.out.println("Received message: " + message.getPayload());
//        JSONObject jsonObj = new JSONObject(message.getPayload());
//        System.out.println(jsonObj.getString("message"));
//        // 모든 클라이언트에게 메시지 브로드캐스트
//        for (WebSocketSession s : sessions) {
//            if (s.isOpen()) {
//                s.sendMessage(new TextMessage(jsonObj.toString()));
//                s.sendMessage(new TextMessage("Server response: " + message.getPayload()));
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        // 클라이언트 연결이 종료되었을 때 세션 제거
//        sessions.remove(session);
//        System.out.println("Connection closed: " + session.getId());
//    }
//
//
//}
