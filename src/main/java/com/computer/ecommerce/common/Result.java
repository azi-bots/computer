package com.computer.ecommerce.common;

import lombok.Data;

/**
 * 统一响应结果
 */
@Data
public class Result<T> {
    private boolean success;
    private Integer code;
    private String message;
    private T data;

    public Result() {}

    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = success ? ResultCode.SUCCESS.getCode() : ResultCode.INTERNAL_SERVER_ERROR.getCode();
    }

    public Result(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> ok() {
        return new Result<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static Result<Void> ok(String message) {
        return new Result<>(true, ResultCode.SUCCESS.getCode(), message, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(true, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(true, ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(false, ResultCode.INTERNAL_SERVER_ERROR.getCode(), message, null);
    }

    public static <T> Result<T> fail(String message, T data) {
        return new Result<>(false, ResultCode.INTERNAL_SERVER_ERROR.getCode(), message, data);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(false, resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        return new Result<>(false, resultCode.getCode(), message, null);
    }

    public static <T> Result<T> fail(ResultCode resultCode, T data) {
        return new Result<>(false, resultCode.getCode(), resultCode.getMessage(), data);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String message, T data) {
        return new Result<>(false, resultCode.getCode(), message, data);
    }
}