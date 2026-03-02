package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.UserAddress;
import com.computer.ecommerce.mapper.UserAddressMapper;
import com.computer.ecommerce.service.IUserAddressService;
import com.computer.ecommerce.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 用户地址服务实现类
 */
@Service
@CacheConfig(cacheNames = "userAddress")
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements IUserAddressService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据ID查询用户地址（带缓存）
     */
    @Override
    @Cacheable(key = "#id")
    public UserAddress getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 保存用户地址（清除用户相关缓存）
     */
    @Override
    @CacheEvict(key = "'user:' + #entity.userId + '*")
    public boolean save(UserAddress entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新用户地址（更新缓存，清除用户相关缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    @CacheEvict(key = "'user:' + #entity.userId + '*")
    public boolean updateById(UserAddress entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除用户地址（清除缓存）
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean removeById(Serializable id) {
        UserAddress address = getById(id);
        boolean result = super.removeById(id);
        if (address != null) {
            // 清除用户相关的缓存
            redisUtil.clearByPrefix("userAddress::user:" + address.getUserId());
        }
        return result;
    }
}