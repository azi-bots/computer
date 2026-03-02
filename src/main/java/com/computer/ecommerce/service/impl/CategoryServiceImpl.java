package com.computer.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.ecommerce.entity.Category;
import com.computer.ecommerce.mapper.CategoryMapper;
import com.computer.ecommerce.service.ICategoryService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 商品分类服务实现类
 */
@Service
@CacheConfig(cacheNames = "category")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    /**
     * 根据ID查询分类（带缓存）
     */
    @Override
    @Cacheable(key = "#id")
    public Category getById(Serializable id) {
        return super.getById(id);
    }

    /**
     * 查询所有分类（带缓存）
     */
    @Override
    @Cacheable(key = "'all'")
    public List<Category> list() {
        return super.list();
    }

    /**
     * 根据父分类ID查询子分类（带缓存）
     */
    @Cacheable(key = "'parent:' + #parentId")
    public List<Category> getByParentId(Long parentId) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        return baseMapper.selectList(wrapper);
    }

    /**
     * 保存分类（清除相关缓存）
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean save(Category entity) {
        return super.save(entity);
    }

    /**
     * 根据ID更新分类（更新缓存）
     */
    @Override
    @CachePut(key = "#entity.id")
    public boolean updateById(Category entity) {
        return super.updateById(entity);
    }

    /**
     * 根据ID删除分类（清除缓存）
     */
    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }
}