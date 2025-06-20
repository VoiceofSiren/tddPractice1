package com.example.tdd1.global.jwt.filter;

import com.example.tdd1.global.jwt.util.JwtUtils;
import com.example.tdd1.user.dto.CustomUserDetails;
import com.example.tdd1.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtils.expires(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtils.extractUsername(token);
        String role = jwtUtils.extractRole(token);

        User user = new User();
        user.setUsername(username);
        user.setPassword("tempPassword");
        user.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
