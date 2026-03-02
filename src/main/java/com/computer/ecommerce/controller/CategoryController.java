package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.common.PageResult;
import com.computer.ecommerce.common.Result;
import com.computer.ecommerce.entity.Category;
import com.computer.ecommerce.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类控制器
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    /**
     * 获取所有分类
     */
    @GetMapping
    public Result<List<Category>> list() {
        List<Category> categories = categoryService.list();
        return Result.ok(categories);
    }

    /**
     * 分页查询分类
     */
    @GetMapping("/page")
    public Result<PageResult<Category>> page(@RequestParam(defaultValue = "1") Integer current,
                                             @RequestParam(defaultValue = "10") Integer size) {
        Page<Category> page = new Page<>(current, size);
        IPage<Category> categoryPage = categoryService.page(page);
        return Result.ok(PageResult.of(categoryPage));
    }

    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return Result.fail("分类不存在");
        }
        return Result.ok(category);
    }

    /**
     * 创建分类
     */
    @PostMapping
    public Result<Boolean> create(@RequestBody Category category) {
        boolean success = categoryService.save(category);
        if (success) {
            return Result.ok("创建成功", true);
        }
        return Result.fail("创建失败");
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        boolean success = categoryService.updateById(category);
        if (success) {
            return Result.ok("更新成功", true);
        }
        return Result.fail("更新失败");
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean success = categoryService.removeById(id);
        if (success) {
            return Result.ok("删除成功", true);
        }
        return Result.fail("删除失败");
    }

    /**
     * 根据父分类ID查询子分类
     */
    @GetMapping("/parent/{parentId}")
    public Result<List<Category>> getByParentId(@PathVariable Long parentId) {
        List<Category> categories = categoryService.getByParentId(parentId);
        return Result.ok(categories);
    }
}