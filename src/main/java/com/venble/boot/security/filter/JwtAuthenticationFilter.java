package com.venble.boot.security.filter;

import com.venble.boot.common.exception.ErrorCode;
import com.venble.boot.common.util.ServletUtils;
import com.venble.boot.security.service.CustomUserDetailsService;
import com.venble.boot.security.util.JwtTokenProvider;
import com.venble.boot.security.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = tokenProvider.resolveToken(request);
        if (token != null && tokenProvider.validateToken(token)) {
            // token is expired
            if (tokenProvider.isExpired(token)) {
                ServletUtils.writeJson(response, ErrorCode.TOKEN_EXPIRED);
            } else {
                String username = tokenProvider.getUsername(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                SecurityUtils.setCurrentUserLogin(userDetails);
            }
        }
        filterChain.doFilter(request, response);
    }
}
