package com.example.backend_spring.controller;

import com.example.backend_spring.entity.Memo;
import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.service.MemoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/utils")
public class UtilsController {

    private final MemoService memoService;

    public UtilsController(MemoService memoService) {
        this.memoService = memoService;
    }

    @PostMapping("/memo")
    public ResponseEntity setMemo(@RequestBody Memo memo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        memoService.createMemo(memo, username);

        // seperate create and update

        return ResponseEntity.status(HttpStatus.CREATED).body("Memo created successfully");
    }
    @GetMapping("/memo")
    public ResponseEntity<List<Memo>> getMemos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Memo> entities = memoService.getMemos(username);

        return ResponseEntity.status(HttpStatus.CREATED).body(entities);

    }
}
