package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存实体类（对应 inventory 表）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("inventory")
public class Inventory extends BaseEntity {
    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品SKU
     */
    private String sku;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 总库存数量
     */
    private Integer totalQuantity;

    /**
     * 可用库存数量
     */
    private Integer availableQuantity;

    /**
     * 锁定库存数量（已下单未支付）
     */
    private Integer lockedQuantity;

    /**
     * 已售库存数量
     */
    private Integer soldQuantity;

    /**
     * 预警库存数量
     */
    private Integer warningQuantity = 10;

    /**
     * 库存位置（仓库内位置）
     */
    private String location;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 过期日期
     */
    private LocalDate expiryDate;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 库存状态：0-停用，1-启用
     */
    private Integer status = 1;

    /**
     * 商品名称（非数据库字段）
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 商品品牌（非数据库字段）
     */
    @TableField(exist = false)
    private String productBrand;

    /**
     * 商品型号（非数据库字段）
     */
    @TableField(exist = false)
    private String productModel;

    /**
     * 商品主图（非数据库字段）
     */
    @TableField(exist = false)
    private String productImage;

    /**
     * 仓库名称（非数据库字段）
     */
    @TableField(exist = false)
    private String warehouseName;

    /**
     * 仓库编码（非数据库字段）
     */
    @TableField(exist = false)
    private String warehouseCode;

    /**
     * 供应商名称（非数据库字段）
     */
    @TableField(exist = false)
    private String supplierName;

    /**
     * 供应商公司（非数据库字段）
     */
    @TableField(exist = false)
    private String supplierCompany;
}