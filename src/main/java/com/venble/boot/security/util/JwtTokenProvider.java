package com.venble.boot.security.util;

import com.venble.boot.security.config.SecurityProperties;
import com.venble.boot.security.domain.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validity;
        if (rememberMe) {
            validity = now.plusSeconds(securityProperties.getTokenExpiryInSecondsForRememberMe());
        } else {
            validity = now.plusSeconds(securityProperties.getTokenExpiryInSeconds());
        }
        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(securityProperties.getTokenSecret().getBytes()), SignatureAlgorithm.HS512)
                .setId(customUserDetails.getId().toString())
                .setSubject(authentication.getName())
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
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
     * @param token token
     * @return 用户名
     */
    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }


    /**
     * token 是否过期
     *
     * @param token token
     * @return 是否过期
     */
    public boolean isExpired(String token) {
        return getExpiration(token).isBefore(LocalDateTime.now());
    }

    /**
     * 获取过期时间
     *
     * @param token token
     * @return 过期时间
     */
    private LocalDateTime getExpiration(String token) {
        return LocalDateTime.ofInstant(getClaim(token, Claims::getExpiration).toInstant(), ZoneId.systemDefault());
    }

    /**
     * 获取用户详情
     *
     * @param token token
     * @return 用户详情
     */
    public CustomUserDetails getUserDetails(String token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(securityProperties.getTokenSecret().getBytes())
                .build()
                .parseClaimsJws(token).getBody();
        // TODO
        return new CustomUserDetails(
                Long.parseLong(body.getId()),
                body.getSubject(),
                "",
                true,
                new HashSet<>());
    }
}
