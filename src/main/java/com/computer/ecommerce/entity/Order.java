package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("`order`")
public class Order extends BaseEntity {
    /**
     * 订单号（唯一）
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单总金额（单位：元）
     */
    private BigDecimal totalAmount;

    /**
     * 实付金额（单位：元）
     */
    private BigDecimal payAmount;

    /**
     * 运费金额（单位：元）
     */
    private BigDecimal freightAmount = BigDecimal.ZERO;

    /**
     * 优惠金额（单位：元）
     */
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * 订单状态：
     * 0-待付款，1-已付款，2-待发货，3-已发货，4-已完成，5-已取消，6-已关闭，7-退款中，8-已退款
     */
    private Integer orderStatus = 0;

    /**
     * 支付状态：0-未支付，1-支付中，2-已支付，3-支付失败，4-已退款
     */
    private Integer payStatus = 0;

    /**
     * 支付方式：1-微信支付，2-支付宝，3-银行卡，4-货到付款
     */
    private Integer payType;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付交易号
     */
    private String transactionId;

    /**
     * 发货状态：0-未发货，1-部分发货，2-已发货
     */
    private Integer deliveryStatus = 0;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流单号
     */
    private String logisticsNo;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 收货地址（省市区街道详细地址）
     */
    private String receiverAddress;

    /**
     * 收货地址邮编
     */
    private String receiverPostCode;

    /**
     * 用户订单备注
     */
    private String userRemark;

    /**
     * 商家备注
     */
    private String merchantRemark;

    /**
     * 订单来源：1-Web，2-Android，3-iOS，4-小程序
     */
    private Integer orderSource = 1;

    /**
     * 自动确认收货时间（天数）
     */
    private Integer autoConfirmDays = 7;

    /**
     * 确认收货时间
     */
    private LocalDateTime confirmTime;

    /**
     * 订单完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 订单取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 订单取消原因
     */
    private String cancelReason;

    /**
     * 是否已评价：0-未评价，1-已评价
     */
    private Integer isCommented = 0;

    /**
     * 发票类型：0-不开发票，1-个人发票，2-公司发票
     */
    private Integer invoiceType = 0;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 纳税人识别号
     */
    private String taxpayerId;

    // 非数据库字段
    /**
     * 用户名
     */
    @TableField(exist = false)
    private String username;

    /**
     * 订单商品项列表
     */
    @TableField(exist = false)
    private java.util.List<OrderItem> orderItems;
}