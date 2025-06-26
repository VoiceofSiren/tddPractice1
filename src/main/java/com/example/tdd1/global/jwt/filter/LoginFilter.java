package com.example.tdd1.global.jwt.filter;

import com.example.tdd1.global.jwt.service.RefreshTokenService;
import com.example.tdd1.global.jwt.util.CookieUtils;
import com.example.tdd1.global.jwt.util.JwtConstants;
import com.example.tdd1.global.jwt.util.JwtUtils;
import com.example.tdd1.user.dto.CustomUserDetails;
import com.example.tdd1.user.dto.request.UserLoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        setFilterProcessesUrl("/auth/logIn");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // form-data 방식
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);

        // application/json 방식
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            userLoginRequestDto = objectMapper.readValue(messageBody, UserLoginRequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = userLoginRequestDto.getUsername();
        String password = userLoginRequestDto.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String role = authorities.stream().findFirst().get().getAuthority();

        String accessToken = jwtUtils.issueJwt(JwtConstants.ACCESS_TOKEN_CATEGORY, username, role, JwtConstants.ACCESS_TOKEN_EXPIRATION);
        String refreshToken = jwtUtils.issueJwt(JwtConstants.REFRESH_TOKEN_CATEGORY, username, role, JwtConstants.REFRESH_TOKEN_EXPIRATION);

        // Refresh token 저장
        refreshTokenService.addRefreshTokenEntity(username, refreshToken, JwtConstants.REFRESH_TOKEN_EXPIRATION);

        response.addHeader(JwtConstants.ACCESS_TOKEN_HEADER_NAME, accessToken);
        response.addCookie(CookieUtils.createCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken));
        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("Authentication failed.");
        response.setStatus(401);
    }

}
