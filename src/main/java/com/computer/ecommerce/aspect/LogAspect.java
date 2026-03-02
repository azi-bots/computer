package com.computer.ecommerce.aspect;

import com.alibaba.fastjson.JSON;
import com.computer.ecommerce.common.Result;
import com.computer.ecommerce.security.UserDetailsImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API日志切面
 */
@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义切点：所有Controller包下的方法
     */
    @Pointcut("execution(* com.computer.ecommerce.controller..*.*(..))")
    public void controllerPointcut() {}

    /**
     * 环绕通知：记录API请求和响应
     */
    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();

        // 获取请求参数（排除敏感参数和文件参数）
        Map<String, Object> requestParams = getRequestParams(joinPoint);

        // 获取当前用户信息
        String username = getCurrentUsername();

        // 记录请求日志
        if (request != null) {
            logger.info("API请求开始 >>> URL: {} {}, Method: {}, IP: {}, User: {}, Class: {}, Method: {}, Params: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getMethod(),
                getClientIp(request),
                username,
                className,
                methodName,
                JSON.toJSONString(requestParams)
            );
        } else {
            logger.info("API请求开始 >>> Class: {}, Method: {}, Params: {}, User: {}",
                className, methodName, JSON.toJSONString(requestParams), username);
        }

        Object result;
        try {
            // 执行原方法
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            // 记录异常日志
            logger.error("API请求异常 <<< Class: {}, Method: {}, Cost: {}ms, Exception: {}",
                className, methodName, costTime, throwable.getMessage(), throwable);

            throw throwable;
        }

        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        // 记录响应日志（排除敏感数据）
        String responseStr = "";
        if (result != null) {
            if (result instanceof Result) {
                Result<?> apiResult = (Result<?>) result;
                // 只记录成功状态和基本信息，不记录数据内容（可能包含敏感信息）
                responseStr = String.format("success: %s, code: %d, message: %s",
                    apiResult.isSuccess(), apiResult.getCode(), apiResult.getMessage());
            } else {
                responseStr = result.getClass().getSimpleName();
            }
        }

        logger.info("API请求完成 <<< Class: {}, Method: {}, Cost: {}ms, Response: {}",
            className, methodName, costTime, responseStr);

        return result;
    }

    /**
     * 获取请求参数（过滤掉敏感信息和文件参数）
     */
    private Map<String, Object> getRequestParams(ProceedingJoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<>();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        if (paramNames != null && paramValues != null) {
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object paramValue = paramValues[i];

                // 过滤掉敏感参数
                if (isSensitiveParam(paramName)) {
                    params.put(paramName, "***");
                    continue;
                }

                // 处理文件参数
                if (paramValue instanceof MultipartFile) {
                    MultipartFile file = (MultipartFile) paramValue;
                    params.put(paramName, String.format("File[name=%s, size=%d]",
                        file.getOriginalFilename(), file.getSize()));
                    continue;
                }

                // 处理多个文件参数
                if (paramValue instanceof MultipartFile[]) {
                    MultipartFile[] files = (MultipartFile[]) paramValue;
                    String fileInfo = Arrays.stream(files)
                        .map(f -> String.format("File[name=%s, size=%d]",
                            f.getOriginalFilename(), f.getSize()))
                        .collect(Collectors.joining(", ", "[", "]"));
                    params.put(paramName, fileInfo);
                    continue;
                }

                // 过滤HttpServletRequest和HttpServletResponse
                if (paramValue instanceof HttpServletRequest ||
                    paramValue instanceof HttpServletResponse) {
                    continue;
                }

                // 其他参数正常记录
                params.put(paramName, paramValue);
            }
        }

        return params;
    }

    /**
     * 判断是否为敏感参数
     */
    private boolean isSensitiveParam(String paramName) {
        String[] sensitiveKeywords = {"password", "pwd", "token", "secret", "key", "credit", "card"};
        String lowerParamName = paramName.toLowerCase();
        for (String keyword : sensitiveKeywords) {
            if (lowerParamName.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个IP时取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * 获取当前用户名
     */
    private String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetailsImpl) {
                    return ((UserDetailsImpl) principal).getUsername();
                } else if (principal instanceof String) {
                    return (String) principal;
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return "anonymous";
    }

    /**
     * 异常通知：记录异常信息
     */
    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();

        logger.error("API执行异常 >>> Class: {}, Method: {}, Exception: {}",
            className, methodName, e.getMessage(), e);
    }
}