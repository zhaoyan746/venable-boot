package com.venble.boot.common.exception;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;

/**
 * api 异常
 *
 * @author zbq
 * @since 4/2/2022
 */
@JsonIncludeProperties({"code", "msg"})
public class ApiException extends RuntimeException {
    public ApiException(String msg) {
        super(msg);
        this.code = ErrorCode.ERROR.getCode();
        this.msg = msg;
    }

    public ApiException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public ApiException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.ERROR.getCode();
        this.msg = msg;
    }

    public ApiException(int code, String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
    }

    @Getter
    private final int code;
    @Getter
    private final String msg;
}
