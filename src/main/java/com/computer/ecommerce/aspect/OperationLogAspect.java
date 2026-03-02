package com.computer.ecommerce.aspect;

import com.alibaba.fastjson.JSON;
import com.computer.ecommerce.common.Result;
import com.computer.ecommerce.entity.OperationLog;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    /**
     * 定义切点：被@OperationLog注解的方法
     */
    @Pointcut("@annotation(com.computer.ecommerce.annotation.OperationLog)")
    public void operationLogPointcut() {}

    /**
     * 环绕通知：记录操作日志
     */
    @Around("operationLogPointcut()")
    public Object logOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.computer.ecommerce.annotation.OperationLog annotation = method.getAnnotation(com.computer.ecommerce.annotation.OperationLog.class);

        // 如果注解设置为忽略，直接执行原方法
        if (annotation.ignore()) {
            return joinPoint.proceed();
        }

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 获取用户信息
        UserInfo userInfo = getCurrentUserInfo();
        String userIp = request != null ? getClientIp(request) : "";
        String userAgent = request != null ? request.getHeader("User-Agent") : "";

        // 获取请求参数（根据注解决定是否保存）
        String requestParams = "";
        if (annotation.saveRequest()) {
            requestParams = getRequestParamsString(joinPoint);
        }

        Object result;
        boolean success = true;
        String errorMsg = null;
        String responseResult = null;

        try {
            // 执行原方法
            result = joinPoint.proceed();
            success = true;

            // 根据注解决定是否保存响应结果
            if (annotation.saveResponse() && result != null) {
                if (result instanceof Result) {
                    Result<?> apiResult = (Result<?>) result;
                    // 只保存基本信息，不保存数据内容（可能包含敏感信息）
                    responseResult = String.format("success: %s, code: %d, message: %s",
                        apiResult.isSuccess(), apiResult.getCode(), apiResult.getMessage());
                } else {
                    responseResult = result.getClass().getSimpleName();
                }
            }
        } catch (Throwable throwable) {
            success = false;
            errorMsg = throwable.getMessage();
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            // 构建操作日志
            OperationLog log = success ?
                OperationLog.success(
                    annotation.module(),
                    annotation.type(),
                    annotation.description(),
                    request != null ? request.getRequestURI() : "",
                    request != null ? request.getMethod() : "",
                    requestParams,
                    responseResult,
                    costTime,
                    userInfo.getUserId(),
                    userInfo.getUsername(),
                    userIp,
                    userAgent
                ) :
                OperationLog.failure(
                    annotation.module(),
                    annotation.type(),
                    annotation.description(),
                    request != null ? request.getRequestURI() : "",
                    request != null ? request.getMethod() : "",
                    requestParams,
                    errorMsg,
                    costTime,
                    userInfo.getUserId(),
                    userInfo.getUsername(),
                    userIp,
                    userAgent
                );

            // 保存操作日志（这里只记录到日志，实际项目中可以保存到数据库）
            saveOperationLog(log);
        }

        return result;
    }

    /**
     * 获取请求参数字符串
     */
    private String getRequestParamsString(ProceedingJoinPoint joinPoint) {
        try {
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

            return JSON.toJSONString(params);
        } catch (Exception e) {
            logger.warn("获取请求参数失败", e);
            return "{}";
        }
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
     * 获取当前用户信息
     */
    private UserInfo getCurrentUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetailsImpl) {
                    UserDetailsImpl userDetails = (UserDetailsImpl) principal;
                    return new UserInfo(userDetails.getUserId(), userDetails.getUsername());
                } else if (principal instanceof String) {
                    return new UserInfo(null, (String) principal);
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return new UserInfo(null, "anonymous");
    }

    /**
     * 保存操作日志（这里只记录到日志）
     */
    private void saveOperationLog(OperationLog log) {
        try {
            // 这里可以改为保存到数据库，使用异步处理
            logger.info("操作日志: 模块={}, 类型={}, 描述={}, 用户={}, IP={}, 状态={}, 耗时={}ms",
                log.getModule(), log.getType(), log.getDescription(),
                log.getUsername(), log.getUserIp(),
                log.getStatus() == 1 ? "成功" : "失败", log.getCostTime());

            // 实际项目中可以取消注释以下代码，异步保存到数据库
            // operationLogService.saveAsync(log);
        } catch (Exception e) {
            logger.error("保存操作日志失败", e);
        }
    }

    /**
     * 用户信息内部类
     */
    private static class UserInfo {
        private final Long userId;
        private final String username;

        public UserInfo(Long userId, String username) {
            this.userId = userId;
            this.username = username;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }
}