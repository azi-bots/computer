package com.computer.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.ecommerce.entity.Category;

import java.util.List;

/**
 * 商品分类服务接口
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 根据父分类ID查询子分类
     */
    List<Category> getByParentId(Long parentId);
}