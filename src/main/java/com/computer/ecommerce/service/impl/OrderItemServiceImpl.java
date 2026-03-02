package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.OrderItem;
import com.computer.ecommerce.mapper.OrderItemMapper;
import com.computer.ecommerce.service.IOrderItemService;
import com.computer.ecommerce.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 订单项服务实现类
 */
@Service
@CacheConfig(cacheNames = "orderItem")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据ID查询订单项（带缓存）
     */
    @Override
    @Cacheable(key = "#id")
    public OrderItem getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 保存订单项（清除订单相关缓存）
     */
    @Override
    @CacheEvict(key = "'order:' + #entity.orderId + '*")
    public boolean save(OrderItem entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新订单项（更新缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    @CacheEvict(key = "'order:' + #entity.orderId + '*")
    public boolean updateById(OrderItem entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除订单项（清除缓存）
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean removeById(Serializable id) {
        OrderItem orderItem = getById(id);
        boolean result = super.removeById(id);
        if (orderItem != null) {
            // 清除订单相关的缓存
            redisUtil.clearByPrefix("orderItem::order:" + orderItem.getOrderId());
        }
        return result;
    }
}