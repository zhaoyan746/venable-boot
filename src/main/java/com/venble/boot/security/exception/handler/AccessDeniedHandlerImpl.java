package com.venble.boot.security.exception.handler;

import com.venble.boot.common.exception.ErrorCode;
import com.venble.boot.common.util.ServletUtils;
import com.venble.boot.common.vo.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        log.error("Access denied: {}", accessDeniedException.getMessage());
        ServletUtils.writeJson(response, R.error(ErrorCode.FORBIDDEN));
    }
}
