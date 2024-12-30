package com.example.backend_spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AsyncService {

//    private final ThreadPoolTaskExecutor taskExecutor;
//
//    public AsyncService(@Qualifier("customThreadPoolTaskExecutor") ThreadPoolTaskExecutor taskExecutor) {
//        this.taskExecutor = taskExecutor;
//    }
    private final TransferRequestService transferRequestService;

    public AsyncService(TransferRequestService transferRequestService) {
        this.transferRequestService = transferRequestService;
    }

    @Async("customThreadPoolTaskExecutor")
    public void frontConnectionMaintainer(SseEmitter emitter){
        Map<String, String> ping = new HashMap<>();
        ping.put("status", "Ping");
        while(true){
            try{
                Thread.sleep(5000);
                synchronized (emitter){
                    if(emitter != null) emitter.send(ping);
                }
            } catch (IOException | IllegalStateException e) {
                // intended exception occurring from using completed emitter or closed connection.
//                synchronized (emitter) {emitter.completeWithError(e);} // managed by onError method
                log.info("connection closed by another thread");
                return;
            } catch (InterruptedException e) {
                // Thread.currentThread().interrupt(); // isInterrupted에 의존하지 않고 return에 의해 while문에서 빠져나오므로 불필요
                log.error(e.getMessage());
                return;
            }
        }
    }

    @Async("customThreadPoolTaskExecutor")
    public void analyzeServerConnector(SseEmitter emitter, String target_url){
        try{
            ResponseEntity<String> obj = transferRequestService.transferRequest(target_url, HttpMethod.GET, null);
            synchronized (emitter){
                emitter.send(obj.getBody());
                emitter.complete();
            }
        } catch (IOException e){
//            emitter.completeWithError(e); // managed by onError method
            log.error(e.getMessage());
        }
    }

    @Async("customThreadPoolTaskExecutor")
    public CompletableFuture<String> asyncTask(){
        try{
            Thread.sleep(10000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("Async task executed at: " + System.currentTimeMillis());
    }
}
