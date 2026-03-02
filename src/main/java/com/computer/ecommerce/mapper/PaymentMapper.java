package com.computer.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.computer.ecommerce.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付记录Mapper接口
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}