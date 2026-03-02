package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.User;
import com.computer.ecommerce.mapper.UserMapper;
import com.computer.ecommerce.service.IUserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 用户服务实现类
 */
@Service
@CacheConfig(cacheNames = "user")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 根据ID查询用户（带缓存）
     */
    @Override
    @Cacheable(key = "#id")
    public User getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 保存用户（清除相关缓存）
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean save(User entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新用户（更新缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    public boolean updateById(User entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除用户（清除缓存）
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 检查用户名是否存在
     */
    @Override
    public boolean existsByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).count() > 0;
    }

    /**
     * 检查邮箱是否存在
     */
    @Override
    public boolean existsByEmail(String email) {
        return lambdaQuery().eq(User::getEmail, email).count() > 0;
    }

    /**
     * 检查手机号是否存在
     */
    @Override
    public boolean existsByPhone(String phone) {
        return lambdaQuery().eq(User::getPhone, phone).count() > 0;
    }
}