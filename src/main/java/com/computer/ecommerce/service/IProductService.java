package com.computer.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.ecommerce.entity.Product;

/**
 * 商品服务接口
 */
public interface IProductService extends IService<Product> {

    /**
     * 根据分类ID分页查询商品
     */
    IPage<Product> listByCategory(Long categoryId, Page<Product> page);

    /**
     * 根据供应商ID分页查询商品
     */
    IPage<Product> listBySupplier(Long supplierId, Page<Product> page);
}