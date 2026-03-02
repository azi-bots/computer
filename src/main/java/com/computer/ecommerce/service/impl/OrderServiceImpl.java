package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.Order;
import com.computer.ecommerce.mapper.OrderMapper;
import com.computer.ecommerce.service.IOrderService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 订单服务实现类
 */
@Service
@CacheConfig(cacheNames = "order")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    /**
     * 根据ID查询订单（带缓存，缓存时间较短）
     */
    @Override
    @Cacheable(key = "#id")
    public Order getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 保存订单（清除相关缓存）
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean save(Order entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新订单（更新缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    public boolean updateById(Order entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除订单（清除缓存）
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 根据用户分页查询订单（带缓存，缓存时间很短）
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':page:' + #page.current + ':size:' + #page.size")
    public IPage<Order> listByUser(Long userId, Page<Order> page) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectPage(page, wrapper);
    }

    /**
     * 根据供应商分页查询订单（带缓存，缓存时间很短）
     */
    @Override
    @Cacheable(key = "'supplier:' + #supplierId + ':page:' + #page.current + ':size:' + #page.size")
    public IPage<Order> listBySupplier(Long supplierId, Page<Order> page) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("supplier_id", supplierId);
        return baseMapper.selectPage(page, wrapper);
    }
}