package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体类（统一管理普通用户、供应商、管理员）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {
    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 密码（BCrypt加密存储）
     */
    private String password;

    /**
     * 邮箱（唯一）
     */
    private String email;

    /**
     * 手机号（唯一）
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 用户类型：1-普通用户，2-供应商，3-管理员
     */
    private Integer userType;

    /**
     * 用户状态：0-禁用，1-正常，2-锁定
     */
    private Integer status = 1;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    // 供应商特有字段
    /**
     * 公司名称（供应商专用）
     */
    @TableField(exist = false)
    private String companyName;

    /**
     * 营业执照号（供应商专用）
     */
    @TableField(exist = false)
    private String businessLicense;

    /**
     * 联系人姓名（供应商专用）
     */
    @TableField(exist = false)
    private String contactName;

    /**
     * 联系人电话（供应商专用）
     */
    @TableField(exist = false)
    private String contactPhone;

    /**
     * 供应商描述（供应商专用）
     */
    @TableField(exist = false)
    private String supplierDescription;

    /**
     * 供应商等级（供应商专用）
     */
    @TableField(exist = false)
    private Integer supplierLevel;

    /**
     * 供应商状态（供应商专用）：0-未认证，1-已认证，2-已拒绝
     */
    @TableField(exist = false)
    private Integer supplierStatus;

    // 管理员特有字段
    /**
     * 管理员角色（管理员专用）
     */
    @TableField(exist = false)
    private String adminRole;

    /**
     * 部门（管理员专用）
     */
    @TableField(exist = false)
    private String department;
}