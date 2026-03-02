package com.computer.ecommerce.dto;

import lombok.Data;

/**
 * 认证响应DTO
 */
@Data
public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String nickname;
    private Integer userType;
    private String role;

    // 新增字段：用户对象和令牌过期时间（秒）
    private UserResponse user;
    private Long expiresIn;

    public AuthResponse() {
    }

    public AuthResponse(String token, Long userId, String username, String email,
                       String phone, String nickname, Integer userType, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.nickname = nickname;
        this.userType = userType;
        this.role = role;
    }

    public AuthResponse(String token, UserResponse user, Long expiresIn) {
        this.token = token;
        this.user = user;
        this.expiresIn = expiresIn;
        if (user != null) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.nickname = user.getNickname();
            this.userType = user.getUserType();
            // 将userType转换为角色字符串
            this.role = convertUserTypeToRole(user.getUserType());
        }
    }

    private String convertUserTypeToRole(Integer userType) {
        if (userType == null) return "USER";
        switch (userType) {
            case 2: return "SUPPLIER";
            case 3: return "ADMIN";
            default: return "USER";
        }
    }
}