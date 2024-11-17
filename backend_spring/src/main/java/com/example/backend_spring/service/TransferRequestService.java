package com.example.backend_spring.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransferRequestService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> transferRequest(String target_url, HttpMethod method, String data){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(data, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                target_url,
                method,
                requestEntity,
                String.class
        );

        return response;
    }

}
