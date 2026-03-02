package com.computer.ecommerce.exception;

import com.computer.ecommerce.common.ResultCode;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {

    private final ResultCode resultCode;
    private final Object data;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.data = null;
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
        this.data = null;
    }

    public BusinessException(ResultCode resultCode, Object data) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.data = data;
    }

    public BusinessException(ResultCode resultCode, String message, Object data) {
        super(message);
        this.resultCode = resultCode;
        this.data = data;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public Object getData() {
        return data;
    }
}