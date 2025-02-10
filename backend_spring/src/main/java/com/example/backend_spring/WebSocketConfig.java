package com.example.backend_spring;


import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

// websocket만 사용시
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new SocketTextHandler(), "/test/websocket")
//                .setAllowedOrigins("*"); // cors 설정
//
//        // websocket, sockjs 프로토콜은 호환되지 않으므로 엔드포인트를 분리해야 한다.
//        registry.addHandler(new SocketTextHandler(), "/test/websocket-sockjs")
//                .setAllowedOrigins("*")
//                .withSockJS(); // 웹소켓 미지원 브라우저 환경 위한 sockJS 지원
//
//    }
//    // 이거 대신 STOMP로 AnalysisController 재작성할 것
//}

// STOMP 사용시
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public WebSocketConfig(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // websocket 엔드포인트 설정
//                .addInterceptors(jwtHandshakeInterceptor) // deprecated
                .setAllowedOrigins("*"); // cors 설정
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 내장 브로커 사용, prefix가 붙은 메시지를 발행시 브로커가 처리
        // topic은 메시지가 1-to-1으로, queue는 메시지가 1-to-many로 broadcast될 때 주로 사용
        registry.enableSimpleBroker("/topic", "/queue");
        // 메시지 처리, 가공이 필요한 경우 핸들러를 거쳐가게 할 수 있음
        // 이때 메시지 핸들러로 라우팅되는 prefix
        registry.setApplicationDestinationPrefixes("/app"); // publishers should use this publishing prefix
    }

    // 일반 http request로 pre-handshake authentication을 거친 클라이언트에게만 ws 연결을 허용하는 방식도 있으나 user가 누구인지 전달해야 하므로 기각
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                log.info(message.getHeaders().toString());

                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if(StompCommand.CONNECT.equals(accessor.getCommand())){ // CONNECT 단계에서 차단
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        throw new AccessDeniedException("Authorization header is incorrect");
                    }
                    String jwt = authHeader.substring(7);
                    String subject = jwtUtil.extractUsername(jwt);
                    if(subject != null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
                        if(jwtUtil.isTokenValid(jwt, userDetails.getUsername())){
//                            accessor.setHeader("username", subject);
                            accessor.getSessionAttributes().put("username", subject); // WebSocket 세션에 인증 정보 저장
                            // connect 요청을 처리한 쓰레드와 메시지를 처리하는 쓰레드가 다를 수 있어,
                            // 설정한 보안 컨텍스트가 손실될 가능성이 있으므로 securitycontextholder 사용 제한
                            return message;
                        }
                    }
                    throw new AccessDeniedException("Authorization header incorrect"); // Spring에서 연결 종료
                }
//                return null; // 메시지가 이후의 핸들러로 전달되지 않으며 무시됨.
                return message;
            }
        });
    }

}