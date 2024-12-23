package com.example.backend_spring.controller;

import com.example.backend_spring.dto.CustomerDTO;
import com.example.backend_spring.dto.MemoDTO;
import com.example.backend_spring.entity.Customer;
import com.example.backend_spring.entity.Memo;
import com.example.backend_spring.entity.PlatformsRoomsInfo;
import com.example.backend_spring.service.CustomerService;
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
    private final CustomerService customerService;

    public UtilsController(MemoService memoService, CustomerService customerService) {
        this.memoService = memoService;
        this.customerService = customerService;
    }

    @PostMapping("/memo")
    public ResponseEntity<String> setMemo(@RequestBody MemoDTO memoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean created = memoService.setMemo(memoDTO, username);
        if (created) return ResponseEntity.status(HttpStatus.CREATED).body("Memo created successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Memo updated successfully");
    }
    @GetMapping("/memo")
    public ResponseEntity<List<MemoDTO>> getMemos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<MemoDTO> data = memoService.getMemos(username);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }


    @PostMapping("/customer")
    public ResponseEntity<String> setCustomer(@RequestBody CustomerDTO customerDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean created = customerService.setCustomer(customerDTO, username);
        if (created) return ResponseEntity.status(HttpStatus.CREATED).body("Customer created successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Customer updated successfully");
    }
    @GetMapping("/customer")
    public ResponseEntity<List<CustomerDTO>> getCustomers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<CustomerDTO> data = customerService.getCustomers(username);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
