package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.Product;
import com.computer.ecommerce.mapper.ProductMapper;
import com.computer.ecommerce.service.IProductService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 商品服务实现类
 */
@Service
@CacheConfig(cacheNames = "product")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    /**
     * 根据ID查询商品（带缓存）
     */
    @Override
    @Cacheable(key = "#id")
    public Product getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 保存商品（清除相关缓存）
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean save(Product entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新商品（更新缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    public boolean updateById(Product entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除商品（清除缓存）
     */
    @Override
    @CacheEvict(key = "#id")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    /**
     * 根据分类分页查询商品（带缓存，缓存时间较短）
     */
    @Override
    @Cacheable(key = "'category:' + #categoryId + ':page:' + #page.current + ':size:' + #page.size")
    public IPage<Product> listByCategory(Long categoryId, Page<Product> page) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", categoryId);
        return baseMapper.selectPage(page, wrapper);
    }

    /**
     * 根据供应商分页查询商品（带缓存，缓存时间较短）
     */
    @Override
    @Cacheable(key = "'supplier:' + #supplierId + ':page:' + #page.current + ':size:' + #page.size")
    public IPage<Product> listBySupplier(Long supplierId, Page<Product> page) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("supplier_id", supplierId);
        return baseMapper.selectPage(page, wrapper);
    }
}