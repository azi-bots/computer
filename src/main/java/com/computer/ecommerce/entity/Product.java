package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product")
public class Product extends BaseEntity {
    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述（HTML富文本）
     */
    private String description;

    /**
     * 商品简介（纯文本）
     */
    private String brief;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 商品品牌
     */
    private String brand;

    /**
     * 商品型号
     */
    private String model;

    /**
     * 商品SKU（库存单位，唯一）
     */
    private String sku;

    /**
     * 商品图片（主图）
     */
    private String mainImage;

    /**
     * 商品轮播图（JSON数组）
     */
    private String carouselImages;

    /**
     * 商品详情图（JSON数组）
     */
    private String detailImages;

    /**
     * 商品价格（单位：元）
     */
    private BigDecimal price;

    /**
     * 商品原价（单位：元）
     */
    private BigDecimal originalPrice;

    /**
     * 商品成本价（单位：元）
     */
    private BigDecimal costPrice;

    /**
     * 商品库存数量
     */
    private Integer stockQuantity;

    /**
     * 商品预警库存
     */
    private Integer warningStock = 10;

    /**
     * 商品单位（如：个、台、套）
     */
    private String unit;

    /**
     * 商品重量（kg）
     */
    private BigDecimal weight;

    /**
     * 商品体积（立方米）
     */
    private BigDecimal volume;

    /**
     * 商品状态：0-下架，1-上架，2-售罄，3-删除
     */
    private Integer status = 0;

    /**
     * 商品审核状态：0-待审核，1-审核通过，2-审核拒绝
     */
    private Integer auditStatus = 0;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 商品销量
     */
    private Integer salesCount = 0;

    /**
     * 商品点击量
     */
    private Integer clickCount = 0;

    /**
     * 商品收藏数
     */
    private Integer favoriteCount = 0;

    /**
     * 商品评分（1-5）
     */
    private BigDecimal rating = BigDecimal.ZERO;

    /**
     * 商品评价数
     */
    private Integer reviewCount = 0;

    /**
     * 供应商ID（用户ID）
     */
    private Long supplierId;

    /**
     * 是否推荐：0-不推荐，1-推荐
     */
    private Integer isRecommended = 0;

    /**
     * 是否新品：0-否，1-是
     */
    private Integer isNew = 1;

    /**
     * 是否热销：0-否，1-是
     */
    private Integer isHot = 0;

    /**
     * 商品属性（JSON格式，例如：{"颜色":"黑色","内存":"16GB"}）
     */
    private String attributes;

    /**
     * 商品规格（JSON格式，例如：[{"specName":"颜色","specValues":["黑色","白色"]}]）
     */
    private String specifications;

    /**
     * 扩展字段（JSON格式）
     */
    private String extraInfo;

    // 非数据库字段
    /**
     * 分类名称
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String supplierName;
}