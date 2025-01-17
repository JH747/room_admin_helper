package com.example.backend_spring.controller;


import com.example.backend_spring.JWTUtil;
import com.example.backend_spring.service.AsyncService;
import com.example.backend_spring.service.TransferRequestService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@RestController
public class AnalysisController {

    private final TransferRequestService transferRequestService;
    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    public AnalysisController(TransferRequestService transferRequestService, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.transferRequestService = transferRequestService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

//    // TODO:  consider replacing below with combination of multiple async functions
//    @GetMapping("/general")
//    public SseEmitter getAnalysis(HttpServletResponse response,
//                                  @RequestParam("start_date") String start_date,
//                                  @RequestParam("end_date") String end_date,
//                                  @RequestParam("mode") String mode) {
//
//        // TODO: check if start_date and end_date period is appropriate below
//        // add here
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName(); // change here
//        String target_url = String.format("http://localhost:8000/api/processes/?username=%s&start_date=%s&end_date=%s&mode=%s",
//                username, start_date, end_date, mode);
//
//        log.info("target_url: {}", target_url);
//
//        // set sseEmitter
//        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L); // 5 min timeout
//        emitter.onCompletion(() -> log.info("SSE 연결 종료"));
//        emitter.onTimeout(() -> log.info("SSE 연결 타임아웃"));
//        emitter.onError((e) -> log.error("SSE 연결 중 에러 발생: {}", e.getMessage()));
//
//        Map<String, String> init = new HashMap<>();
//        init.put("status", "Connection opened");
//        try{
//            emitter.send(init);
//        }catch (IOException e){
//            emitter.completeWithError(e);
//            log.error(e.getMessage());
//        }
//
//        // scheduled ping sending on child thread to maintain connection with client
//        Map<String, String> ping = new HashMap<>();
//        ping.put("status", "Ping");
//        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        Runnable task = () -> {
//            try{
//                log.info("{}\t{}\t{}", Thread.currentThread().getName(), SecurityContextHolder.getContext().getAuthentication().getName(), response.isCommitted());
//                emitter.send(ping);
//            }catch (Exception e){
//                emitter.completeWithError(e);
//            }
//        };
//        Runnable task_with_securityContext = new DelegatingSecurityContextRunnable(task); // not this problem
//        executorService.scheduleAtFixedRate(task_with_securityContext, 5, 5, TimeUnit.SECONDS);
//
//        // get response from analysis server on child thread
//        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
//        Runnable task2 = () -> {
//            ResponseEntity<String> obj = transferRequestService.transferRequest(target_url, HttpMethod.GET, null);
//            log.info(obj.getBody());
//            executorService.shutdown();
//            log.info("{}\t{}\t{}", Thread.currentThread().getName(), SecurityContextHolder.getContext().getAuthentication().getName(), response.isCommitted());
//            try {
//                emitter.send(obj.getBody());
//                emitter.complete();
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        };
//        Runnable task2_with_securityContext = new DelegatingSecurityContextRunnable(task2);
//        executorService2.submit(task2_with_securityContext);
//
//        asyncService.frontConnectionMaintainer(emitter);
//        asyncService.analyzeServerConnector(emitter, target_url);
//        emitter.complete();
//        return emitter;
//    }

    @MessageMapping("/analysis")
    @SendTo("/queue/general")
    public String getAnalysis(Map<String, Object> obj, SimpMessageHeaderAccessor headerAccessor){
        String start_date = obj.get("start_date").toString();
        String end_date = obj.get("end_date").toString();
        String mode = obj.get("mode").toString();
//        String authHeader = obj.get("Authorization").toString();

        // 이 방식 안됨
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();

        // 이 방식도 영...
//        String jwt = authHeader.substring(7);
//        String subject = jwtUtil.extractUsername(jwt);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
//        if(!jwtUtil.isTokenValid(jwt, userDetails.getUsername())){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("sorry");
//        }
//        String username = userDetails.getUsername();

//        String username = (String) headerAccessor.getHeader("username");
        String username = (String) headerAccessor.getSessionAttributes().get("username");


        String target_url = String.format("http://localhost:8000/api/processes/?username=%s&start_date=%s&end_date=%s&mode=%s",
        username, start_date, end_date, mode);
        ResponseEntity res = transferRequestService.transferRequest(target_url, HttpMethod.GET, null);

        return res.getBody().toString();
        // 반환값 재작성
    }


    // controller 내부 전역 예외처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleSpecificControllerExceptions(Exception e){
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
