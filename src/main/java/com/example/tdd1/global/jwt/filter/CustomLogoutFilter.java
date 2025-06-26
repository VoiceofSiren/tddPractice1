package com.example.tdd1.global.jwt.filter;

import com.example.tdd1.global.jwt.repository.RefreshTokenRepository;
import com.example.tdd1.global.jwt.util.CookieUtils;
import com.example.tdd1.global.jwt.util.JwtConstants;
import com.example.tdd1.global.jwt.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    public CustomLogoutFilter(JwtUtils jwtUtils, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtils = jwtUtils;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Verify path
        String requestURI = request.getRequestURI();
        if(!requestURI.matches("^/auth/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Verify method
        String method = request.getMethod();
        if(!method.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get refresh token
        String refreshToken = CookieUtils.getRefreshTokenFromCookie(request);

        if (    refreshToken == null ||                                         // Check null
                jwtUtils.expires(refreshToken) ||                           // Check expiration
                !jwtUtils.getCategory(refreshToken).equals(JwtConstants.REFRESH_TOKEN_CATEGORY) ||    // Check category
                !refreshTokenRepository.existsByRefresh(refreshToken)) {    // Check db
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        refreshTokenRepository.deleteOldRefreshToken(refreshToken);
        Cookie cookie = CookieUtils.removeRefresh();

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
