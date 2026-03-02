package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 订单项实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_item")
public class OrderItem extends BaseEntity {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品SKU
     */
    private String productSku;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String productImage;

    /**
     * 商品单价（单位：元）
     */
    private BigDecimal productPrice;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品总价（单价*数量）
     */
    private BigDecimal totalPrice;

    /**
     * 商品属性（JSON格式，例如：{"颜色":"黑色","内存":"16GB"}）
     */
    private String productAttributes;

    /**
     * 是否已评价：0-未评价，1-已评价
     */
    private Integer isCommented = 0;

    /**
     * 评价ID（关联评价表）
     */
    private Long reviewId;

    // 非数据库字段
    /**
     * 商品信息
     */
    @TableField(exist = false)
    private Product product;
}