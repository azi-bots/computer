package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.entity.OrderItem;
import com.computer.ecommerce.service.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单项控制器
 */
@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private IOrderItemService orderItemService;

    /**
     * 获取所有订单项
     */
    @GetMapping
    public List<OrderItem> list() {
        return orderItemService.list();
    }

    /**
     * 分页查询订单项
     */
    @GetMapping("/page")
    public IPage<OrderItem> page(@RequestParam(defaultValue = "1") Integer current,
                                 @RequestParam(defaultValue = "10") Integer size) {
        Page<OrderItem> page = new Page<>(current, size);
        return orderItemService.page(page);
    }

    /**
     * 根据ID获取订单项
     */
    @GetMapping("/{id}")
    public OrderItem getById(@PathVariable Long id) {
        return orderItemService.getById(id);
    }

    /**
     * 创建订单项
     */
    @PostMapping
    public boolean create(@RequestBody OrderItem orderItem) {
        return orderItemService.save(orderItem);
    }

    /**
     * 更新订单项
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody OrderItem orderItem) {
        orderItem.setId(id);
        return orderItemService.updateById(orderItem);
    }

    /**
     * 删除订单项
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return orderItemService.removeById(id);
    }

    /**
     * 根据订单ID查询订单项
     */
    @GetMapping("/order/{orderId}")
    public List<OrderItem> getByOrderId(@PathVariable Long orderId) {
        QueryWrapper<OrderItem> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        return orderItemService.list(wrapper);
    }
}