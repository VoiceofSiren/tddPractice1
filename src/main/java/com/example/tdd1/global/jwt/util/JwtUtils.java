package com.example.tdd1.global.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private SecretKey secretKey;

    public JwtUtils(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Claims extractPayload(String token) {
        return Jwts
                // secretKey를 이용하여 파싱
                .parser().verifyWith(secretKey)
                // token 값을 이용
                .build().parseSignedClaims(token)
                // payload 추출
                .getPayload();
    }

    public String getCategory(String token) {
        return extractPayload(token).get("category", String.class);
    }

    public String getUsername(String token) {
        return extractPayload(token).get("username", String.class);
    }

    public String getRole(String token) {
        return extractPayload(token).get("role", String.class);
    }

    public Boolean expires(String token) {
        return extractPayload(token).getExpiration().before(new Date());
    }

    public String issueJwt(String category, String username, String role, Long expirationMillis) {
        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey)
                .compact();
    }
}
