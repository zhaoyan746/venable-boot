package com.venble.boot.security.config;

import com.venble.boot.security.exception.handler.AccessDeniedHandlerImpl;
import com.venble.boot.security.exception.handler.AuthenticationEntryPointImpl;
import com.venble.boot.security.filter.JwtAuthenticationFilter;
import jakarta.annotation.security.PermitAll;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final SecurityProperties securityProperties;

    private final AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    private final AuthenticationEntryPointImpl authenticationEntryPointImpl;

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;


    public SpringSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, SecurityProperties securityProperties,
                                AccessDeniedHandlerImpl accessDeniedHandlerImpl, AuthenticationEntryPointImpl authenticationEntryPointImpl,
                                RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.securityProperties = securityProperties;
        this.accessDeniedHandlerImpl = accessDeniedHandlerImpl;
        this.authenticationEntryPointImpl = authenticationEntryPointImpl;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 开启跨域
                .cors(AbstractHttpConfigurer::disable)
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
                .authorizeHttpRequests(this::setupPermitAllPatterns)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private void setupPermitAllPatterns(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        // ignore ant patterns
        auth.requestMatchers(securityProperties.getIgnoreAntPatterns()).permitAll();
        // has @PermitAll
        requestMappingHandlerMapping.getHandlerMethods().forEach((info, method) -> {
            if (!method.hasMethodAnnotation(PermitAll.class)) return;
            if (info.getPathPatternsCondition() == null) return;
            String[] patterns = info.getPathPatternsCondition().getPatternValues().toArray(new String[0]);
            info.getMethodsCondition().getMethods().forEach(m ->
                    auth.requestMatchers(m.asHttpMethod(), patterns).permitAll()
            );
        });
        // any request authenticated
        auth.anyRequest().authenticated();
    }
}
