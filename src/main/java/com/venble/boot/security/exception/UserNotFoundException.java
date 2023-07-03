package com.venble.boot.security.exception;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
public class UserNotFoundException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable t) {
        super(message, t);
    }
}