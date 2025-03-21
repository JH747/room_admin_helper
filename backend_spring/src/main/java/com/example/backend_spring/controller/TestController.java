package com.example.backend_spring.controller;

import com.example.backend_spring.entity.AppUser;
import com.example.backend_spring.entity.StandardRoomsInfo;
import com.example.backend_spring.repository.AppUserRepository;
import com.example.backend_spring.service.AppUserService;
import com.example.backend_spring.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AsyncService asyncService;



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/administratoronly")
    public ResponseEntity<String> getAdministratorOnly() {
        return ResponseEntity.ok("you have administrator authority");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authenticatedonly")
    public ResponseEntity<String> getRes() {
        return ResponseEntity.ok("Hello World");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authenticatedonly/1")
    public ResponseEntity<String> test() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getName());

        return ResponseEntity.ok("Hello, World!");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/authenticatedonly/2")
    public ResponseEntity<String> getData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        AppUser appUser = appUserRepository.findByUsername(username);

        System.out.println(appUser.getStandardRoomsInfos());

        for(StandardRoomsInfo sri : appUser.getStandardRoomsInfos()) {
            System.out.println(sri.getId() + "\t" + sri.getDisplayOrder() + "\t" + sri.getRoomQuantity() + "\t" + sri.getRoomName());
        }

        return ResponseEntity.ok("Hello, World!");
    }

    @GetMapping("/testAsync")
    public ResponseEntity<String> testAsync() {

        CompletableFuture<?> cf2 = asyncService.asyncTask();
        String str = "";
        try {
            str = (String) cf2.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("Hello, World!" + str);
    }

    @GetMapping("/emit")
    public SseEmitter testEmitter() {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L); // 5 min timeout
        emitter.onCompletion(() -> log.info("SSE 연결 종료"));
        emitter.onTimeout(() -> log.info("SSE 연결 타임아웃"));
        emitter.onError((e) -> log.error("SSE 연결 중 에러 발생: {}", e.getMessage()));

        Map<String, String> init = new HashMap<>();
        init.put("status", "Connection opened");
        try{
            emitter.send(init);
            emitter.complete();
        }catch (IOException e){
            emitter.completeWithError(e);

        }
        return emitter;
    }

}
