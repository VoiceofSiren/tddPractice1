package com.example.tdd1.global.jwt.filter;

import com.example.tdd1.global.jwt.util.JwtUtils;
import com.example.tdd1.user.dto.CustomUserDetails;
import com.example.tdd1.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("accessToken");

        if (accessToken == null) {
            // 권한이 필요 없는 요청일 수도 있기 때문에 다음 필터로 넘기는 작업을 수행
            filterChain.doFilter(request, response);
            return;
        }

        // Access Token 만료 여부 확인
        // 다음 필터로 넘기지 않음.
        try {
            jwtUtils.expires(accessToken);
        } catch (ExpiredJwtException e) {

            // Response Body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            // Response Status Code
            // Front-end 측과 협의된 상태 코드 필요
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰 종류가 Access Token인지 확인
        // 다음 필터로 넘기지 않음.
        if (!jwtUtils.getCategory(accessToken).equals("access")) {

            // Response Body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            // Response Status Code
            // Front-end 측과 협의된 상태 코드 필요
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtils.getUsername(accessToken);
        String role = jwtUtils.getRole(accessToken);

        User user = new User();
        user.setUsername(username);
        user.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
