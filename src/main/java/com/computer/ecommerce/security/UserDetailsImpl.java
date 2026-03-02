package com.computer.ecommerce.security;

import com.computer.ecommerce.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Spring Security UserDetails实现
 */
@Data
public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.user = user;
        // 根据userType设置角色
        String role = getUserRole(user.getUserType());
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    /**
     * 根据用户类型获取角色
     *
     * @param userType 用户类型
     * @return 角色字符串
     */
    private String getUserRole(Integer userType) {
        if (userType == null) {
            return "ROLE_USER";
        }
        switch (userType) {
            case 2:
                return "ROLE_SUPPLIER";
            case 3:
                return "ROLE_ADMIN";
            default:
                return "ROLE_USER";
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != 2; // 2表示锁定状态
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == 1; // 1表示正常状态
    }

    public Long getUserId() {
        return user.getId();
    }

    public Integer getUserType() {
        return user.getUserType();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPhone() {
        return user.getPhone();
    }
}