package com.venble.boot.security.exception.handler;

import com.venble.boot.common.exception.ErrorCode;
import com.venble.boot.common.util.ServletUtils;
import com.venble.boot.common.vo.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证错误
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        ServletUtils.writeJson(response, R.error(ErrorCode.ERROR.getCode(), exception.getMessage()));
    }
}
