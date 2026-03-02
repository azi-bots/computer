package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.Inventory;
import com.computer.ecommerce.mapper.InventoryMapper;
import com.computer.ecommerce.service.IInventoryService;
import com.computer.ecommerce.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 库存服务实现类
 */
@Service
@CacheConfig(cacheNames = "inventory")
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据ID查询库存记录（带短暂缓存）
     */
    @Override
    @Cacheable(key = "#id")
    public Inventory getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 保存库存记录（清除商品相关缓存）
     */
    @Override
    @CacheEvict(key = "'product:' + #entity.productId + '*")
    public boolean save(Inventory entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新库存记录（更新缓存，清除商品相关缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    @CacheEvict(key = "'product:' + #entity.productId + '*")
    public boolean updateById(Inventory entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除库存记录（清除缓存）
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean removeById(Serializable id) {
        Inventory inventory = getById(id);
        boolean result = super.removeById(id);
        if (inventory != null) {
            // 清除商品相关的缓存
            redisUtil.clearByPrefix("inventory::product:" + inventory.getProductId());
        }
        return result;
    }
}