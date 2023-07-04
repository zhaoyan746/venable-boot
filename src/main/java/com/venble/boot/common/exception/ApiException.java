package com.venble.boot.common.exception;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;

/**
 * api 异常
 *
 * @author zbq
 * @since 4/2/2022
 */
@JsonIncludeProperties({"code", "message"})
public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
        this.code = ErrorCode.ERROR.getCode();
        this.message = message;
    }

    public ApiException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public ApiException(String message, Throwable e) {
        super(message, e);
        this.code = ErrorCode.ERROR.getCode();
        this.message = message;
    }

    public ApiException(int code, String message, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Getter
    private final int code;
    @Getter
    private final String message;
}
