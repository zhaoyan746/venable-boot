package com.venble.boot.security.config;

import com.venble.boot.security.JwtAuthenticationFilter;
import com.venble.boot.security.exception.handler.AccessDeniedHandlerImpl;
import com.venble.boot.security.exception.handler.AuthenticationEntryPointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final SecurityProperties securityProperties;

    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;

    public SpringSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, SecurityProperties securityProperties,
                                AccessDeniedHandlerImpl accessDeniedHandlerImpl, AuthenticationEntryPointImpl authenticationEntryPointImpl) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.securityProperties = securityProperties;
        this.accessDeniedHandlerImpl = accessDeniedHandlerImpl;
        this.authenticationEntryPointImpl = authenticationEntryPointImpl;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 开启跨域
                .cors(Customizer.withDefaults())
                // 关闭csrf(使用jwt,不需要csrf)
                .csrf(AbstractHttpConfigurer::disable)
                // 关闭session(使用jwt,不需要session)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 异常处理
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                // 未登录
                                .accessDeniedHandler(accessDeniedHandlerImpl)
                                // 未授权或token过期
                                .authenticationEntryPoint(authenticationEntryPointImpl)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(securityProperties.getIgnoreAntPatterns()).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
