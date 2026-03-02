package com.computer.ecommerce.util;

import com.computer.ecommerce.dto.ProductRequest;
import com.computer.ecommerce.dto.ProductResponse;
import com.computer.ecommerce.dto.UserRequest;
import com.computer.ecommerce.dto.UserResponse;
import com.computer.ecommerce.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * DTO转换工具类
 */
@Component
public class DtoConverter {

    private final PasswordEncoder passwordEncoder;

    public DtoConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 将UserRequest转换为User实体（用于创建）
     */
    public User toUser(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest, user);

        // 密码加密
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        return user;
    }

    /**
     * 将UserRequest转换为User实体（用于更新）
     */
    public User toUser(UserRequest userRequest, User existingUser) {
        BeanUtils.copyProperties(userRequest, existingUser, "id", "password", "createTime", "updateTime");

        // 如果提供了新密码，则加密
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        return existingUser;
    }

    /**
     * 将User实体转换为UserResponse
     */
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);

        // 复制供应商特有字段
        response.setCompanyName(user.getCompanyName());
        response.setBusinessLicense(user.getBusinessLicense());
        response.setContactName(user.getContactName());
        response.setContactPhone(user.getContactPhone());
        response.setSupplierDescription(user.getSupplierDescription());
        response.setSupplierLevel(user.getSupplierLevel());
        response.setSupplierStatus(user.getSupplierStatus());

        // 复制管理员特有字段
        response.setAdminRole(user.getAdminRole());
        response.setDepartment(user.getDepartment());

        return response;
    }

    /**
     * 将ProductRequest转换为Product实体（用于创建）
     */
    public com.computer.ecommerce.entity.Product toProduct(ProductRequest productRequest) {
        com.computer.ecommerce.entity.Product product = new com.computer.ecommerce.entity.Product();
        BeanUtils.copyProperties(productRequest, product);
        return product;
    }

    /**
     * 将ProductRequest转换为Product实体（用于更新）
     */
    public com.computer.ecommerce.entity.Product toProduct(ProductRequest productRequest,
                                                           com.computer.ecommerce.entity.Product existingProduct) {
        BeanUtils.copyProperties(productRequest, existingProduct,
            "id", "salesCount", "clickCount", "favoriteCount", "rating", "reviewCount",
            "createTime", "updateTime");
        return existingProduct;
    }

    /**
     * 将Product实体转换为ProductResponse
     */
    public ProductResponse toProductResponse(com.computer.ecommerce.entity.Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();
        BeanUtils.copyProperties(product, response);

        // 复制扩展字段
        response.setCategoryName(product.getCategoryName());
        response.setSupplierName(product.getSupplierName());

        return response;
    }

    /**
     * 通用复制属性（忽略null值）
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 获取null属性名数组
     */
    private static String[] getNullPropertyNames(Object source) {
        return Arrays.stream(org.springframework.beans.BeanUtils.getPropertyDescriptors(source.getClass()))
            .filter(pd -> {
                try {
                    return pd.getReadMethod().invoke(source) == null;
                } catch (Exception e) {
                    return false;
                }
            })
            .map(java.beans.PropertyDescriptor::getName)
            .toArray(String[]::new);
    }
}