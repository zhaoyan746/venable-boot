package com.venble.boot.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {

    /**
     * Token is valid for rememberMeInSeconds seconds (1 weeks by default)
     */
    private long tokenExpiryInSecondsForRememberMe = 604800;

    /**
     * Token is valid for 30 days
     */
    private long tokenExpiryInSeconds = 2592000;

    /**
     * Token header
     */
    private String tokenHeader = "Authorization";

    /**
     * Token prefix
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Token secret
     */
    private String tokenSecret;

    /**
     * Token ant patterns
     */
    private String[] ignoreAntPatterns;
}
