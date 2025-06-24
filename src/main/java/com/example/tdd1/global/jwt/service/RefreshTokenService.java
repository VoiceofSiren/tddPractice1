package com.example.tdd1.global.jwt.service;

import com.example.tdd1.global.jwt.entity.RefreshToken;
import com.example.tdd1.global.jwt.exception.JwtCategoryNonMatchingException;
import com.example.tdd1.global.jwt.exception.JwtExpiredException;
import com.example.tdd1.global.jwt.exception.JwtNonExistingException;
import com.example.tdd1.global.jwt.exception.JwtNullException;
import com.example.tdd1.global.jwt.repository.RefreshTokenRepository;
import com.example.tdd1.global.jwt.util.JwtConstants;
import com.example.tdd1.global.jwt.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public void validate(String refreshToken) throws JwtNonExistingException, JwtExpiredException, JwtCategoryNonMatchingException, JwtNullException {
        // Check existence
        if (refreshToken == null || refreshToken.isBlank()) {

            throw new JwtNullException( "refresh token null");
        }

        // Check expiration of refresh token
        try {
            jwtUtils.expires(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException("refresh token expires");
        }

        // Check if category is "refresh"
        if (!jwtUtils.getCategory(refreshToken).equals("refresh")) {
            throw new JwtCategoryNonMatchingException("category does not match");
        }

        // Check if refresh token is stored in DB
        Boolean refreshExists = refreshTokenRepository.existsByRefresh(refreshToken);
        if (!refreshExists) {
            throw new JwtNonExistingException("invalid refresh token");
        }
    }

    @Transactional
    public Map<String, String> reissueTokens(String refreshToken) {
        String username = jwtUtils.getUsername(refreshToken);
        String role = jwtUtils.getRole(refreshToken);

        // Reissue tokens
        String newAccessToken = jwtUtils.issueJwt("access", username, role, JwtConstants.ACCESS_TOKEN_EXPIRATION);
        String newRefreshToken = jwtUtils.issueJwt("refresh", username, role, JwtConstants.REFRESH_TOKEN_EXPIRATION);

        // Delete old refresh token
        refreshTokenRepository.deleteOldRefreshToken(refreshToken);

        // Store new refresh token into DB
        addRefreshTokenEntity(username, newRefreshToken, JwtConstants.REFRESH_TOKEN_EXPIRATION);

        return Map.of("newAccessToken", newAccessToken, "newRefreshToken", newRefreshToken);
    }

    @Transactional
    public void addRefreshTokenEntity(String username, String refresh, Long expirationMs) {
        Date date = new Date(System.currentTimeMillis() + expirationMs);

        System.out.println("now: " + new Date(System.currentTimeMillis()));
        System.out.println("exp: " + date);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(date.toString());

        refreshTokenRepository.save(refreshToken);
    }



}
