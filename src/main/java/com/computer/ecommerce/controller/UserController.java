package com.computer.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.ecommerce.common.PageResult;
import com.computer.ecommerce.common.Result;
import com.computer.ecommerce.entity.User;
import com.computer.ecommerce.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 获取所有用户
     */
    @GetMapping
    public Result<List<User>> list() {
        List<User> users = userService.list();
        return Result.ok(users);
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public Result<PageResult<User>> page(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size) {
        Page<User> page = new Page<>(current, size);
        IPage<User> userPage = userService.page(page);
        return Result.ok(PageResult.of(userPage));
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        return Result.ok(user);
    }

    /**
     * 创建用户
     */
    @PostMapping
    public Result<Boolean> create(@RequestBody User user) {
        boolean success = userService.save(user);
        if (success) {
            return Result.ok("创建成功", true);
        }
        return Result.fail("创建失败");
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        boolean success = userService.updateById(user);
        if (success) {
            return Result.ok("更新成功", true);
        }
        return Result.fail("更新失败");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        if (success) {
            return Result.ok("删除成功", true);
        }
        return Result.fail("删除失败");
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/username/{username}")
    public Result<User> getByUsername(@PathVariable String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userService.getOne(wrapper);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        return Result.ok(user);
    }

    /**
     * 上传用户头像
     */
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail("文件不能为空");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.fail("只能上传图片文件");
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ?
            originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String fileName = System.currentTimeMillis() + "_" + (int)(Math.random() * 1000) + fileExtension;

        // 保存文件（简化处理，实际项目中应使用云存储或配置可访问的路径）
        try {
            // TODO: 保存到配置的upload-dir目录，这里仅示例
            // Path uploadPath = Paths.get(uploadDir);
            // Files.createDirectories(uploadPath);
            // Path filePath = uploadPath.resolve(fileName);
            // file.transferTo(filePath);

            // 模拟成功，返回虚拟URL
            String avatarUrl = "/uploads/avatars/" + fileName;
            return Result.ok("头像上传成功", avatarUrl);
        } catch (Exception e) {
            return Result.fail("头像上传失败: " + e.getMessage());
        }
    }
}