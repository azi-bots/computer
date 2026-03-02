package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 购物车实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("cart")
public class Cart extends BaseEntity {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品SKU
     */
    private String productSku;

    /**
     * 商品数量
     */
    private Integer quantity = 1;

    /**
     * 是否选中：0-未选中，1-选中
     */
    private Integer selected = 1;

    /**
     * 商品单价（单位：元）
     */
    private BigDecimal price;

    /**
     * 商品总价（单价*数量）
     */
    private BigDecimal totalPrice;

    /**
     * 商品属性（JSON格式，例如：{"颜色":"黑色","内存":"16GB"}）
     */
    private String productAttributes;

    // 非数据库字段
    /**
     * 商品信息
     */
    @TableField(exist = false)
    private Product product;

    /**
     * 库存是否充足（0-不足，1-充足）
     */
    @TableField(exist = false)
    private Integer stockEnough = 1;
}