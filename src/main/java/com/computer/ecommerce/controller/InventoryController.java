package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.entity.Inventory;
import com.computer.ecommerce.service.IInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存控制器
 */
@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    @Autowired
    private IInventoryService inventoryService;

    /**
     * 获取所有库存记录
     */
    @GetMapping
    public List<Inventory> list() {
        return inventoryService.list();
    }

    /**
     * 分页查询库存记录
     */
    @GetMapping("/page")
    public IPage<Inventory> page(@RequestParam(defaultValue = "1") Integer current,
                                 @RequestParam(defaultValue = "10") Integer size) {
        Page<Inventory> page = new Page<>(current, size);
        return inventoryService.page(page);
    }

    /**
     * 根据ID获取库存记录
     */
    @GetMapping("/{id}")
    public Inventory getById(@PathVariable Long id) {
        return inventoryService.getById(id);
    }

    /**
     * 创建库存记录
     */
    @PostMapping
    public boolean create(@RequestBody Inventory inventory) {
        return inventoryService.save(inventory);
    }

    /**
     * 更新库存记录
     */
    @PutMapping("/{id}")
    public boolean update(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id);
        return inventoryService.updateById(inventory);
    }

    /**
     * 删除库存记录
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return inventoryService.removeById(id);
    }

    /**
     * 根据商品ID查询库存记录
     */
    @GetMapping("/product/{productId}")
    public List<Inventory> getByProductId(@PathVariable Long productId) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        return inventoryService.list(wrapper);
    }
}