package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("category")
public class Category extends BaseEntity {
    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 父分类ID（0表示根分类）
     */
    private Long parentId = 0L;

    /**
     * 分类级别（1-一级分类，2-二级分类，3-三级分类）
     */
    private Integer level;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 排序序号（越小越靠前）
     */
    private Integer sortOrder = 0;

    /**
     * 是否显示：0-隐藏，1-显示
     */
    private Integer isVisible = 1;

    /**
     * 分类路径（例如：0/1/2）
     */
    private String path;

    /**
     * 子分类数量
     */
    private Integer childCount = 0;
}