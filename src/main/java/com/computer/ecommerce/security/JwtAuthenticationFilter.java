package com.computer.ecommerce.security;

import com.computer.ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(jwtUtil.getHeader());

            if (authHeader != null && authHeader.startsWith(jwtUtil.getTokenPrefix())) {
                String token = jwtUtil.extractTokenFromHeader(authHeader);

                if (token != null && jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getUsernameFromToken(token);
                    Long userId = jwtUtil.getUserIdFromToken(token);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // 验证令牌中的用户ID与数据库中的用户ID是否一致
                        if (userDetails instanceof UserDetailsImpl) {
                            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
                            if (!userDetailsImpl.getUserId().equals(userId)) {
                                // 用户ID不匹配，可能是令牌被篡改
                                logger.warn("JWT令牌用户ID不匹配: tokenUserId=" + userId + ", dbUserId=" + userDetailsImpl.getUserId());
                                filterChain.doFilter(request, response);
                                return;
                            }
                        }

                        UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 检查令牌是否即将过期（最后5分钟），如果是，在响应头中添加新令牌
                        if (jwtUtil.isTokenExpiringSoon(token, 5 * 60 * 1000)) {
                            String newToken = jwtUtil.refreshToken(token);
                            response.setHeader(jwtUtil.getHeader(), jwtUtil.getTokenPrefix() + newToken);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("JWT认证处理失败", e);
            // 不清除SecurityContext，让后续过滤器处理
        }

        filterChain.doFilter(request, response);
    }
}