-- 计算机电商平台数据库设计
-- 版本: 1.0.0
-- 创建日期: 2026-03-02
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_general_ci
-- 存储引擎: InnoDB

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `computer_ecommerce`
CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE `computer_ecommerce`;

-- ==================== 基础表：用户相关 ====================

-- 用户表（统一管理普通用户、供应商、管理员）
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名（唯一）',
    `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密存储）',
    `email` VARCHAR(100) NOT NULL COMMENT '邮箱（唯一）',
    `phone` VARCHAR(20) NULL COMMENT '手机号（唯一）',
    `real_name` VARCHAR(50) NULL COMMENT '真实姓名',
    `nickname` VARCHAR(50) NULL COMMENT '昵称',
    `avatar` VARCHAR(500) NULL COMMENT '头像URL',
    `user_type` TINYINT NOT NULL DEFAULT 1 COMMENT '用户类型：1-普通用户，2-供应商，3-管理员',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常，2-锁定',
    `last_login_time` DATETIME NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) NULL COMMENT '最后登录IP',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_user_type` (`user_type`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- 供应商信息表（扩展表，用户类型为2）
CREATE TABLE IF NOT EXISTS `supplier_info` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '供应商信息ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `company_name` VARCHAR(100) NOT NULL COMMENT '公司名称',
    `business_license` VARCHAR(100) NOT NULL COMMENT '营业执照号',
    `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系人电话',
    `supplier_description` TEXT NULL COMMENT '供应商描述',
    `supplier_level` TINYINT NOT NULL DEFAULT 1 COMMENT '供应商等级：1-普通，2-银牌，3-金牌，4-钻石',
    `supplier_status` TINYINT NOT NULL DEFAULT 0 COMMENT '供应商状态：0-未认证，1-已认证，2-已拒绝',
    `certification_time` DATETIME NULL COMMENT '认证时间',
    `rejection_reason` VARCHAR(500) NULL COMMENT '拒绝原因',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_business_license` (`business_license`),
    KEY `idx_supplier_status` (`supplier_status`),
    KEY `idx_supplier_level` (`supplier_level`),
    CONSTRAINT `fk_supplier_info_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='供应商信息表';

-- 管理员信息表（扩展表，用户类型为3）
CREATE TABLE IF NOT EXISTS `admin_info` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '管理员信息ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `admin_role` VARCHAR(50) NOT NULL DEFAULT 'admin' COMMENT '管理员角色：admin-超级管理员，editor-内容编辑，operator-运营人员',
    `department` VARCHAR(100) NULL COMMENT '部门',
    `permissions` TEXT NULL COMMENT '权限列表（JSON数组）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_admin_role` (`admin_role`),
    CONSTRAINT `fk_admin_info_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='管理员信息表';

-- 用户收货地址表
CREATE TABLE IF NOT EXISTS `user_address` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '地址ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区县',
    `detail_address` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `post_code` VARCHAR(10) NULL COMMENT '邮政编码',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
    `address_tag` VARCHAR(20) NULL COMMENT '地址标签（如：家、公司、学校）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_default` (`is_default`),
    CONSTRAINT `fk_user_address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户收货地址表';

-- ==================== 基础表：商品相关 ====================

-- 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(500) NULL COMMENT '分类描述',
    `parent_id` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父分类ID（0表示根分类）',
    `level` TINYINT NOT NULL DEFAULT 1 COMMENT '分类级别（1-一级分类，2-二级分类，3-三级分类）',
    `icon` VARCHAR(500) NULL COMMENT '分类图标',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序序号（越小越靠前）',
    `is_visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示：0-隐藏，1-显示',
    `path` VARCHAR(200) NOT NULL DEFAULT '0' COMMENT '分类路径（例如：0/1/2）',
    `child_count` INT NOT NULL DEFAULT 0 COMMENT '子分类数量',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_level` (`level`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_is_visible` (`is_visible`),
    KEY `idx_path` (`path`(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS `product` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `description` TEXT NOT NULL COMMENT '商品描述（HTML富文本）',
    `brief` VARCHAR(500) NULL COMMENT '商品简介（纯文本）',
    `category_id` BIGINT UNSIGNED NOT NULL COMMENT '商品分类ID',
    `brand` VARCHAR(100) NULL COMMENT '商品品牌',
    `model` VARCHAR(100) NULL COMMENT '商品型号',
    `sku` VARCHAR(100) NOT NULL COMMENT '商品SKU（库存单位，唯一）',
    `main_image` VARCHAR(500) NULL COMMENT '商品图片（主图）',
    `carousel_images` TEXT NULL COMMENT '商品轮播图（JSON数组）',
    `detail_images` TEXT NULL COMMENT '商品详情图（JSON数组）',
    `price` DECIMAL(10, 2) NOT NULL COMMENT '商品价格（单位：元）',
    `original_price` DECIMAL(10, 2) NULL COMMENT '商品原价（单位：元）',
    `cost_price` DECIMAL(10, 2) NULL COMMENT '商品成本价（单位：元）',
    `stock_quantity` INT NOT NULL DEFAULT 0 COMMENT '商品库存数量',
    `warning_stock` INT NOT NULL DEFAULT 10 COMMENT '商品预警库存',
    `unit` VARCHAR(10) NULL COMMENT '商品单位（如：个、台、套）',
    `weight` DECIMAL(10, 3) NULL COMMENT '商品重量（kg）',
    `volume` DECIMAL(10, 3) NULL COMMENT '商品体积（立方米）',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '商品状态：0-下架，1-上架，2-售罄，3-删除',
    `audit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '商品审核状态：0-待审核，1-审核通过，2-审核拒绝',
    `audit_remark` VARCHAR(500) NULL COMMENT '审核备注',
    `sales_count` INT NOT NULL DEFAULT 0 COMMENT '商品销量',
    `click_count` INT NOT NULL DEFAULT 0 COMMENT '商品点击量',
    `favorite_count` INT NOT NULL DEFAULT 0 COMMENT '商品收藏数',
    `rating` DECIMAL(3, 2) NOT NULL DEFAULT 0.00 COMMENT '商品评分（1-5）',
    `review_count` INT NOT NULL DEFAULT 0 COMMENT '商品评价数',
    `supplier_id` BIGINT UNSIGNED NOT NULL COMMENT '供应商ID（用户ID）',
    `is_recommended` TINYINT NOT NULL DEFAULT 0 COMMENT '是否推荐：0-不推荐，1-推荐',
    `is_new` TINYINT NOT NULL DEFAULT 1 COMMENT '是否新品：0-否，1-是',
    `is_hot` TINYINT NOT NULL DEFAULT 0 COMMENT '是否热销：0-否，1-是',
    `attributes` TEXT NULL COMMENT '商品属性（JSON格式，例如：{"颜色":"黑色","内存":"16GB"}）',
    `specifications` TEXT NULL COMMENT '商品规格（JSON格式，例如：[{"specName":"颜色","specValues":["黑色","白色"]}]）',
    `extra_info` TEXT NULL COMMENT '扩展字段（JSON格式）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sku` (`sku`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_status` (`status`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_is_recommended` (`is_recommended`),
    KEY `idx_is_new` (`is_new`),
    KEY `idx_is_hot` (`is_hot`),
    KEY `idx_rating` (`rating`),
    KEY `idx_sales_count` (`sales_count`),
    KEY `idx_created_at` (`created_at`),
    CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_product_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品表';

-- 商品评价表
CREATE TABLE IF NOT EXISTS `product_review` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
    `order_item_id` BIGINT UNSIGNED NOT NULL COMMENT '订单项ID',
    `rating` TINYINT NOT NULL COMMENT '评分（1-5星）',
    `content` TEXT NOT NULL COMMENT '评价内容',
    `images` TEXT NULL COMMENT '评价图片（JSON数组）',
    `is_anonymous` TINYINT NOT NULL DEFAULT 0 COMMENT '是否匿名：0-否，1-是',
    `reply_content` TEXT NULL COMMENT '商家回复内容',
    `reply_time` DATETIME NULL COMMENT '商家回复时间',
    `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览数',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    `is_recommended` TINYINT NOT NULL DEFAULT 0 COMMENT '是否精选：0-否，1-是',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '评价状态：0-隐藏，1-显示，2-待审核',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_item_id` (`order_item_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_rating` (`rating`),
    KEY `idx_status` (`status`),
    KEY `idx_is_top` (`is_top`),
    KEY `idx_is_recommended` (`is_recommended`),
    KEY `idx_created_at` (`created_at`),
    CONSTRAINT `fk_product_review_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_product_review_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_product_review_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品评价表';

-- 商品收藏表
CREATE TABLE IF NOT EXISTS `product_favorite` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    CONSTRAINT `fk_product_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_product_favorite_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品收藏表';

-- 商品浏览历史表
CREATE TABLE IF NOT EXISTS `product_view_history` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '浏览记录ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `view_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    `duration` INT NULL COMMENT '浏览时长（秒）',
    `ip_address` VARCHAR(50) NULL COMMENT 'IP地址',
    `user_agent` VARCHAR(500) NULL COMMENT '用户代理',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_view_time` (`view_time`),
    CONSTRAINT `fk_product_view_history_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_product_view_history_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品浏览历史表';

-- ==================== 基础表：订单相关 ====================

-- 订单表（注意：order是MySQL保留字，使用反引号）
CREATE TABLE IF NOT EXISTS `order` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号（唯一）',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '订单总金额（单位：元）',
    `pay_amount` DECIMAL(10, 2) NOT NULL COMMENT '实付金额（单位：元）',
    `freight_amount` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '运费金额（单位：元）',
    `discount_amount` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额（单位：元）',
    `order_status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0-待付款，1-已付款，2-待发货，3-已发货，4-已完成，5-已取消，6-已关闭，7-退款中，8-已退款',
    `pay_status` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0-未支付，1-支付中，2-已支付，3-支付失败，4-已退款',
    `pay_type` TINYINT NULL COMMENT '支付方式：1-微信支付，2-支付宝，3-银行卡，4-货到付款',
    `pay_time` DATETIME NULL COMMENT '支付时间',
    `transaction_id` VARCHAR(100) NULL COMMENT '支付交易号',
    `delivery_status` TINYINT NOT NULL DEFAULT 0 COMMENT '发货状态：0-未发货，1-部分发货，2-已发货',
    `delivery_time` DATETIME NULL COMMENT '发货时间',
    `logistics_company` VARCHAR(100) NULL COMMENT '物流公司',
    `logistics_no` VARCHAR(100) NULL COMMENT '物流单号',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
    `receiver_address` VARCHAR(500) NOT NULL COMMENT '收货地址（省市区街道详细地址）',
    `receiver_post_code` VARCHAR(10) NULL COMMENT '收货地址邮编',
    `user_remark` VARCHAR(500) NULL COMMENT '用户订单备注',
    `merchant_remark` VARCHAR(500) NULL COMMENT '商家备注',
    `order_source` TINYINT NOT NULL DEFAULT 1 COMMENT '订单来源：1-Web，2-Android，3-iOS，4-小程序',
    `auto_confirm_days` INT NOT NULL DEFAULT 7 COMMENT '自动确认收货时间（天数）',
    `confirm_time` DATETIME NULL COMMENT '确认收货时间',
    `finish_time` DATETIME NULL COMMENT '订单完成时间',
    `cancel_time` DATETIME NULL COMMENT '订单取消时间',
    `cancel_reason` VARCHAR(500) NULL COMMENT '订单取消原因',
    `is_commented` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已评价：0-未评价，1-已评价',
    `invoice_type` TINYINT NOT NULL DEFAULT 0 COMMENT '发票类型：0-不开发票，1-个人发票，2-公司发票',
    `invoice_title` VARCHAR(200) NULL COMMENT '发票抬头',
    `taxpayer_id` VARCHAR(50) NULL COMMENT '纳税人识别号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_status` (`order_status`),
    KEY `idx_pay_status` (`pay_status`),
    KEY `idx_pay_time` (`pay_time`),
    KEY `idx_delivery_status` (`delivery_status`),
    KEY `idx_created_at` (`created_at`),
    KEY `idx_order_source` (`order_source`),
    CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单表';

-- 订单项表
CREATE TABLE IF NOT EXISTS `order_item` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
    `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `product_sku` VARCHAR(100) NOT NULL COMMENT '商品SKU',
    `product_name` VARCHAR(200) NOT NULL COMMENT '商品名称',
    `product_image` VARCHAR(500) NULL COMMENT '商品主图',
    `product_price` DECIMAL(10, 2) NOT NULL COMMENT '商品单价（单位：元）',
    `quantity` INT NOT NULL COMMENT '商品数量',
    `total_price` DECIMAL(10, 2) NOT NULL COMMENT '商品总价（单价*数量）',
    `product_attributes` TEXT NULL COMMENT '商品属性（JSON格式，例如：{"颜色":"黑色","内存":"16GB"}）',
    `is_commented` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已评价：0-未评价，1-已评价',
    `review_id` BIGINT UNSIGNED NULL COMMENT '评价ID（关联评价表）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_review_id` (`review_id`),
    CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_order_item_review` FOREIGN KEY (`review_id`) REFERENCES `product_review` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='订单项表';

-- ==================== 基础表：购物车 ====================

-- 购物车表
CREATE TABLE IF NOT EXISTS `cart` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `product_sku` VARCHAR(100) NOT NULL COMMENT '商品SKU',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '商品数量',
    `selected` TINYINT NOT NULL DEFAULT 1 COMMENT '是否选中：0-未选中，1-选中',
    `price` DECIMAL(10, 2) NOT NULL COMMENT '商品单价（单位：元）',
    `total_price` DECIMAL(10, 2) NOT NULL COMMENT '商品总价（单价*数量）',
    `product_attributes` TEXT NULL COMMENT '商品属性（JSON格式，例如：{"颜色":"黑色","内存":"16GB"}）',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product_sku` (`user_id`, `product_id`, `product_sku`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_selected` (`selected`),
    CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='购物车表';

-- ==================== 基础表：支付相关 ====================

-- 支付记录表
CREATE TABLE IF NOT EXISTS `payment` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
    `payment_no` VARCHAR(50) NOT NULL COMMENT '支付单号（唯一）',
    `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    `amount` DECIMAL(10, 2) NOT NULL COMMENT '支付金额（单位：元）',
    `pay_type` TINYINT NOT NULL COMMENT '支付方式：1-微信支付，2-支付宝，3-银行卡，4-货到付款',
    `pay_status` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态：0-待支付，1-支付中，2-支付成功，3-支付失败，4-已退款',
    `pay_time` DATETIME NULL COMMENT '支付时间',
    `transaction_id` VARCHAR(100) NULL COMMENT '支付交易号（第三方支付平台返回）',
    `remark` VARCHAR(500) NULL COMMENT '支付备注',
    `pay_ip` VARCHAR(50) NULL COMMENT '支付IP地址',
    `pay_device` VARCHAR(500) NULL COMMENT '支付设备信息',
    `raw_response` TEXT NULL COMMENT '支付渠道返回的原始数据（JSON格式）',
    `refund_amount` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '退款金额（单位：元）',
    `refund_time` DATETIME NULL COMMENT '退款时间',
    `refund_reason` VARCHAR(500) NULL COMMENT '退款原因',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_pay_status` (`pay_status`),
    KEY `idx_pay_time` (`pay_time`),
    KEY `idx_created_at` (`created_at`),
    CONSTRAINT `fk_payment_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_payment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付记录表';

-- ==================== 基础表：库存管理 ====================

-- 库存表
CREATE TABLE IF NOT EXISTS `inventory` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '库存记录ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `sku` VARCHAR(100) NOT NULL COMMENT '商品SKU',
    `warehouse_id` BIGINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '仓库ID',
    `total_quantity` INT NOT NULL DEFAULT 0 COMMENT '总库存数量',
    `available_quantity` INT NOT NULL DEFAULT 0 COMMENT '可用库存数量',
    `locked_quantity` INT NOT NULL DEFAULT 0 COMMENT '锁定库存数量（已下单未支付）',
    `sold_quantity` INT NOT NULL DEFAULT 0 COMMENT '已售库存数量',
    `warning_quantity` INT NOT NULL DEFAULT 10 COMMENT '预警库存数量',
    `location` VARCHAR(100) NULL COMMENT '库存位置（仓库内位置）',
    `batch_no` VARCHAR(50) NULL COMMENT '批次号',
    `production_date` DATE NULL COMMENT '生产日期',
    `expiry_date` DATE NULL COMMENT '过期日期',
    `cost_price` DECIMAL(10, 2) NULL COMMENT '成本价',
    `supplier_id` BIGINT UNSIGNED NOT NULL COMMENT '供应商ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '库存状态：0-停用，1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_sku_warehouse` (`product_id`, `sku`, `warehouse_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_sku` (`sku`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_supplier_id` (`supplier_id`),
    KEY `idx_status` (`status`),
    KEY `idx_available_quantity` (`available_quantity`),
    KEY `idx_expiry_date` (`expiry_date`),
    CONSTRAINT `fk_inventory_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_inventory_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='库存表';

-- 库存操作日志表
CREATE TABLE IF NOT EXISTS `inventory_log` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `inventory_id` BIGINT UNSIGNED NOT NULL COMMENT '库存ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
    `sku` VARCHAR(100) NOT NULL COMMENT '商品SKU',
    `warehouse_id` BIGINT UNSIGNED NOT NULL COMMENT '仓库ID',
    `operation_type` TINYINT NOT NULL COMMENT '操作类型：1-入库，2-出库，3-调拨，4-盘点，5-锁定，6-解锁',
    `quantity` INT NOT NULL COMMENT '操作数量（正数表示增加，负数表示减少）',
    `before_quantity` INT NOT NULL COMMENT '操作前数量',
    `after_quantity` INT NOT NULL COMMENT '操作后数量',
    `order_id` BIGINT UNSIGNED NULL COMMENT '关联订单ID',
    `order_item_id` BIGINT UNSIGNED NULL COMMENT '关联订单项ID',
    `remark` VARCHAR(500) NULL COMMENT '操作备注',
    `operator_id` BIGINT UNSIGNED NOT NULL COMMENT '操作员ID',
    `operator_name` VARCHAR(50) NOT NULL COMMENT '操作员姓名',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_inventory_id` (`inventory_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_sku` (`sku`),
    KEY `idx_warehouse_id` (`warehouse_id`),
    KEY `idx_operation_type` (`operation_type`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_operator_id` (`operator_id`),
    KEY `idx_created_at` (`created_at`),
    CONSTRAINT `fk_inventory_log_inventory` FOREIGN KEY (`inventory_id`) REFERENCES `inventory` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_inventory_log_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_inventory_log_order` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='库存操作日志表';

-- 仓库表
CREATE TABLE IF NOT EXISTS `warehouse` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
    `name` VARCHAR(100) NOT NULL COMMENT '仓库名称',
    `code` VARCHAR(50) NOT NULL COMMENT '仓库编码（唯一）',
    `type` TINYINT NOT NULL DEFAULT 1 COMMENT '仓库类型：1-中心仓，2-区域仓，3-门店仓',
    `address` VARCHAR(500) NOT NULL COMMENT '仓库地址',
    `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系人电话',
    `area` DECIMAL(10, 2) NULL COMMENT '仓库面积（平方米）',
    `capacity` INT NULL COMMENT '仓库容量',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '仓库状态：0-停用，1-启用',
    `manager_id` BIGINT UNSIGNED NULL COMMENT '仓库管理员ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标志（0=未删除，1=已删除）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`),
    KEY `idx_manager_id` (`manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='仓库表';

-- ==================== 基础表：系统管理 ====================

-- 操作日志表
CREATE TABLE IF NOT EXISTS `operation_log` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `module` VARCHAR(50) NOT NULL COMMENT '操作模块',
    `type` VARCHAR(50) NOT NULL COMMENT '操作类型（CREATE, UPDATE, DELETE, QUERY, etc.）',
    `description` VARCHAR(500) NOT NULL COMMENT '操作描述',
    `request_url` VARCHAR(500) NOT NULL COMMENT '请求URL',
    `request_method` VARCHAR(10) NOT NULL COMMENT '请求方法（GET, POST, PUT, DELETE, etc.）',
    `request_params` TEXT NULL COMMENT '请求参数',
    `response_result` TEXT NULL COMMENT '响应结果',
    `status` TINYINT NOT NULL COMMENT '操作状态（0-失败，1-成功）',
    `error_msg` TEXT NULL COMMENT '错误信息',
    `cost_time` BIGINT NOT NULL COMMENT '耗时（毫秒）',
    `user_id` BIGINT UNSIGNED NULL COMMENT '操作用户ID',
    `username` VARCHAR(50) NULL COMMENT '操作用户名',
    `user_ip` VARCHAR(50) NULL COMMENT '用户IP地址',
    `user_agent` VARCHAR(500) NULL COMMENT '用户代理（浏览器信息）',
    `operation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_module` (`module`),
    KEY `idx_type` (`type`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_username` (`username`),
    KEY `idx_status` (`status`),
    KEY `idx_operation_time` (`operation_time`),
    KEY `idx_created_at` (`created_at`),
    CONSTRAINT `fk_operation_log_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志表';

-- ==================== 初始化数据 ====================

-- 插入超级管理员用户（初始密码：admin123）
INSERT IGNORE INTO `user` (`id`, `username`, `password`, `email`, `phone`, `real_name`, `nickname`, `user_type`, `status`, `created_at`, `updated_at`) VALUES
(1, 'admin', '$2a$10$7C6c5z5q5q5q5q5q5q5q5u5u5u5u5u5u5u5u5u5u5u5u5u5u5u5u5u', 'admin@computer.com', '13800138000', '超级管理员', 'Admin', 3, 1, NOW(), NOW());

-- 插入管理员信息
INSERT IGNORE INTO `admin_info` (`user_id`, `admin_role`, `department`, `permissions`) VALUES
(1, 'admin', '技术部', '["*"]');

-- 插入根分类
INSERT IGNORE INTO `category` (`id`, `name`, `description`, `parent_id`, `level`, `sort_order`, `path`) VALUES
(1, '电脑整机', '台式机、笔记本、一体机等', 0, 1, 1, '0'),
(2, '电脑配件', 'CPU、内存、硬盘、显卡等', 0, 1, 2, '0'),
(3, '外设产品', '显示器、键盘、鼠标、音响等', 0, 1, 3, '0');

-- 插入子分类
INSERT IGNORE INTO `category` (`name`, `description`, `parent_id`, `level`, `sort_order`, `path`) VALUES
-- 电脑整机子分类
('笔记本电脑', '轻薄本、游戏本、商务本等', 1, 2, 1, '0/1'),
('台式电脑', '品牌台式机、DIY台式机等', 1, 2, 2, '0/1'),
('一体机', '苹果iMac、微软Surface Studio等', 1, 2, 3, '0/1'),
('工作站', '图形工作站、服务器等', 1, 2, 4, '0/1'),

-- 电脑配件子分类
('CPU处理器', 'Intel、AMD等品牌CPU', 2, 2, 1, '0/2'),
('主板', 'ATX、MATX、ITX等规格主板', 2, 2, 2, '0/2'),
('内存', 'DDR4、DDR5内存条', 2, 2, 3, '0/2'),
('硬盘', 'SSD固态硬盘、HDD机械硬盘', 2, 2, 4, '0/2'),
('显卡', 'NVIDIA、AMD独立显卡', 2, 2, 5, '0/2'),
('电源', '台式机电源、服务器电源', 2, 2, 6, '0/2'),
('散热器', 'CPU散热器、机箱风扇', 2, 2, 7, '0/2'),
('机箱', 'ATX机箱、MATX机箱、ITX机箱', 2, 2, 8, '0/2'),

-- 外设产品子分类
('显示器', '电竞显示器、设计显示器等', 3, 2, 1, '0/3'),
('键盘', '机械键盘、薄膜键盘、无线键盘', 3, 2, 2, '0/3'),
('鼠标', '游戏鼠标、办公鼠标、无线鼠标', 3, 2, 3, '0/3'),
('音箱', '桌面音箱、蓝牙音箱、耳机', 3, 2, 4, '0/3'),
('摄像头', '网络摄像头、直播摄像头', 3, 2, 5, '0/3'),
('麦克风', '直播麦克风、会议麦克风', 3, 2, 6, '0/3');

-- 更新根分类的子分类数量
UPDATE `category` SET `child_count` = 4 WHERE `id` = 1;
UPDATE `category` SET `child_count` = 8 WHERE `id` = 2;
UPDATE `category` SET `child_count` = 6 WHERE `id` = 3;

-- 插入默认仓库
INSERT IGNORE INTO `warehouse` (`id`, `name`, `code`, `type`, `address`, `contact_name`, `contact_phone`, `area`, `capacity`, `status`) VALUES
(1, '中央仓库', 'WH001', 1, '北京市朝阳区', '张管理员', '13800138001', 5000.00, 100000, 1),
(2, '上海分仓', 'WH002', 2, '上海市浦东新区', '李管理员', '13800138002', 3000.00, 50000, 1),
(3, '广州分仓', 'WH003', 2, '广州市天河区', '王管理员', '13800138003', 2000.00, 30000, 1);

-- ==================== 创建视图 ====================

-- 商品详情视图（包含分类名称和供应商名称）
CREATE OR REPLACE VIEW `v_product_detail` AS
SELECT
    p.*,
    c.name AS category_name,
    u.username AS supplier_name,
    si.company_name AS supplier_company
FROM `product` p
LEFT JOIN `category` c ON p.category_id = c.id
LEFT JOIN `user` u ON p.supplier_id = u.id
LEFT JOIN `supplier_info` si ON p.supplier_id = si.user_id
WHERE p.deleted = 0;

-- 订单详情视图（包含用户信息和订单项统计）
CREATE OR REPLACE VIEW `v_order_detail` AS
SELECT
    o.*,
    u.username,
    u.nickname,
    u.phone AS user_phone,
    u.email AS user_email,
    (SELECT COUNT(*) FROM `order_item` oi WHERE oi.order_id = o.id AND oi.deleted = 0) AS item_count,
    (SELECT SUM(oi.quantity) FROM `order_item` oi WHERE oi.order_id = o.id AND oi.deleted = 0) AS total_quantity
FROM `order` o
LEFT JOIN `user` u ON o.user_id = u.id
WHERE o.deleted = 0;

-- 库存详情视图（包含商品信息和仓库信息）
CREATE OR REPLACE VIEW `v_inventory_detail` AS
SELECT
    i.*,
    p.name AS product_name,
    p.brand AS product_brand,
    p.model AS product_model,
    p.main_image AS product_image,
    w.name AS warehouse_name,
    w.code AS warehouse_code,
    u.username AS supplier_name,
    si.company_name AS supplier_company
FROM `inventory` i
LEFT JOIN `product` p ON i.product_id = p.id
LEFT JOIN `warehouse` w ON i.warehouse_id = w.id
LEFT JOIN `user` u ON i.supplier_id = u.id
LEFT JOIN `supplier_info` si ON i.supplier_id = si.user_id
WHERE i.deleted = 0;

-- ==================== 创建存储过程 ====================

-- 更新商品库存的存储过程
DELIMITER $$
CREATE PROCEDURE `sp_update_product_stock`(
    IN p_product_id BIGINT,
    IN p_quantity_change INT,
    IN p_operation_type TINYINT, -- 1-增加库存，2-减少库存，3-锁定库存，4-解锁库存
    IN p_order_id BIGINT,
    IN p_operator_id BIGINT,
    IN p_remark VARCHAR(500)
)
BEGIN
    DECLARE v_current_stock INT;
    DECLARE v_sku VARCHAR(100);
    DECLARE v_warehouse_id BIGINT DEFAULT 1;
    DECLARE v_inventory_id BIGINT;
    DECLARE v_before_quantity INT;
    DECLARE v_after_quantity INT;

    -- 获取商品SKU和当前库存
    SELECT sku INTO v_sku FROM `product` WHERE id = p_product_id;

    -- 获取默认仓库的库存记录
    SELECT id, available_quantity INTO v_inventory_id, v_current_stock
    FROM `inventory`
    WHERE product_id = p_product_id AND sku = v_sku AND warehouse_id = v_warehouse_id;

    -- 如果库存记录不存在，创建新的库存记录
    IF v_inventory_id IS NULL THEN
        INSERT INTO `inventory` (product_id, sku, warehouse_id, total_quantity, available_quantity, supplier_id)
        SELECT p_product_id, v_sku, v_warehouse_id, 0, 0, supplier_id
        FROM `product` WHERE id = p_product_id;

        SET v_inventory_id = LAST_INSERT_ID();
        SET v_current_stock = 0;
    END IF;

    -- 根据操作类型更新库存
    SET v_before_quantity = v_current_stock;

    CASE p_operation_type
        WHEN 1 THEN -- 增加库存
            UPDATE `inventory`
            SET total_quantity = total_quantity + p_quantity_change,
                available_quantity = available_quantity + p_quantity_change,
                updated_at = NOW()
            WHERE id = v_inventory_id;
            SET v_after_quantity = v_current_stock + p_quantity_change;

        WHEN 2 THEN -- 减少库存（出库）
            IF v_current_stock >= p_quantity_change THEN
                UPDATE `inventory`
                SET total_quantity = total_quantity - p_quantity_change,
                    available_quantity = available_quantity - p_quantity_change,
                    sold_quantity = sold_quantity + p_quantity_change,
                    updated_at = NOW()
                WHERE id = v_inventory_id;
                SET v_after_quantity = v_current_stock - p_quantity_change;
            ELSE
                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '库存不足';
            END IF;

        WHEN 3 THEN -- 锁定库存
            IF v_current_stock >= p_quantity_change THEN
                UPDATE `inventory`
                SET available_quantity = available_quantity - p_quantity_change,
                    locked_quantity = locked_quantity + p_quantity_change,
                    updated_at = NOW()
                WHERE id = v_inventory_id;
                SET v_after_quantity = v_current_stock - p_quantity_change;
            ELSE
                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '库存不足';
            END IF;

        WHEN 4 THEN -- 解锁库存
            UPDATE `inventory`
            SET available_quantity = available_quantity + p_quantity_change,
                locked_quantity = locked_quantity - p_quantity_change,
                updated_at = NOW()
            WHERE id = v_inventory_id;
            SET v_after_quantity = v_current_stock + p_quantity_change;

        ELSE
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '无效的操作类型';
    END CASE;

    -- 记录库存操作日志
    INSERT INTO `inventory_log` (
        inventory_id, product_id, sku, warehouse_id, operation_type, quantity,
        before_quantity, after_quantity, order_id, remark, operator_id, operator_name
    ) VALUES (
        v_inventory_id, p_product_id, v_sku, v_warehouse_id, p_operation_type, p_quantity_change,
        v_before_quantity, v_after_quantity, p_order_id, p_remark, p_operator_id,
        (SELECT username FROM `user` WHERE id = p_operator_id)
    );

    -- 更新商品表的库存数量
    UPDATE `product`
    SET stock_quantity = (SELECT available_quantity FROM `inventory` WHERE id = v_inventory_id),
        updated_at = NOW()
    WHERE id = p_product_id;

    SELECT 1 AS result, '库存更新成功' AS message;
END$$
DELIMITER ;

-- ==================== 创建触发器 ====================

-- 订单项插入后更新订单总金额的触发器
DELIMITER $$
CREATE TRIGGER `tr_order_item_after_insert` AFTER INSERT ON `order_item`
FOR EACH ROW
BEGIN
    DECLARE v_total_amount DECIMAL(10, 2);

    SELECT SUM(total_price) INTO v_total_amount
    FROM `order_item`
    WHERE order_id = NEW.order_id AND deleted = 0;

    UPDATE `order`
    SET total_amount = COALESCE(v_total_amount, 0),
        pay_amount = COALESCE(v_total_amount, 0) + freight_amount - discount_amount,
        updated_at = NOW()
    WHERE id = NEW.order_id;
END$$
DELIMITER ;

-- 订单项更新后更新订单总金额的触发器
DELIMITER $$
CREATE TRIGGER `tr_order_item_after_update` AFTER UPDATE ON `order_item`
FOR EACH ROW
BEGIN
    DECLARE v_total_amount DECIMAL(10, 2);

    SELECT SUM(total_price) INTO v_total_amount
    FROM `order_item`
    WHERE order_id = NEW.order_id AND deleted = 0;

    UPDATE `order`
    SET total_amount = COALESCE(v_total_amount, 0),
        pay_amount = COALESCE(v_total_amount, 0) + freight_amount - discount_amount,
        updated_at = NOW()
    WHERE id = NEW.order_id;
END$$
DELIMITER ;

-- 订单项删除后更新订单总金额的触发器
DELIMITER $$
CREATE TRIGGER `tr_order_item_after_delete` AFTER DELETE ON `order_item`
FOR EACH ROW
BEGIN
    DECLARE v_total_amount DECIMAL(10, 2);

    SELECT SUM(total_price) INTO v_total_amount
    FROM `order_item`
    WHERE order_id = OLD.order_id AND deleted = 0;

    UPDATE `order`
    SET total_amount = COALESCE(v_total_amount, 0),
        pay_amount = COALESCE(v_total_amount, 0) + freight_amount - discount_amount,
        updated_at = NOW()
    WHERE id = OLD.order_id;
END$$
DELIMITER ;

-- 商品评价插入后更新商品评分和评价数的触发器
DELIMITER $$
CREATE TRIGGER `tr_product_review_after_insert` AFTER INSERT ON `product_review`
FOR EACH ROW
BEGIN
    DECLARE v_avg_rating DECIMAL(3, 2);
    DECLARE v_review_count INT;

    -- 计算平均评分
    SELECT AVG(rating), COUNT(*) INTO v_avg_rating, v_review_count
    FROM `product_review`
    WHERE product_id = NEW.product_id AND status = 1 AND deleted = 0;

    -- 更新商品表的评分和评价数
    UPDATE `product`
    SET rating = COALESCE(v_avg_rating, 0),
        review_count = v_review_count,
        updated_at = NOW()
    WHERE id = NEW.product_id;

    -- 更新订单项的评价状态
    UPDATE `order_item`
    SET is_commented = 1,
        review_id = NEW.id,
        updated_at = NOW()
    WHERE id = NEW.order_item_id;

    -- 更新订单的评价状态（如果所有订单项都已评价）
    UPDATE `order` o
    SET is_commented = CASE
        WHEN (SELECT COUNT(*) FROM `order_item` oi WHERE oi.order_id = o.id AND oi.deleted = 0) =
             (SELECT COUNT(*) FROM `order_item` oi WHERE oi.order_id = o.id AND oi.is_commented = 1 AND oi.deleted = 0)
        THEN 1 ELSE 0 END,
        updated_at = NOW()
    WHERE o.id = NEW.order_id;
END$$
DELIMITER ;

-- ==================== 创建索引优化 ====================

-- 为常用查询字段创建复合索引
CREATE INDEX `idx_product_search` ON `product` (`category_id`, `status`, `audit_status`, `is_recommended`, `rating`, `sales_count`);
CREATE INDEX `idx_order_search` ON `order` (`user_id`, `order_status`, `pay_status`, `created_at`);
CREATE INDEX `idx_user_search` ON `user` (`user_type`, `status`, `created_at`);
CREATE INDEX `idx_category_tree` ON `category` (`parent_id`, `level`, `sort_order`);

-- ==================== 数据库版本记录 ====================

CREATE TABLE IF NOT EXISTS `db_version` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `version` VARCHAR(20) NOT NULL COMMENT '版本号',
    `description` VARCHAR(500) NOT NULL COMMENT '版本描述',
    `sql_file` VARCHAR(100) NOT NULL COMMENT 'SQL文件名',
    `executed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据库版本记录表';

-- 记录当前版本
INSERT INTO `db_version` (`version`, `description`, `sql_file`) VALUES
('1.0.0', '初始版本，创建所有基础表结构', 'schema_v1.0.0.sql');

-- ==================== 完成 ====================

SELECT '数据库创建完成！' AS message;