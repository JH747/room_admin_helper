package com.example.backend_spring;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

// deprecated
// 스레드 로컬 문제로 인해 securitycontextholder가 각 메시지 처리 스레드에서 공유되지 않음.
// websocket 메시지 처리시 다른 스레드가 사용되므로 securitycontextholder가 비어있음.
// 따라서 handshakeinterceptor 사용을 포기하고 configureClientInboundChannel를 사용한다.

//@Component
//public class JWTHandshakeInterceptor implements HandshakeInterceptor {
//
//    private final JWTUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//
//    public JWTHandshakeInterceptor(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
//
//
//
////        String authHeader = request.getHeaders().getFirst("Authorization");
////        if (authHeader == null) {
////            System.out.println("Authorization header missing or invalid");
////            return false; // 연결 차단
////        }
//
//        // bearer 미포함
//        String token;
//        try{
//            token = request.getURI().getQuery().split("token=")[1];
//        } catch (Exception e){
//            System.out.println("Authorization header missing or invalid");
//            return false; // 연결 차단
//        }
//
//
//        String jwt = token;
//        String subject = jwtUtil.extractUsername(jwt);
//
//        if (subject != null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
//            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities()
//                );
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                return true;
//            }
//        }
//
//        return false; // JWT 검증 실패 시 연결 차단
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
//                               WebSocketHandler wsHandler, Exception exception) {
//        // No-op
//    }
//}

