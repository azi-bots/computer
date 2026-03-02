package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.Payment;
import com.computer.ecommerce.mapper.PaymentMapper;
import com.computer.ecommerce.service.IPaymentService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 支付记录服务实现类
 */
@Service
@CacheConfig(cacheNames = "payment")
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IPaymentService {

    /**
     * 根据ID查询支付记录（带缓存）
     */
    @Override
    @Cacheable(key = "#id")
    public Payment getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 保存支付记录（清除相关缓存）
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean save(Payment entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新支付记录（更新缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    public boolean updateById(Payment entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除支付记录（清除缓存）
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}