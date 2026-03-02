package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.entity.Order;
import com.computer.ecommerce.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 获取所有订单
     */
    @GetMapping
    public List<Order> list() {
        return orderService.list();
    }

    /**
     * 分页查询订单
     */
    @GetMapping("/page")
    public IPage<Order> page(@RequestParam(defaultValue = "1") Integer current,
                             @RequestParam(defaultValue = "10") Integer size) {
        Page<Order> page = new Page<>(current, size);
        return orderService.page(page);
    }

    /**
     * 根据ID获取订单
     */
    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    /**
     * 创建订单
     */
    @PostMapping
    public boolean create(@RequestBody Order order) {
        return orderService.save(order);
    }

    /**
     * 更新订单
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        return orderService.updateById(order);
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return orderService.removeById(id);
    }

    /**
     * 根据用户ID查询订单
     */
    @GetMapping("/user/{userId}")
    public List<Order> getByUserId(@PathVariable Long userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return orderService.list(wrapper);
    }

    /**
     * 根据用户ID分页查询订单
     */
    @GetMapping("/user/{userId}/page")
    public IPage<Order> getByUserIdPage(@PathVariable Long userId,
                                        @RequestParam(defaultValue = "1") Integer current,
                                        @RequestParam(defaultValue = "10") Integer size) {
        Page<Order> page = new Page<>(current, size);
        return orderService.listByUser(userId, page);
    }

    /**
     * 根据供应商ID分页查询订单
     */
    @GetMapping("/supplier/{supplierId}/page")
    public IPage<Order> getBySupplierIdPage(@PathVariable Long supplierId,
                                            @RequestParam(defaultValue = "1") Integer current,
                                            @RequestParam(defaultValue = "10") Integer size) {
        Page<Order> page = new Page<>(current, size);
        return orderService.listBySupplier(supplierId, page);
    }
}