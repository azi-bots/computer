package com.computer.ecommerce.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 用户请求DTO（用于创建和更新）
 */
@Data
public class UserRequest {

    @NotBlank(message = "用户名不能为空", groups = {CreateGroup.class})
    @Size(min = 3, max = 20, message = "用户名长度需在3-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @NotBlank(message = "密码不能为空", groups = {CreateGroup.class})
    @Size(min = 6, max = 32, message = "密码长度需在6-32个字符之间", groups = {CreateGroup.class})
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @Size(max = 20, message = "昵称长度不能超过20个字符")
    private String nickname;

    @Size(max = 20, message = "真实姓名长度不能超过20个字符")
    private String realName;

    @Size(max = 200, message = "头像URL长度不能超过200个字符")
    private String avatar;

    private Integer userType = 1; // 默认普通用户

    private Integer status = 1; // 默认正常状态

    /**
     * 创建分组
     */
    public interface CreateGroup {}

    /**
     * 更新分组
     */
    public interface UpdateGroup {}
}