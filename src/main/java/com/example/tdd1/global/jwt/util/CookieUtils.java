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
        // cookie.setSecure(true);                                      // HTTPS 설정
        cookie.setMaxAge((int) JwtConstants.REFRESH_TOKEN_EXPIRATION);  // 만료 시간
        cookie.setHttpOnly(true);                                       // JS 공격 방지
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie removeRefresh() {
        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    public static String getRefreshTokenFromCookie(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(JwtConstants.REFRESH_TOKEN_COOKIE_NAME)) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        return refreshToken;
    }
}
