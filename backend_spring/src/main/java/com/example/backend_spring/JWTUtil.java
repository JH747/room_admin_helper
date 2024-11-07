package com.example.backend_spring;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    private final String SECRET_KEY = "temp_dev_secret_key"; // 환경 변수 등에서 설정하는 것이 좋습니다.
    private final long EXPIRATION_TIME = 1000*60*5; // 토큰 유효 기간 (밀리초)

    public String generateToken(String username) {

        Claims claims = Jwts.claims()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            return extractUsername(token).equals(username);
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token");
            return false;
        } catch (Exception e) {
            System.out.println("Something wrong with JWT token");
            return false;
        }
    }

}

