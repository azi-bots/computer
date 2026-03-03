package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.common.PageResult;
import com.computer.ecommerce.common.Result;
import com.computer.ecommerce.dto.ProductRequest;
import com.computer.ecommerce.dto.ProductResponse;
import com.computer.ecommerce.entity.Product;
import com.computer.ecommerce.service.IProductService;
import com.computer.ecommerce.util.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private DtoConverter dtoConverter;

    /**
     * 获取所有商品
     */
    @GetMapping
    public Result<List<ProductResponse>> list() {
        List<Product> products = productService.list();
        List<ProductResponse> responses = products.stream()
            .map(dtoConverter::toProductResponse)
            .collect(Collectors.toList());
        return Result.ok(responses);
    }

    /**
     * 分页查询商品
     */
    @GetMapping("/page")
    public Result<PageResult<Product>> page(@RequestParam(defaultValue = "1") Integer current,
                                            @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> page = new Page<>(current, size);
        IPage<Product> productPage = productService.page(page);
        return Result.ok(PageResult.of(productPage));
    }

    /**
     * 获取热门商品
     */
    @GetMapping("/hot")
    public Result<List<ProductResponse>> getHotProducts(@RequestParam(defaultValue = "10") Integer limit) {
        List<Product> products = productService.getHotProducts(limit);
        List<ProductResponse> responses = products.stream()
            .map(dtoConverter::toProductResponse)
            .collect(Collectors.toList());
        return Result.ok(responses);
    }

    /**
     * 获取新品
     */
    @GetMapping("/new")
    public Result<List<ProductResponse>> getNewProducts(@RequestParam(defaultValue = "10") Integer limit) {
        List<Product> products = productService.getNewProducts(limit);
        List<ProductResponse> responses = products.stream()
            .map(dtoConverter::toProductResponse)
            .collect(Collectors.toList());
        return Result.ok(responses);
    }

    /**
     * 根据ID获取商品
     */
    @GetMapping("/{id}")
    public Result<ProductResponse> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.fail("商品不存在");
        }
        ProductResponse response = dtoConverter.toProductResponse(product);
        return Result.ok(response);
    }

    /**
     * 创建商品
     */
    @PostMapping
    public Result<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest) {
        Product product = dtoConverter.toProduct(productRequest);
        boolean success = productService.save(product);
        if (success) {
            ProductResponse response = dtoConverter.toProductResponse(product);
            return Result.ok("创建成功", response);
        }
        return Result.fail("创建失败");
    }

    /**
     * 更新商品
     */
    @PutMapping("/{id}")
    public Result<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        Product existingProduct = productService.getById(id);
        if (existingProduct == null) {
            return Result.fail("商品不存在");
        }

        Product product = dtoConverter.toProduct(productRequest, existingProduct);
        product.setId(id);
        boolean success = productService.updateById(product);
        if (success) {
            ProductResponse response = dtoConverter.toProductResponse(product);
            return Result.ok("更新成功", response);
        }
        return Result.fail("更新失败");
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean success = productService.removeById(id);
        if (success) {
            return Result.ok("删除成功", true);
        }
        return Result.fail("删除失败");
    }

    /**
     * 根据分类ID查询商品
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<Product>> getByCategoryId(@PathVariable Long categoryId) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id", categoryId);
        List<Product> products = productService.list(wrapper);
        return Result.ok(products);
    }

    /**
     * 根据分类ID分页查询商品
     */
    @GetMapping("/category/{categoryId}/page")
    public Result<PageResult<Product>> getByCategoryIdPage(@PathVariable Long categoryId,
                                                           @RequestParam(defaultValue = "1") Integer current,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> page = new Page<>(current, size);
        IPage<Product> productPage = productService.listByCategory(categoryId, page);
        return Result.ok(PageResult.of(productPage));
    }

    /**
     * 根据供应商ID分页查询商品
     */
    @GetMapping("/supplier/{supplierId}/page")
    public Result<PageResult<Product>> getBySupplierIdPage(@PathVariable Long supplierId,
                                                           @RequestParam(defaultValue = "1") Integer current,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> page = new Page<>(current, size);
        IPage<Product> productPage = productService.listBySupplier(supplierId, page);
        return Result.ok(PageResult.of(productPage));
    }
}