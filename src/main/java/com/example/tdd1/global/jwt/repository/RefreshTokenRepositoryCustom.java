package com.example.tdd1.global.jwt.repository;

public interface RefreshTokenRepositoryCustom {
    void deleteOldRefreshToken(String refresh);
}
