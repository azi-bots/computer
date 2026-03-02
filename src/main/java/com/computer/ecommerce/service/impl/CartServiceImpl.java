package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.Cart;
import com.computer.ecommerce.mapper.CartMapper;
import com.computer.ecommerce.service.ICartService;
import com.computer.ecommerce.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 购物车服务实现类
 */
@Service
@CacheConfig(cacheNames = "cart")
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 根据ID查询购物车项（带短暂缓存）
     */
    @Override
    @Cacheable(key = "#id")
    public Cart getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 保存购物车项（清除用户相关缓存）
     */
    @Override
    @CacheEvict(key = "'user:' + #entity.userId + '*")
    public boolean save(Cart entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新购物车项（更新缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    @CacheEvict(key = "'user:' + #entity.userId + '*")
    public boolean updateById(Cart entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除购物车项（清除缓存）
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean removeById(Serializable id) {
        Cart cart = getById(id);
        boolean result = super.removeById(id);
        if (cart != null) {
            // 清除用户相关的缓存
            redisUtil.clearByPrefix("cart::user:" + cart.getUserId());
        }
        return result;
    }

    /**
     * 根据用户分页查询购物车项（带短暂缓存）
     */
    @Override
    @Cacheable(key = "'user:' + #userId + ':page:' + #page.current + ':size:' + #page.size")
    public IPage<Cart> listByUser(Long userId, Page<Cart> page) {
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectPage(page, wrapper);
    }
}