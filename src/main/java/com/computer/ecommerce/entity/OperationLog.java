package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("operation_log")
public class OperationLog extends BaseEntity {

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作类型（CREATE, UPDATE, DELETE, QUERY, etc.）
     */
    private String type;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求方法（GET, POST, PUT, DELETE, etc.）
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应结果
     */
    private String responseResult;

    /**
     * 操作状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 耗时（毫秒）
     */
    private Long costTime;

    /**
     * 操作用户ID
     */
    private Long userId;

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 用户IP地址
     */
    private String userIp;

    /**
     * 用户代理（浏览器信息）
     */
    private String userAgent;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime = LocalDateTime.now();

    /**
     * 创建成功日志
     */
    public static OperationLog success(String module, String type, String description,
                                      String requestUrl, String requestMethod, String requestParams,
                                      String responseResult, Long costTime, Long userId, String username,
                                      String userIp, String userAgent) {
        OperationLog log = new OperationLog();
        log.setModule(module);
        log.setType(type);
        log.setDescription(description);
        log.setRequestUrl(requestUrl);
        log.setRequestMethod(requestMethod);
        log.setRequestParams(requestParams);
        log.setResponseResult(responseResult);
        log.setStatus(1);
        log.setCostTime(costTime);
        log.setUserId(userId);
        log.setUsername(username);
        log.setUserIp(userIp);
        log.setUserAgent(userAgent);
        return log;
    }

    /**
     * 创建失败日志
     */
    public static OperationLog failure(String module, String type, String description,
                                      String requestUrl, String requestMethod, String requestParams,
                                      String errorMsg, Long costTime, Long userId, String username,
                                      String userIp, String userAgent) {
        OperationLog log = new OperationLog();
        log.setModule(module);
        log.setType(type);
        log.setDescription(description);
        log.setRequestUrl(requestUrl);
        log.setRequestMethod(requestMethod);
        log.setRequestParams(requestParams);
        log.setStatus(0);
        log.setErrorMsg(errorMsg);
        log.setCostTime(costTime);
        log.setUserId(userId);
        log.setUsername(username);
        log.setUserIp(userIp);
        log.setUserAgent(userAgent);
        return log;
    }
}