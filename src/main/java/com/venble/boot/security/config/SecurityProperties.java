package com.venble.boot.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {

    /**
     * Token is valid for 30 minutes
     */
    private long tokenValidityInSecondsForRememberMe = 2592000;

    /**
     * Token is valid for 30 days
     */
    private long tokenValidityInSeconds = 1800;

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

    /**
     * Before filter class name
     */
    private String beforeFilterClassName;
}
