package com.example.tdd1.global.jwt.service;

import com.example.tdd1.global.jwt.entity.RefreshToken;
import com.example.tdd1.global.jwt.exception.JwtCategoryNonMatchingException;
import com.example.tdd1.global.jwt.exception.JwtExpiredException;
import com.example.tdd1.global.jwt.exception.JwtNonExistingException;
import com.example.tdd1.global.jwt.exception.JwtNullException;
import com.example.tdd1.global.jwt.repository.RefreshTokenRepository;
import com.example.tdd1.global.jwt.util.JwtConstants;
import com.example.tdd1.global.jwt.util.JwtUtils;
import com.example.tdd1.global.redis.service.RedisSingleDataService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisSingleDataService redisSingleDataService;
    private final JwtUtils jwtUtils;

    @Transactional
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
        if (!jwtUtils.getCategory(refreshToken).equals(JwtConstants.REFRESH_TOKEN_CATEGORY)) {
            throw new JwtCategoryNonMatchingException("category does not match");
        }

        // Check if refresh token is stored in DB
        //  [1] Check by redis first.
        String username = jwtUtils.getUsername(refreshToken);
        String refreshTokenInMemory = redisSingleDataService.getSingleData(username);
        if (refreshTokenInMemory == null) {
            // [2] Check by rdb if refresh token is not found by redis.
            Boolean refreshExistsInRdb = refreshTokenRepository.existsByRefresh(refreshToken);
            if (!refreshExistsInRdb) {
                throw new JwtNonExistingException("invalid refresh token");
            }
            redisSingleDataService.setSingleData(username, refreshToken, Duration.ofDays(1));
        }
    }

    @Transactional
    public Map<String, String> reissueTokens(String refreshToken) {
        String username = jwtUtils.getUsername(refreshToken);
        String role = jwtUtils.getRole(refreshToken);

        // Reissue tokens
        String newAccessToken = jwtUtils.issueJwt(JwtConstants.ACCESS_TOKEN_CATEGORY, username, role, JwtConstants.ACCESS_TOKEN_EXPIRATION);
        String newRefreshToken = jwtUtils.issueJwt(JwtConstants.REFRESH_TOKEN_CATEGORY, username, role, JwtConstants.REFRESH_TOKEN_EXPIRATION);

        // Delete old refresh token from memory
        redisSingleDataService.deleteSingleData(refreshToken);

        // Delete old refresh token from RDB
        refreshTokenRepository.deleteOldRefreshToken(refreshToken);

        // Store new refresh token into DB
        storeRefreshToken(username, newRefreshToken, JwtConstants.REFRESH_TOKEN_EXPIRATION);

        return Map.of("newAccessToken", newAccessToken, "newRefreshToken", newRefreshToken);
    }

    @Transactional
    public void storeRefreshToken(String username, String refresh, Long expirationMs) {
        Date date = new Date(System.currentTimeMillis() + expirationMs);
        Duration timeToLiveMs = Duration.ofMillis(System.currentTimeMillis() + expirationMs);

        System.out.println("now: " + new Date(System.currentTimeMillis()));
        System.out.println("exp: " + date);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(Instant.now().plus(timeToLiveMs).toString());

        refreshTokenRepository.save(refreshToken);
    }

}
