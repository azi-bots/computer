package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.entity.UserAddress;
import com.computer.ecommerce.service.IUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户地址控制器
 */
@RestController
@RequestMapping("/api/user-addresses")
public class UserAddressController {

    @Autowired
    private IUserAddressService userAddressService;

    /**
     * 获取所有用户地址
     */
    @GetMapping
    public List<UserAddress> list() {
        return userAddressService.list();
    }

    /**
     * 分页查询用户地址
     */
    @GetMapping("/page")
    public IPage<UserAddress> page(@RequestParam(defaultValue = "1") Integer current,
                                   @RequestParam(defaultValue = "10") Integer size) {
        Page<UserAddress> page = new Page<>(current, size);
        return userAddressService.page(page);
    }

    /**
     * 根据ID获取用户地址
     */
    @GetMapping("/{id}")
    public UserAddress getById(@PathVariable Long id) {
        return userAddressService.getById(id);
    }

    /**
     * 创建用户地址
     */
    @PostMapping
    public boolean create(@RequestBody UserAddress userAddress) {
        return userAddressService.save(userAddress);
    }

    /**
     * 更新用户地址
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody UserAddress userAddress) {
        userAddress.setId(id);
        return userAddressService.updateById(userAddress);
    }

    /**
     * 删除用户地址
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return userAddressService.removeById(id);
    }

    /**
     * 根据用户ID查询用户地址
     */
    @GetMapping("/user/{userId}")
    public List<UserAddress> getByUserId(@PathVariable Long userId) {
        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return userAddressService.list(wrapper);
    }
}