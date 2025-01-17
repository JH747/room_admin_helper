//package com.example.backend_spring.controller;
//
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class MessageController {
//
//    @MessageMapping("/hello") // 클라이언트에서
//    @SendTo("/topic/greeting")
//    public String example(String message){
//        System.out.println(message);
//        System.out.println("here we are");
//        return "ws success";
//    }
//
//}
