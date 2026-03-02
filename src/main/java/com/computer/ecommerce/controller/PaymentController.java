package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.entity.Payment;
import com.computer.ecommerce.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付记录控制器
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    /**
     * 获取所有支付记录
     */
    @GetMapping
    public List<Payment> list() {
        return paymentService.list();
    }

    /**
     * 分页查询支付记录
     */
    @GetMapping("/page")
    public IPage<Payment> page(@RequestParam(defaultValue = "1") Integer current,
                               @RequestParam(defaultValue = "10") Integer size) {
        Page<Payment> page = new Page<>(current, size);
        return paymentService.page(page);
    }

    /**
     * 根据ID获取支付记录
     */
    @GetMapping("/{id}")
    public Payment getById(@PathVariable Long id) {
        return paymentService.getById(id);
    }

    /**
     * 创建支付记录
     */
    @PostMapping
    public boolean create(@RequestBody Payment payment) {
        return paymentService.save(payment);
    }

    /**
     * 更新支付记录
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody Payment payment) {
        payment.setId(id);
        return paymentService.updateById(payment);
    }

    /**
     * 删除支付记录
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return paymentService.removeById(id);
    }

    /**
     * 根据订单ID查询支付记录
     */
    @GetMapping("/order/{orderId}")
    public List<Payment> getByOrderId(@PathVariable Long orderId) {
        QueryWrapper<Payment> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        return paymentService.list(wrapper);
    }
}