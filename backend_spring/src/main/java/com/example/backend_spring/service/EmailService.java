package com.example.backend_spring.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Service
public class EmailService {

    private final RedisService redisService;
    private final JavaMailSender mailSender;

    public EmailService(RedisService redisService, JavaMailSender mailSender) {
        this.redisService = redisService;
        this.mailSender = mailSender;
    }

    private String createCode(){
        int length = 6;
        try{
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException e){
//            log.debug("MailService.createCode() exception occurred. ");
            throw new RuntimeException(); // ---
        }
    }

    private void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendCode(String to) {
        String code = createCode();
        sendSimpleEmail(to, "[Room Admin Helper] This mail is for your signup", code);
        redisService.saveData(to, code, Duration.ofMillis(1000*60*5));
    }

    public boolean verifyCode(String to, String code) {
        String rightCode = (String) redisService.getData(to);
        return rightCode != null && rightCode.equals(code);
    }


}
