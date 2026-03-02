package com.computer.ecommerce.dto;

import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 商品请求DTO（用于创建和更新）
 */
@Data
public class ProductRequest {

    @NotBlank(message = "商品名称不能为空")
    @Size(min = 2, max = 100, message = "商品名称长度需在2-100个字符之间")
    private String name;

    @NotBlank(message = "商品描述不能为空")
    @Size(min = 10, max = 5000, message = "商品描述长度需在10-5000个字符之间")
    private String description;

    @Size(max = 200, message = "商品简介长度不能超过200个字符")
    private String brief;

    @NotNull(message = "商品分类ID不能为空")
    private Long categoryId;

    @Size(max = 50, message = "商品品牌长度不能超过50个字符")
    private String brand;

    @Size(max = 50, message = "商品型号长度不能超过50个字符")
    private String model;

    @NotBlank(message = "商品SKU不能为空")
    @Size(max = 50, message = "商品SKU长度不能超过50个字符")
    private String sku;

    @Size(max = 500, message = "商品主图URL长度不能超过500个字符")
    private String mainImage;

    private String carouselImages; // JSON数组

    private String detailImages; // JSON数组

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    private BigDecimal price;

    @DecimalMin(value = "0.01", message = "商品原价必须大于0")
    private BigDecimal originalPrice;

    @DecimalMin(value = "0.01", message = "商品成本价必须大于0")
    private BigDecimal costPrice;

    @NotNull(message = "商品库存数量不能为空")
    private Integer stockQuantity;

    private Integer warningStock = 10;

    @Size(max = 10, message = "商品单位长度不能超过10个字符")
    private String unit;

    @DecimalMin(value = "0", message = "商品重量不能为负数")
    private BigDecimal weight;

    @DecimalMin(value = "0", message = "商品体积不能为负数")
    private BigDecimal volume;

    private Integer status = 0; // 0-下架，1-上架

    private Integer auditStatus = 0; // 0-待审核

    private String auditRemark;

    private Long supplierId;

    private Integer isRecommended = 0;

    private Integer isNew = 1;

    private Integer isHot = 0;

    private String attributes; // JSON格式

    private String specifications; // JSON格式

    private String extraInfo; // JSON格式

    /**
     * 创建分组
     */
    public interface CreateGroup {}

    /**
     * 更新分组
     */
    public interface UpdateGroup {}
}