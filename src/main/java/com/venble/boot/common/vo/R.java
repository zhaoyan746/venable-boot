package com.venble.boot.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.venble.boot.common.exception.ErrorCode;
import lombok.Data;

@Data
public class R<T> {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 错误提示
     *
     * @see ErrorCode#getMessage() ()
     */
    private String message;

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.code = ErrorCode.SUCCESS.getCode();
        r.message = ErrorCode.SUCCESS.getMessage();
        r.data = data;
        return r;
    }

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> error(ErrorCode errorCode) {
        R<T> r = new R<>();
        r.code = errorCode.getCode();
        r.message = errorCode.getMessage();
        return r;
    }

    public static <T> R<T> error(Integer code, String msg) {
        R<T> r = new R<>();
        r.code = code;
        r.message = msg;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.code = ErrorCode.ERROR.getCode();
        r.message = msg;
        return r;
    }

    public static <T> R<T> error() {
        R<T> r = new R<>();
        r.code = ErrorCode.ERROR.getCode();
        r.message = ErrorCode.ERROR.getMessage();
        return r;
    }
}
