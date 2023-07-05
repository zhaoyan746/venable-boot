package com.venble.boot.jpa.config;

import com.venble.boot.security.util.SecurityUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class CustomAuditorAware implements AuditorAware<Long> {

    @Override
    public @NotNull Optional<Long> getCurrentAuditor() {
        return SecurityUtils.getCurrentUserId();
    }
}