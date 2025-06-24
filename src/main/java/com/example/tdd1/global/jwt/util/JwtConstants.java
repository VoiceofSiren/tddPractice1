package com.example.tdd1.global.jwt.util;

public class JwtConstants {

    private JwtConstants() {} // 생성자 private 처리로 인스턴스화 방지

    public static final long ACCESS_TOKEN_EXPIRATION = 1 * 1 * 20 * 1000L;    // 20초
    public static final long REFRESH_TOKEN_EXPIRATION = 1 * 1 * 2 * 60 * 1000L; // 2분
}
