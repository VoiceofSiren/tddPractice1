package com.example.tdd1.global.jwt.controller;

import com.example.tdd1.global.jwt.exception.JwtCategoryNonMatchingException;
import com.example.tdd1.global.jwt.exception.JwtExpiredException;
import com.example.tdd1.global.jwt.exception.JwtNonExistingException;
import com.example.tdd1.global.jwt.exception.JwtNullException;
import com.example.tdd1.global.jwt.service.RefreshTokenService;
import com.example.tdd1.global.jwt.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // Get refresh token
        String refreshToken = CookieUtils.getRefreshTokenFromCookie(request);

        try {
            refreshTokenService.validate(refreshToken);
        } catch (JwtNullException | JwtNonExistingException | JwtExpiredException | JwtCategoryNonMatchingException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // Reissue access token and refresh token
        // Delete old refresh token
        Map<String, String> reissuedTokens = refreshTokenService.reissueTokens(refreshToken);

        // Add to response
        response.setHeader("accessToken", reissuedTokens.get("newAccessToken"));
        response.addCookie(CookieUtils.createCookie("refreshToken", reissuedTokens.get("newRefreshToken")));

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
