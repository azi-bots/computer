package com.computer.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.ecommerce.entity.Order;

/**
 * 订单服务接口
 */
public interface IOrderService extends IService<Order> {

    /**
     * 根据用户ID分页查询订单
     */
    IPage<Order> listByUser(Long userId, Page<Order> page);

    /**
     * 根据供应商ID分页查询订单
     */
    IPage<Order> listBySupplier(Long supplierId, Page<Order> page);
}