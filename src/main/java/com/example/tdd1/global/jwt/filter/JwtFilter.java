package com.example.tdd1.global.jwt.filter;

import com.example.tdd1.global.jwt.util.CookieUtils;
import com.example.tdd1.global.jwt.util.JwtConstants;
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

        String accessToken = request.getHeader(JwtConstants.ACCESS_TOKEN_HEADER_NAME);
        String refreshToken = CookieUtils.getRefreshTokenFromCookie(request);

        if (accessToken == null) {
            // 권한이 필요 없는 요청일 수도 있기 때문에 다음 필터로 넘기는 작업을 수행
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 종류가 Access Token인지 확인
        // 다음 필터로 넘기지 않음.
        if (!jwtUtils.getCategory(accessToken).equals(JwtConstants.ACCESS_TOKEN_CATEGORY)) {

            // Response Body
            PrintWriter writer = response.getWriter();
            writer.print("JwtFilter: invalid access token");

            // Response Status Code
            // Front-end 측과 협의된 상태 코드 필요
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Access Token 만료 여부 확인
        // 다음 단계의 필터로 넘기지 않음.
        if (jwtUtils.expires(accessToken) ) {
            if (jwtUtils.expires(refreshToken)) {
                PrintWriter writer = response.getWriter();
                writer.print("JwtFilter: both access token and refresh token expired");

                // Response Status Code
                // Front-end 측과 협의된 상태 코드 필요
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        setAuthentication(accessToken);

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {

        String username = jwtUtils.getUsername(token);
        String role = jwtUtils.getRole(token);

        User user = new User();
        user.setUsername(username);
        user.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authenticationToken);
    }
}
