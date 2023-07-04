package com.venble.boot.web.handler;

import com.venble.boot.common.exception.ApiException;
import com.venble.boot.common.exception.ErrorCode;
import com.venble.boot.common.vo.R;
import com.venble.boot.security.exception.UserNotActivatedException;
import com.venble.boot.security.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("请求参数缺失异常:{}", e.getMessage(), e);
        return R.error(ErrorCode.INVALID_ARGUMENT.getCode(), String.format("请求参数缺失: %s", e.getParameterName()));
    }

    /**
     * 请求参数类型异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        log.error("请求参数类型异常:{}", e.getMessage(), e);
        return R.error(ErrorCode.INVALID_ARGUMENT.getCode(), String.format("请求参数类型错误: %s", e.getMessage()));
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("参数校验异常:{}", e.getMessage(), e);
        FieldError fieldError = e.getBindingResult().getFieldError();
        assert fieldError != null;
        return R.error(ErrorCode.INVALID_ARGUMENT.getCode(), String.format("请求参数异常: %s", fieldError.getDefaultMessage()));
    }

    /**
     * MVC参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public R<?> bindExceptionHandler(BindException e) {
        log.error("MVC参数绑定异常:{}", e.getMessage(), e);
        FieldError fieldError = e.getFieldError();
        assert fieldError != null; // 断言，避免告警
        return R.error(ErrorCode.INVALID_ARGUMENT.getCode(), String.format("请求参数异常: %s", fieldError.getDefaultMessage()));
    }

    /**
     * Validator 校验不通过异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("Validator 校验不通过异常:{}", e.getMessage(), e);
        ConstraintViolation<?> constraintViolation = e.getConstraintViolations().iterator().next();
        return R.error(ErrorCode.INVALID_ARGUMENT.getCode(), String.format("请求参数异常: %s", constraintViolation.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R<?> noHandlerFoundExceptionHandler(NoHandlerFoundException e) {
        log.error("404异常:{}", e.getMessage(), e);
        return R.error(ErrorCode.RESOURCE_NOT_FOUND.getCode(), String.format("资源不存在: %s", e.getMessage()));
    }

    /**
     * 权限不足
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R<?> accessDeniedExceptionHandler(AccessDeniedException e) {
        log.error("权限不足异常:{}", e.getMessage(), e);
        return R.error(ErrorCode.FORBIDDEN);
    }

    /**
     * 密码错误
     */
    @ExceptionHandler(BadCredentialsException.class)
    public R<?> badCredentialsExceptionHandler(BadCredentialsException e) {
        log.error("密码错误异常:{}", e.getMessage(), e);
        return R.error(ErrorCode.ERROR.getCode(), "密码错误");
    }

    /**
     * 用户未激活
     */
    @ExceptionHandler(UserNotActivatedException.class)
    public R<?> userNotActivatedExceptionHandler(UserNotActivatedException e) {
        log.error("用户未激活异常:{}", e.getMessage(), e);
        return R.error(ErrorCode.ERROR.getCode(), "用户未激活");
    }

    /**
     * 用户不存在
     */
    @ExceptionHandler(UserNotFoundException.class)
    public R<?> userNotFoundExceptionHandler(UserNotFoundException e) {
        log.error("用户不存在异常:{}", e.getMessage(), e);
        return R.error(ErrorCode.ERROR.getCode(), "用户不存在");
    }

    /**
     * api异常
     */
    @ExceptionHandler(ApiException.class)
    public R<?> apiExceptionHandler(ApiException e) {
        log.error("Api异常:{}", e.getMessage(), e);
        return R.error(e.getCode(), e.getMessage());
    }

    /**
     * 默认异常
     */
    @ExceptionHandler(Exception.class)
    public R<?> exceptionHandler(Exception e) {
        log.error("默认异常:{}", e.getMessage(), e);
        // TODO 写入日志
        return R.error(ErrorCode.ERROR);
    }
}
