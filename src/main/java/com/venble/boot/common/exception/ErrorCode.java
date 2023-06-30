package com.venble.boot.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorCode {

    private int code;

    private String message;

    public static final ErrorCode SUCCESS = new ErrorCode(0, "操作成功");

    public static final ErrorCode ERROR = new ErrorCode(500, "操作失败");

    public static final ErrorCode INVALID_ARGUMENT = new ErrorCode(400, "参数错误");

    public static final ErrorCode UNAUTHORIZED = new ErrorCode(401, "未授权");

    public static final ErrorCode FORBIDDEN = new ErrorCode(403, "没有权限访问");

    public static final ErrorCode RESOURCE_NOT_FOUND = new ErrorCode(404, "请求的资源不存在");

    public static final ErrorCode DUPLICATE_RESOURCE = new ErrorCode(409, "资源已存在");
}
