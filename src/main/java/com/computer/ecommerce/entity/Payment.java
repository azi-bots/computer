package com.computer.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("payment")
public class Payment extends BaseEntity {
    /**
     * 支付单号（唯一）
     */
    private String paymentNo;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 支付金额（单位：元）
     */
    private BigDecimal amount;

    /**
     * 支付方式：1-微信支付，2-支付宝，3-银行卡，4-货到付款
     */
    private Integer payType;

    /**
     * 支付状态：0-待支付，1-支付中，2-支付成功，3-支付失败，4-已退款
     */
    private Integer payStatus = 0;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付交易号（第三方支付平台返回）
     */
    private String transactionId;

    /**
     * 支付备注
     */
    private String remark;

    /**
     * 支付IP地址
     */
    private String payIp;

    /**
     * 支付设备信息
     */
    private String payDevice;

    /**
     * 支付渠道返回的原始数据（JSON格式）
     */
    private String rawResponse;

    /**
     * 退款金额（单位：元）
     */
    private BigDecimal refundAmount = BigDecimal.ZERO;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 退款原因
     */
    private String refundReason;
}