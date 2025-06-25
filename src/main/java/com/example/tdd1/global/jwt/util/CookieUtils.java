package com.example.tdd1.global.jwt.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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
        cookie.setMaxAge(1 * 60 * 60);      // 만료 시간: 1h
        cookie.setHttpOnly(true);           // JS 공격 방지
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie removeRefresh() {
        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    public static String getRefreshTokenFromCookie(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }
}
