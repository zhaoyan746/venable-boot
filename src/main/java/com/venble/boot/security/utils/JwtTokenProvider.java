package com.venble.boot.security.utils;

import com.venble.boot.security.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Token provider.
 */
@Component
public class JwtTokenProvider {

    private final SecurityProperties securityProperties;

    public JwtTokenProvider(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * 从请求头中获取token
     *
     * @param request 请求
     * @return token
     */
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(securityProperties.getTokenHeader());
        if (token != null && token.startsWith(securityProperties.getTokenPrefix())) {
            return token.substring(securityProperties.getTokenPrefix().length());
        }
        return null;
    }

    /**
     * 创建token
     *
     * @param authentication 认证信息
     * @param rememberMe     记住我
     * @return token
     */
    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(securityProperties.getTokenValidityInSecondsForRememberMe(), ChronoUnit.SECONDS);
        } else {
            validity = now.plus(securityProperties.getTokenValidityInSeconds(), ChronoUnit.SECONDS);
        }
        String token = Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(validity))
                .setSubject(authentication.getName())
                .claim(SecurityUtils.AUTHORITIES_KEY, authorities)
                .compact();
        return securityProperties.getTokenPrefix() + token;
    }

    /**
     * 验证token
     *
     * @param token token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(securityProperties.getTokenSecret().getBytes())
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获取claim
     *
     * @param token          token
     * @param claimsResolver claimsResolver
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(securityProperties.getTokenSecret().getBytes())
                .build()
                .parseClaimsJws(token).getBody();
        return claimsResolver.apply(body);
    }

    /**
     * 获取用户名
     *
     * @param token
     * @return 用户名
     */
    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }
}
