package com.computer.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.ecommerce.entity.Cart;

/**
 * 购物车服务接口
 */
public interface ICartService extends IService<Cart> {

    /**
     * 根据用户ID分页查询购物车项
     */
    IPage<Cart> listByUser(Long userId, Page<Cart> page);
}