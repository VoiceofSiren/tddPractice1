package com.example.tdd1.global.jwt.controller;

import com.example.tdd1.global.jwt.util.CookieUtils;
import com.example.tdd1.global.jwt.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JwtUtils jwtUtils;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // Get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null || refreshToken.isBlank()) {

            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // Check expiration
        try {
            jwtUtils.expires(refreshToken);
        } catch (ExpiredJwtException e) {

            return new ResponseEntity<>("refresh token expires", HttpStatus.BAD_REQUEST);
        }

        // Check category
        if (!jwtUtils.getCategory(refreshToken).equals("refresh")) {

            return new ResponseEntity<>("category does not match", HttpStatus.BAD_REQUEST);

        }

        String username = jwtUtils.getUsername(refreshToken);
        String role = jwtUtils.getRole(refreshToken);

        // Reissue new access token
        String newAccessToken = jwtUtils.issueJwt("access", username, role, 600000L);       // 만료 시간: 10m
        String newRefreshToken = jwtUtils.issueJwt("refresh", username, role, 86400000L);   // 만료 시간: 24h

        response.setHeader("access", newAccessToken);
        response.addCookie(CookieUtils.createCookie("refresh", newRefreshToken));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
