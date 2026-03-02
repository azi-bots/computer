package com.computer.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.computer.ecommerce.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类Mapper接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}