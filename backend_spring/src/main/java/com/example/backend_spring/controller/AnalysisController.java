package com.example.backend_spring.controller;


import com.example.backend_spring.service.TransferRequestService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final TransferRequestService transferRequestService;

    public AnalysisController(TransferRequestService transferRequestService) {
        this.transferRequestService = transferRequestService;
    }

    // TODO:  consider replacing below with combination of multiple async functions
    @GetMapping("/general")
    public SseEmitter getAnalysis(HttpServletResponse response,
                                  @RequestParam("start_date") String start_date,
                                  @RequestParam("end_date") String end_date,
                                  @RequestParam("mode") String mode) {

        // TODO: check if start_date and end_date period is appropriate below
        // add here

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // change here
        String target_url = String.format("http://localhost:8000/api/processes/?username=%s&start_date=%s&end_date=%s&mode=%s",
                username, start_date, end_date, mode);

        log.info("target_url: {}", target_url);

        // set sseEmitter
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L); // 5 min timeout
        emitter.onCompletion(() -> log.info("SSE 연결 종료")); // TODO: replace with logging
        emitter.onTimeout(() -> log.info("SSE 연결 타임아웃"));
        emitter.onError((e) -> log.error("SSE 연결 중 에러 발생: {}", e.getMessage()));
        Map<String, String> init = new HashMap<>();
        init.put("status", "Connection opened");
        try{
            emitter.send(init);
        }catch (IOException e){
            emitter.completeWithError(e);
        }

        // scheduled ping sending on child thread to maintain connection with client
        Map<String, String> ping = new HashMap<>();
        ping.put("status", "Ping");
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            try{
//                if(future.isDone()) executorService.shutdown();
                log.info("{}\t{}\t{}", Thread.currentThread().getName(), SecurityContextHolder.getContext().getAuthentication().getName(), response.isCommitted());
                emitter.send(ping);
            }catch (Exception e){
                emitter.completeWithError(e);
            }
        };
        Runnable task_with_securityContext = new DelegatingSecurityContextRunnable(task); // not this problem
        executorService.scheduleAtFixedRate(task_with_securityContext, 5, 5, TimeUnit.SECONDS);

        // get response from analysis server on child thread
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
        Runnable task2 = () -> {
//            String s = longFunction();
            ResponseEntity<String> obj = transferRequestService.transferRequest(target_url, HttpMethod.GET, null);
            log.info(obj.getBody());
            executorService.shutdown();
            log.info("{}\t{}\t{}", Thread.currentThread().getName(), SecurityContextHolder.getContext().getAuthentication().getName(), response.isCommitted());
            try {
                emitter.send(obj.getBody());
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        };
        Runnable task2_with_securityContext = new DelegatingSecurityContextRunnable(task2);
        executorService2.submit(task2_with_securityContext);

//        CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->{
//            return longFunction();
////            return transferRequestService.transferRequest(target_url, HttpMethod.GET, null);
//        }).thenAccept(response->{
//            try {
//                executorService.shutdown();
//                emitter.send(response);
//                emitter.complete();
//            } catch (IOException e) {
//                emitter.completeWithError(e);
//            }
//        }).exceptionally(e->{
//            emitter.completeWithError(e);
//            return null;
//        });

        return emitter;
    }


    // controller 내부 전역 예외처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleSpecificControllerExceptions(Exception e){
        log.error("{}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }
}
