package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.entity.Cart;
import com.computer.ecommerce.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 获取所有购物车项
     */
    @GetMapping
    public List<Cart> list() {
        return cartService.list();
    }

    /**
     * 分页查询购物车项
     */
    @GetMapping("/page")
    public IPage<Cart> page(@RequestParam(defaultValue = "1") Integer current,
                            @RequestParam(defaultValue = "10") Integer size) {
        Page<Cart> page = new Page<>(current, size);
        return cartService.page(page);
    }

    /**
     * 根据ID获取购物车项
     */
    @GetMapping("/{id}")
    public Cart getById(@PathVariable Long id) {
        return cartService.getById(id);
    }

    /**
     * 创建购物车项
     */
    @PostMapping
    public boolean create(@RequestBody Cart cart) {
        return cartService.save(cart);
    }

    /**
     * 更新购物车项
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody Cart cart) {
        cart.setId(id);
        return cartService.updateById(cart);
    }

    /**
     * 删除购物车项
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return cartService.removeById(id);
    }

    /**
     * 根据用户ID查询购物车项
     */
    @GetMapping("/user/{userId}")
    public List<Cart> getByUserId(@PathVariable Long userId) {
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return cartService.list(wrapper);
    }

    /**
     * 根据用户ID分页查询购物车项
     */
    @GetMapping("/user/{userId}/page")
    public IPage<Cart> getByUserIdPage(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size) {
        Page<Cart> page = new Page<>(current, size);
        return cartService.listByUser(userId, page);
    }
}