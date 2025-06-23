package com.example.tdd1.global.jwt.util;

import jakarta.servlet.http.Cookie;

public class CookieUtils {
    /**
     *
     * @param key: 문자열 "refresh"
     * @param value: refreshToken 값 (JWT)
     * @return cookie
     */
    public static Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        // cookie.setSecure(true);             // HTTPS 설정
        cookie.setMaxAge(24 * 60 * 60);     // 만료 시간: 24h
        cookie.setHttpOnly(true);           // JS 공격 방지
        return cookie;
    }
}
