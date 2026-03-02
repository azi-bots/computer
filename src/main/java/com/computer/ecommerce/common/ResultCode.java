package com.computer.ecommerce.common;

/**
 * 响应码枚举
 */
public enum ResultCode {

    SUCCESS(200, "成功"),
    CREATED(201, "创建成功"),
    ACCEPTED(202, "请求已接受"),
    NO_CONTENT(204, "无内容"),

    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    CONFLICT(409, "资源冲突"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    VALIDATION_FAILED(422, "数据验证失败"),

    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),

    // 业务错误码
    USER_NOT_FOUND(10001, "用户不存在"),
    USER_DISABLED(10002, "用户已被禁用"),
    USER_LOCKED(10003, "用户已被锁定"),
    USER_EXISTS(10004, "用户已存在"),
    INVALID_CREDENTIALS(10005, "用户名或密码错误"),
    TOKEN_EXPIRED(10006, "令牌已过期"),
    TOKEN_INVALID(10007, "令牌无效"),
    INSUFFICIENT_PERMISSIONS(10008, "权限不足"),
    DATA_NOT_FOUND(10009, "数据不存在"),
    DATA_EXISTS(10010, "数据已存在"),
    OPERATION_FAILED(10011, "操作失败"),
    INVALID_PARAMETER(10012, "参数无效"),
    FILE_UPLOAD_FAILED(10013, "文件上传失败"),
    FILE_TOO_LARGE(10014, "文件过大"),
    RATE_LIMIT_EXCEEDED(10015, "请求频率过高"),
    CAPTCHA_ERROR(10016, "验证码错误"),
    CAPTCHA_EXPIRED(10017, "验证码已过期");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}