package com.example.tdd1.global.jwt.filter;

import com.example.tdd1.global.jwt.util.JwtUtils;
import com.example.tdd1.user.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String role = authorities.stream().findFirst().get().getAuthority();

        String accessToken = jwtUtils.issueJwt("access", username, role, 600000L);       // 만료 시간: 10m
        String refreshToken = jwtUtils.issueJwt("refresh", username, role, 86400000L);   // 만료 시간: 24h

        response.addHeader("accessToken", accessToken);
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
    }

    /**
     *
     * @param key: 문자열 "refresh"
     * @param value: refreshToken 값 (JWT)
     * @return cookie
     */
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        // cookie.setSecure(true);             // HTTPS 설정
        cookie.setMaxAge(24 * 60 * 60);     // 만료 시간: 24h
        cookie.setHttpOnly(true);           // JS 공격 방지
        return cookie;
    }
}
