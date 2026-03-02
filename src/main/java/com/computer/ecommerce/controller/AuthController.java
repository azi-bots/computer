package com.computer.ecommerce.controller;

import com.computer.ecommerce.common.Result;
import com.computer.ecommerce.dto.AuthResponse;
import com.computer.ecommerce.dto.ChangePasswordRequest;
import com.computer.ecommerce.dto.LoginRequest;
import com.computer.ecommerce.dto.PasswordResetRequest;
import com.computer.ecommerce.dto.RegisterRequest;
import com.computer.ecommerce.dto.ResetPasswordConfirmRequest;
import com.computer.ecommerce.dto.UserResponse;
import com.computer.ecommerce.entity.User;
import com.computer.ecommerce.security.UserDetailsImpl;
import com.computer.ecommerce.security.UserDetailsServiceImpl;
import com.computer.ecommerce.service.IUserService;
import com.computer.ecommerce.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

/**
 * 认证控制器（处理登录、注册、注销等）
 */
@Tag(name = "认证管理", description = "用户登录、注册、注销、令牌刷新等认证相关操作")
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     */
    @Operation(
        summary = "用户登录",
        description = "使用用户名/邮箱/手机号和密码登录系统，返回JWT令牌"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "登录成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "401", description = "用户名或密码错误"),
        @ApiResponse(responseCode = "403", description = "账户被禁用或锁定")
    })
    @PostMapping("/login")
    public Result<AuthResponse> login(
            @Parameter(description = "登录请求参数", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 获取用户详情
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 生成JWT令牌
        String token = jwtUtil.generateToken(
            userDetails.getUsername(),
            userDetails.getUserId(),
            userDetails.getAuthorities().iterator().next().getAuthority()
        );

        // 更新最后登录时间（异步处理，这里简化）
        User user = userDetails.getUser();
        user.setLastLoginTime(java.time.LocalDateTime.now());
        // 注意：实际项目中应该记录登录IP
        userService.updateById(user);

        // 转换为UserResponse
        UserResponse userResponse = convertToUserResponse(user);

        // 计算令牌过期时间（秒）
        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
        long expiresIn = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;

        // 构建响应（使用新的构造方法）
        AuthResponse response = new AuthResponse(token, userResponse, expiresIn);

        return Result.ok("登录成功", response);
    }

    /**
     * 用户注册
     */
    @Operation(
        summary = "用户注册",
        description = "注册新用户，支持普通用户、供应商、管理员类型"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "注册成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "409", description = "用户名/邮箱/手机号已存在")
    })
    @PostMapping("/register")
    public Result<AuthResponse> register(
            @Parameter(description = "注册请求参数", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return Result.fail("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return Result.fail("邮箱已存在");
        }

        // 检查手机号是否已存在（如果提供了手机号）
        if (registerRequest.getPhone() != null && !registerRequest.getPhone().isEmpty()) {
            if (userService.existsByPhone(registerRequest.getPhone())) {
                return Result.fail("手机号已存在");
            }
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setNickname(registerRequest.getNickname() != null ? registerRequest.getNickname() : registerRequest.getUsername());
        user.setRealName(registerRequest.getRealName());
        user.setUserType(registerRequest.getUserType() != null ? registerRequest.getUserType() : 1);
        user.setStatus(1); // 正常状态

        boolean success = userService.save(user);
        if (!success) {
            return Result.fail("注册失败");
        }

        // 生成JWT令牌（需要获取用户详情）
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(
            userDetails.getUsername(),
            userDetails.getUserId(),
            userDetails.getAuthorities().iterator().next().getAuthority()
        );

        // 转换为UserResponse
        UserResponse userResponse = convertToUserResponse(user);

        // 计算令牌过期时间（秒）
        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
        long expiresIn = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;

        // 构建响应（使用新的构造方法）
        AuthResponse response = new AuthResponse(token, userResponse, expiresIn);

        return Result.ok("注册成功", response);
    }

    /**
     * 用户注销（客户端删除令牌即可）
     */
    @Operation(
        summary = "用户注销",
        description = "注销当前用户，客户端需要删除本地存储的JWT令牌"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "注销成功")
    })
    @SecurityRequirement(name = "JWT")
    @PostMapping("/logout")
    public Result<Void> logout() {
        SecurityContextHolder.clearContext();
        return Result.ok("注销成功");
    }

    /**
     * 刷新令牌
     */
    @Operation(
        summary = "刷新JWT令牌",
        description = "使用旧令牌刷新获取新令牌，旧令牌必须仍然有效"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "令牌刷新成功"),
        @ApiResponse(responseCode = "400", description = "无效的令牌格式"),
        @ApiResponse(responseCode = "401", description = "令牌无效或已过期")
    })
    @SecurityRequirement(name = "JWT")
    @PostMapping("/refresh")
    public Result<AuthResponse> refreshToken(
            @Parameter(description = "授权请求头，格式: Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = jwtUtil.extractTokenFromHeader(authHeader);
        if (token == null) {
            return Result.fail("无效的令牌");
        }

        if (!jwtUtil.validateToken(token)) {
            return Result.fail("令牌无效或已过期");
        }

        // 刷新令牌
        String newToken = jwtUtil.refreshToken(token);

        // 获取用户信息
        String username = jwtUtil.getUsernameFromToken(newToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        // 转换为UserResponse
        UserResponse userResponse = convertToUserResponse(userDetails.getUser());

        // 计算令牌过期时间（秒）
        Date expirationDate = jwtUtil.getExpirationDateFromToken(newToken);
        long expiresIn = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;

        // 构建响应（使用新的构造方法）
        AuthResponse response = new AuthResponse(newToken, userResponse, expiresIn);

        return Result.ok("令牌刷新成功", response);
    }

    /**
     * 修改密码
     */
    @Operation(
        summary = "修改密码",
        description = "修改当前登录用户的密码"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "密码修改成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "401", description = "用户未认证"),
        @ApiResponse(responseCode = "422", description = "旧密码错误")
    })
    @SecurityRequirement(name = "JWT")
    @PutMapping("/change-password")
    public Result<Void> changePassword(
            @Parameter(description = "修改密码请求参数", required = true)
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.fail("用户未认证");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        // 验证旧密码
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return Result.fail("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userService.updateById(user);

        return Result.ok("密码修改成功");
    }

    /**
     * 请求重置密码
     */
    @Operation(
        summary = "请求重置密码",
        description = "发送重置密码邮件（暂未实现邮件发送，仅模拟）"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "重置密码邮件已发送"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "404", description = "邮箱未注册")
    })
    @PostMapping("/reset-password/request")
    public Result<Void> requestPasswordReset(
            @Parameter(description = "重置密码请求参数", required = true)
            @Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        // 检查邮箱是否存在
        if (!userService.existsByEmail(passwordResetRequest.getEmail())) {
            return Result.fail("该邮箱未注册");
        }

        // TODO: 实际项目中应发送重置密码邮件，包含重置令牌
        // 这里仅模拟成功响应
        return Result.ok("重置密码邮件已发送，请查收邮箱");
    }

    /**
     * 重置密码确认
     */
    @Operation(
        summary = "重置密码确认",
        description = "使用重置令牌设置新密码（暂未实现令牌验证，仅模拟）"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "密码重置成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "401", description = "无效或过期的重置令牌")
    })
    @PostMapping("/reset-password/confirm")
    public Result<Void> resetPassword(
            @Parameter(description = "重置密码确认请求参数", required = true)
            @Valid @RequestBody ResetPasswordConfirmRequest resetPasswordConfirmRequest) {
        // TODO: 验证重置令牌的有效性（应从Redis或数据库中检查）
        // 这里仅模拟成功响应
        return Result.ok("密码重置成功");
    }

    /**
     * 验证令牌
     */
    @Operation(
        summary = "验证令牌",
        description = "验证令牌的有效性（可用于邮箱验证、重置密码令牌验证等）"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "令牌有效"),
        @ApiResponse(responseCode = "400", description = "令牌无效或已过期")
    })
    @GetMapping("/verify")
    public Result<Boolean> verifyToken(
            @Parameter(description = "待验证的令牌", required = true)
            @RequestParam String token) {
        // TODO: 根据令牌类型进行验证（JWT令牌、重置令牌等）
        // 这里仅模拟成功响应
        return Result.ok("令牌有效", true);
    }

    /**
     * 获取当前用户信息
     */
    @Operation(
        summary = "获取当前用户信息",
        description = "获取当前认证用户的详细信息"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "获取成功"),
        @ApiResponse(responseCode = "401", description = "用户未认证")
    })
    @SecurityRequirement(name = "JWT")
    @GetMapping("/me")
    public Result<UserResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Result.fail("用户未认证");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserResponse userResponse = convertToUserResponse(userDetails.getUser());

        return Result.ok("获取成功", userResponse);
    }

    /**
     * 将User实体转换为UserResponse
     */
    private UserResponse convertToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setNickname(user.getNickname());
        response.setRealName(user.getRealName());
        response.setAvatar(user.getAvatar());
        response.setUserType(user.getUserType());
        response.setStatus(user.getStatus());
        response.setLastLoginTime(user.getLastLoginTime());
        response.setLastLoginIp(user.getLastLoginIp());
        response.setCreateTime(user.getCreatedAt());
        response.setUpdateTime(user.getUpdatedAt());
        return response;
    }
}