package com.computer.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.ecommerce.entity.User;

/**
 * 用户服务接口
 */
public interface IUserService extends IService<User> {

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);
}