package com.computer.ecommerce.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.computer.ecommerce.entity.User;
import com.computer.ecommerce.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security UserDetailsService实现
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 支持通过用户名、邮箱或手机号登录
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .or().eq("email", username)
                .or().eq("phone", username);

        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        return new UserDetailsImpl(user);
    }

    /**
     * 根据用户ID加载用户
     *
     * @param userId 用户ID
     * @return UserDetails
     */
    public UserDetails loadUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在，ID: " + userId);
        }
        return new UserDetailsImpl(user);
    }
}