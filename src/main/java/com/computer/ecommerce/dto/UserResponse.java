package com.computer.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户响应DTO
 */
@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String nickname;
    private String realName;
    private String avatar;
    private Integer userType;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 供应商特有字段
    private String companyName;
    private String businessLicense;
    private String contactName;
    private String contactPhone;
    private String supplierDescription;
    private Integer supplierLevel;
    private Integer supplierStatus;

    // 管理员特有字段
    private String adminRole;
    private String department;

    // 计算字段（用于前端） - 通过getter方法提供
    public String getRole() {
        if (userType == null) return "USER";
        switch (userType) {
            case 2: return "SUPPLIER";
            case 3: return "ADMIN";
            default: return "USER";
        }
    }

    @JsonIgnore
    public String getStatusString() {
        return getStatus();
    }

    public String getStatus() {
        if (status == null) return "INACTIVE";
        switch (status) {
            case 0: return "BANNED";
            case 1: return "ACTIVE";
            case 2: return "INACTIVE";
            default: return "INACTIVE";
        }
    }
}