package com.example.backend_spring.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @PreAuthorize("isAnonymous()")
    @GetMapping("/test")
    public List<Integer> test() {
        List<Integer> tmp = new ArrayList<>();
        tmp.add(1);
        tmp.add(2);
        return tmp;
    }

}
