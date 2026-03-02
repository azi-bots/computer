package com.computer.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品响应DTO
 */
@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private String brief;
    private Long categoryId;
    private String categoryName;
    private String brand;
    private String model;
    private String sku;
    private String mainImage;
    private String carouselImages;
    private String detailImages;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private BigDecimal costPrice;
    private Integer stockQuantity;
    private Integer warningStock;
    private String unit;
    private BigDecimal weight;
    private BigDecimal volume;
    private Integer status;
    private Integer auditStatus;
    private String auditRemark;
    private Integer salesCount;
    private Integer clickCount;
    private Integer favoriteCount;
    private BigDecimal rating;
    private Integer reviewCount;
    private Long supplierId;
    private String supplierName;
    private Integer isRecommended;
    private Integer isNew;
    private Integer isHot;
    private String attributes;
    private String specifications;
    private String extraInfo;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}