package com.computer.ecommerce.exception;

import com.computer.ecommerce.common.Result;
import com.computer.ecommerce.common.ResultCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        logger.warn("业务异常: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(e.getResultCode(), e.getMessage(), e.getData());
    }

    /**
     * 处理数据验证异常（@Validated触发）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                          HttpServletRequest request) {
        logger.warn("参数验证失败: {} - {}", request.getRequestURI(), e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return Result.fail(ResultCode.VALIDATION_FAILED, "数据验证失败", errors);
    }

    /**
     * 处理绑定异常（@Validated触发，表单提交）
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e, HttpServletRequest request) {
        logger.warn("绑定异常: {} - {}", request.getRequestURI(), e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return Result.fail(ResultCode.VALIDATION_FAILED, "数据绑定失败", errors);
    }

    /**
     * 处理约束违反异常（@Validated触发，方法参数）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e,
                                                       HttpServletRequest request) {
        logger.warn("约束违反异常: {} - {}", request.getRequestURI(), e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String fieldName = propertyPath.contains(".") ?
                propertyPath.substring(propertyPath.lastIndexOf('.') + 1) : propertyPath;
            errors.put(fieldName, violation.getMessage());
        });

        return Result.fail(ResultCode.VALIDATION_FAILED, "参数验证失败", errors);
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException e,
                                                  HttpServletRequest request) {
        logger.warn("认证失败: {} - {}", request.getRequestURI(), e.getMessage());

        if (e instanceof BadCredentialsException) {
            return Result.fail(ResultCode.INVALID_CREDENTIALS);
        } else if (e instanceof DisabledException) {
            return Result.fail(ResultCode.USER_DISABLED);
        } else if (e instanceof LockedException) {
            return Result.fail(ResultCode.USER_LOCKED);
        }

        return Result.fail(ResultCode.UNAUTHORIZED, e.getMessage());
    }

    /**
     * 处理授权异常（权限不足）
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e,
                                                HttpServletRequest request) {
        logger.warn("权限不足: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.FORBIDDEN);
    }

    /**
     * 处理JWT异常
     */
    @ExceptionHandler({ExpiredJwtException.class, MalformedJwtException.class, SignatureException.class})
    public Result<?> handleJwtException(Exception e, HttpServletRequest request) {
        logger.warn("JWT异常: {} - {}", request.getRequestURI(), e.getMessage());

        if (e instanceof ExpiredJwtException) {
            return Result.fail(ResultCode.TOKEN_EXPIRED);
        } else if (e instanceof MalformedJwtException || e instanceof SignatureException) {
            return Result.fail(ResultCode.TOKEN_INVALID);
        }

        return Result.fail(ResultCode.UNAUTHORIZED, "令牌验证失败");
    }

    /**
     * 处理数据库唯一约束异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e,
                                                HttpServletRequest request) {
        logger.warn("数据重复: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.DATA_EXISTS, "数据已存在");
    }

    /**
     * 处理数据库操作异常
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<?> handleDataAccessException(DataAccessException e,
                                              HttpServletRequest request) {
        logger.error("数据库操作异常: {} - {}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResultCode.INTERNAL_SERVER_ERROR, "数据库操作失败");
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    public Result<?> handleSQLException(SQLException e, HttpServletRequest request) {
        logger.error("SQL异常: {} - {}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResultCode.INTERNAL_SERVER_ERROR, "数据库错误");
    }

    /**
     * 处理数据完整性违反异常
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<?> handleDataIntegrityViolationException(DataIntegrityViolationException e,
                                                          HttpServletRequest request) {
        logger.warn("数据完整性违反: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.BAD_REQUEST, "数据完整性约束违反");
    }

    /**
     * 处理文件上传过大异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e,
                                                         HttpServletRequest request) {
        logger.warn("文件过大: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.FILE_TOO_LARGE);
    }

    /**
     * 处理HTTP消息不可读异常（JSON解析失败等）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                          HttpServletRequest request) {
        logger.warn("HTTP消息不可读: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.BAD_REQUEST, "请求体格式错误");
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
                                                                 HttpServletRequest request) {
        logger.warn("请求方法不支持: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                  HttpServletRequest request) {
        logger.warn("请求参数缺失: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.BAD_REQUEST, "缺少必要参数: " + e.getParameterName());
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                              HttpServletRequest request) {
        logger.warn("参数类型不匹配: {} - {}", request.getRequestURI(), e.getMessage());
        String message = String.format("参数 '%s' 类型不匹配，期望类型: %s",
            e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        return Result.fail(ResultCode.BAD_REQUEST, message);
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handleNoHandlerFoundException(NoHandlerFoundException e,
                                                  HttpServletRequest request) {
        logger.warn("404 Not Found: {} - {}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResultCode.NOT_FOUND, "请求的资源不存在");
    }

    /**
     * 处理所有其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        logger.error("未处理的异常: {} - {}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResultCode.INTERNAL_SERVER_ERROR, "服务器内部错误");
    }
}